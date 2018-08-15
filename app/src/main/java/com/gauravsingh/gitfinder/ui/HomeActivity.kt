package com.gauravsingh.gitfinder.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.gauravsingh.gitfinder.R
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.network.GitServer
import com.gauravsingh.gitfinder.network.apiservice.GitApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

internal class HomeActivity : AppCompatActivity(), GitRepoRecyclerViewAdapter.OnItemClickListener {

    private lateinit var gitRepoRecyclerViewAdapter: GitRepoRecyclerViewAdapter

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar_home)

        fetchData("Gaurav")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_home, menu)

        searchMenuItem = menu?.findItem(R.id.action_search)
        searchView = searchMenuItem?.actionView as SearchView?
        searchView?.setOnQueryTextListener(searchQueryListener)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemCLick(gitRepo: GitRepo) {
        Toast.makeText(this@HomeActivity, "Click", Toast.LENGTH_LONG).show()

    }

    override fun onBackPressed() {

        val isSearchExpanded = searchMenuItem?.isActionViewExpanded ?: false


        if (isSearchExpanded) searchMenuItem?.collapseActionView() else super.onBackPressed()
    }

    private fun fetchData(query: String) {

        pb_home.visibility = View.VISIBLE
        rv_git_repo_home.isEnabled = false

        GitServer().buildApi(GitApiService::class.java).searchRepo(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("Home", "success")

                    populateData(it.repos)

                    rv_git_repo_home.isEnabled = true
                    pb_home.visibility = View.GONE
                }, {
                    Log.e("Home", it.localizedMessage)

                    rv_git_repo_home.isEnabled = true
                    pb_home.visibility = View.GONE
                    Snackbar.make(fab_home, "No Internet Connection", Snackbar.LENGTH_LONG)
                })
    }

    private val searchQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String): Boolean {

            fetchData(query)
            searchView?.clearFocus()

            Toast.makeText(this@HomeActivity, query, Toast.LENGTH_LONG).show()
            return true
        }

        override fun onQueryTextChange(query: String): Boolean {

            return false
        }

    }

    private fun populateData(gitRepoList: List<GitRepo>) {

        if (rv_git_repo_home.adapter == null) {

            rv_git_repo_home.layoutManager = LinearLayoutManager(this)
            gitRepoRecyclerViewAdapter = GitRepoRecyclerViewAdapter(this, gitRepoList)
            rv_git_repo_home.adapter = gitRepoRecyclerViewAdapter
        } else {
            gitRepoRecyclerViewAdapter.gitRepoList = gitRepoList
            gitRepoRecyclerViewAdapter.notifyDataSetChanged()
        }
    }
}
