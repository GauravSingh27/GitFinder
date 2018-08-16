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
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.gauravsingh.gitfinder.R
import com.gauravsingh.gitfinder.model.Contributor
import com.gauravsingh.gitfinder.model.GitRepo
import com.gauravsingh.gitfinder.network.GitServer
import com.gauravsingh.gitfinder.network.apiservice.GitApiService
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contributor.*
import kotlinx.android.synthetic.main.content_contributor.*
import kotlinx.android.synthetic.main.content_git_repo.*

internal class ContributorActivity : AppCompatActivity(), GitRepoRecyclerViewAdapter.OnItemClickListener {


    private lateinit var gitRepoRecyclerViewAdapter: GitRepoRecyclerViewAdapter
    private var isDataFetched = false
    private val networkChangeReceiver: NetworkChangeReceiver by lazy { NetworkChangeReceiver() }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val connMgr: ConnectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val isConnected = connMgr.activeNetworkInfo?.isConnectedOrConnecting == true

            if (!isConnected && !isDataFetched) {
                Snackbar.make(cl_contributor, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            } else if (!isDataFetched) {

                fetchContributorData()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contributor)

        setSupportActionBar(toolbar_contributor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar_contributor.setNavigationOnClickListener { onBackPressed() }
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

    private fun launchContributorProfile(htmlUrl: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", htmlUrl)
        startActivity(intent)
    }

    override fun onItemCLick(gitRepo: GitRepo) {
        val intent = Intent(this, GitRepoActivity::class.java)
        intent.putExtra("gitRepo", gitRepo)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchContributorData() {

        val contributor = intent.getParcelableExtra<Contributor>("contributor")

        toolbar_contributor.title = contributor.login
        toolbar_layout.title = contributor.login
        supportActionBar?.title = contributor.login

        Picasso.Builder(applicationContext).build().load(contributor.avatarUrl)
                .into(iv_contributor_img)

        tv_contributor_url.setOnClickListener { launchContributorProfile(contributor.htmlUrl) }

        pb_contributor.visibility = View.VISIBLE

        val gitApiService = GitServer().buildApi(GitApiService::class.java)

        gitApiService.fetchUserRepos(contributor.login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("GitRepoActivity", "success")
                    pb_contributor.visibility = View.GONE
                    populateData(it)
                    isDataFetched = true

                }, {
                    Log.e("GitRepofetchUser", it.localizedMessage)

                    pb_contributor.visibility = View.GONE
                    isDataFetched = false
                    Snackbar.make(cl_contributor, "No Internet Connection", Snackbar.LENGTH_LONG).show()

                })

        gitApiService.fetchUser(contributor.login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("GitRepoActivity", "success")
                    isDataFetched = true

                    populateContributorData(it)

                }, {
                    Log.e("GitRepofetchUser", it.localizedMessage)

                    pb_contributor.visibility = View.GONE
                    isDataFetched = false
                    Snackbar.make(cl_contributor, "No Internet Connection", Snackbar.LENGTH_LONG).show()

                })
    }


    private fun populateContributorData(contributor: Contributor) {

        tv_contributor_real_name.text = contributor.name ?: contributor.login
        tv_contributor_url.text = contributor.htmlUrl
        tv_contributor_following.text = getString(R.string.following, contributor.following.toString())
        tv_contributor_followers.text = getString(R.string.followers, contributor.followers.toString())

    }

    private fun populateData(gitRepoList: List<GitRepo>) {

        tv_contributor_repo.text = getString(R.string.repositories, gitRepoList.size.toString())
        if (rv_contributor_repositories.adapter == null) {

            rv_contributor_repositories.layoutManager = object : LinearLayoutManager(this) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            gitRepoRecyclerViewAdapter = GitRepoRecyclerViewAdapter(this, gitRepoList, false)
            rv_contributor_repositories.adapter = gitRepoRecyclerViewAdapter
        } else {
            gitRepoRecyclerViewAdapter.gitRepoList = gitRepoList
            gitRepoRecyclerViewAdapter.notifyDataSetChanged()
        }
    }
}
