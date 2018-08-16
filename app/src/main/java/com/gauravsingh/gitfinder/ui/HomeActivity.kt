package com.gauravsingh.gitfinder.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gauravsingh.gitfinder.R
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.network.GitServer
import com.gauravsingh.gitfinder.network.apiservice.GitApiService
import com.gauravsingh.gitfinder.ui.bottomsheet.FilterBottomSheet
import com.gauravsingh.gitfinder.ui.bottomsheet.ORDER_BY_DESC
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

internal class HomeActivity
    : AppCompatActivity(), GitRepoRecyclerViewAdapter.OnItemClickListener, FilterBottomSheet.FilterListener {


    private lateinit var gitRepoRecyclerViewAdapter: GitRepoRecyclerViewAdapter

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private var lastSearchedString: String = "Gaurav"
    private var sort: String? = null
    private var orderBy: String = ORDER_BY_DESC
    private var isDataFetched = false
    private val networkChangeReceiver: NetworkChangeReceiver by lazy { NetworkChangeReceiver() }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val connMgr: ConnectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val isConnected = connMgr.activeNetworkInfo?.isConnectedOrConnecting == true

            if (!isConnected && !isDataFetched) {
                Snackbar.make(cl_home, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            } else if (!isDataFetched) {

                fetchData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar_home)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_githublaunch)
        fab_home.setOnClickListener { displayFilterBottomSheet() }
    }

    override fun onStart() {
        super.onStart()

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onStop() {

        unregisterReceiver(networkChangeReceiver)
        super.onStop()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_home, menu)

        searchMenuItem = menu?.findItem(R.id.action_search)
        searchMenuItem?.setOnActionExpandListener(actionExpandListener)
        searchView = searchMenuItem?.actionView as SearchView?
        searchView?.setOnQueryTextListener(searchQueryListener)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemCLick(gitRepo: GitRepo) {

        val intent = Intent(this, GitRepoActivity::class.java)
        intent.putExtra("gitRepo", gitRepo)
        startActivity(intent)
    }

    override fun applyFilter(sortBy: String?, orderBy: String) {
        sort = sortBy
        this.orderBy = orderBy

        val hasFocus = searchView?.hasFocus() ?: false

        if (!hasFocus) {
            fetchData(lastSearchedString)
        }
    }

    override fun onBackPressed() {

        val isSearchExpanded = searchMenuItem?.isActionViewExpanded ?: false

        if (isSearchExpanded) searchMenuItem?.collapseActionView() else super.onBackPressed()
    }

    private fun displayFilterBottomSheet() {

        val filterBottomSheet = FilterBottomSheet()

        val bundle = Bundle()
        bundle.putString("selectedSort", sort)
        bundle.putString("selectedOrderBy", orderBy)

        filterBottomSheet.arguments = bundle
        filterBottomSheet.show(supportFragmentManager, "sortBottomSheet")
    }

    private fun fetchData(query: String = "Gaurav") {

        if (!pb_home.isShown) {
            pb_home.visibility = View.VISIBLE
            rv_git_repo_home.isEnabled = false

            GitServer().buildApi(GitApiService::class.java).searchRepo(query, this.sort, this.orderBy)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        populateData(it.repos)
                        pb_home.visibility = View.GONE
                        lastSearchedString = query
                        isDataFetched = true
                    }, {

                        isDataFetched = false
                        pb_home.visibility = View.GONE
                        Snackbar.make(cl_home, "No Internet Connection", Snackbar.LENGTH_LONG).show()
                    })
        }
    }

    private val searchQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String): Boolean {

            if (isDataToBeFetched(lastSearchedString, query)) {
                fetchData(query)
            }
            searchView?.clearFocus()
            return true
        }

        override fun onQueryTextChange(query: String): Boolean {

            return false
        }

    }

    private fun isDataToBeFetched(str1: String, str2: String) =
            !TextUtils.equals(str1.trim().toLowerCase(), str2.trim().toLowerCase())

    private val actionExpandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            if (isDataToBeFetched(lastSearchedString.trim(), "Gaurav")) {
                fetchData()
            }
            return true
        }

    }

    private fun populateData(gitRepoList: List<GitRepo>) {

        if (rv_git_repo_home.adapter == null) {

            rv_git_repo_home.layoutManager = LinearLayoutManager(this)
            gitRepoRecyclerViewAdapter = GitRepoRecyclerViewAdapter(this, gitRepoList, true)
            rv_git_repo_home.adapter = gitRepoRecyclerViewAdapter
        } else {
            gitRepoRecyclerViewAdapter.gitRepoList = gitRepoList
            gitRepoRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

}
