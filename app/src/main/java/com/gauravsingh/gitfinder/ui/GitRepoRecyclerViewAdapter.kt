package com.gauravsingh.gitfinder.ui

import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gauravsingh.gitfinder.R
import com.gauravsingh.gitfinder.model.GitRepo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_git_repo.view.*

internal class GitRepoRecyclerViewAdapter(
        val listener: OnItemClickListener,
        var gitRepoList: List<GitRepo>, private val imgVisibility: Boolean)
    : RecyclerView.Adapter<GitRepoRecyclerViewAdapter.GitRepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {

        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_git_repo, parent, false)

        return GitRepoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gitRepoList.size
    }

    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        holder.bindData(gitRepoList[position])
    }

    inner class GitRepoViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(gitRepo: GitRepo) {

            if (imgVisibility) {

                itemView.iv_item_git_repo_img.visibility = View.VISIBLE
                Picasso.Builder(itemView.context).build().load(gitRepo.owner.avatarUrl)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(itemView.iv_item_git_repo_img)
            }

            itemView.tv_item_git_repo_name.text = gitRepo.name
            itemView.tv_item_git_repo_full_name.text = gitRepo.fullName
            itemView.tv_item_git_repo_watcher_count.text = gitRepo.watchersCount.toString()
            itemView.tv_item_git_repo_fork_count.text = gitRepo.forksCount.toString()
            itemView.tv_item_git_repo_stargazer_count.text = gitRepo.stargazersCount.toString()
        }

        override fun onClick(v: View?) {
            listener.onItemCLick(gitRepoList[adapterPosition])
        }

    }

    interface OnItemClickListener {
        fun onItemCLick(gitRepo: GitRepo)
    }
}