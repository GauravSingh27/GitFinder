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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_git_repo.*
import kotlinx.android.synthetic.main.content_git_repo.*

internal class GitRepoActivity
    : AppCompatActivity(), ContributorRecyclerViewAdapter.OnItemClickListener {

    private lateinit var contributorRecyclerViewAdapter: ContributorRecyclerViewAdapter
    private var isDataFetched = false
    private val networkChangeReceiver: NetworkChangeReceiver by lazy { NetworkChangeReceiver() }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val connMgr: ConnectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val isConnected = connMgr.activeNetworkInfo?.isConnectedOrConnecting == true

            if (!isConnected && !isDataFetched) {
                Snackbar.make(cl_git_repo, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            } else if (!isDataFetched) {

                fetchData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_repo)
        setSupportActionBar(toolbar_git_repo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemCLick(contributor: Contributor) {

        val intent = Intent(this, ContributorActivity::class.java)
        intent.putExtra("contributor", contributor)
        startActivity(intent)
    }

    private fun launchRepoProfile(htmlUrl: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", htmlUrl)
        startActivity(intent)
    }

    private fun fetchData() {

        pb_git_repo.visibility = View.VISIBLE

        val gitRepo = intent.getParcelableExtra<GitRepo>("gitRepo")
        tv_git_repo_url.setOnClickListener { launchRepoProfile(gitRepo.htmlUrl) }

        GitServer().buildApi(GitApiService::class.java).fetchRepoContributors(gitRepo.owner.login, gitRepo.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("GitRepoActivity", "success")

                    populateData(it, gitRepo)
                    pb_git_repo.visibility = View.GONE
                    isDataFetched = true
                }, {
                    Log.e("GitRepoActivity", it.localizedMessage)

                    pb_git_repo.visibility = View.GONE
                    isDataFetched = false
                    Snackbar.make(cl_git_repo, "No Internet Connection", Snackbar.LENGTH_LONG).show()

                })
    }

    private fun populateData(contributors: List<Contributor>, gitRepo: GitRepo) {

        tv_git_repo_name.text = gitRepo.name
        tv_git_repo_full_name.text = gitRepo.fullName
        tv_git_repo_watcher_count.text = gitRepo.watchersCount.toString()
        tv_git_repo_fork_count.text = gitRepo.forksCount.toString()
        tv_git_repo_stargazer_count.text = gitRepo.stargazersCount.toString()
        tv_git_repo_default_branch.text = getString(R.string.default_branch, gitRepo.defaultBranch)
        tv_git_repo_primary_language.text = getString(R.string.language, gitRepo.language)
        tv_git_repo_url.text = gitRepo.htmlUrl
        tv_contributors.text = getString(R.string.contributors, contributors.size.toString())
        tv_git_repo_description.text = getString(R.string.description, gitRepo.description)


        if (rv_git_repo_contributors.adapter == null) {

            rv_git_repo_contributors.layoutManager = object : LinearLayoutManager(this) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            contributorRecyclerViewAdapter = ContributorRecyclerViewAdapter(this, contributors)
            rv_git_repo_contributors.adapter = contributorRecyclerViewAdapter
        } else {
            contributorRecyclerViewAdapter.contributorList = contributors
            contributorRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

}
