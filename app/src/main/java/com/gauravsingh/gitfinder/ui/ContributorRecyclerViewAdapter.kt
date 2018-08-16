package com.gauravsingh.gitfinder.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gauravsingh.gitfinder.R
import com.gauravsingh.gitfinder.model.Contributor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_contributor.view.*

internal class ContributorRecyclerViewAdapter(
        val listener: OnItemClickListener,
        var contributorList: List<Contributor>)
    : RecyclerView.Adapter<ContributorRecyclerViewAdapter.ContributorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorViewHolder {

        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_contributor, parent, false)

        return ContributorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contributorList.size
    }

    override fun onBindViewHolder(holder: ContributorViewHolder, position: Int) {
        holder.bindData(contributorList[position])
    }

    inner class ContributorViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(contributor: Contributor) {

            Picasso.Builder(itemView.context).build().load(contributor.avatarUrl)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(itemView.iv_item_contributor_img)

            itemView.tv_item_contributor_name.text = contributor.login
            itemView.tv_item_contributor_url.text = contributor.htmlUrl
            itemView.tv_item_contributor_contribution_count.text = itemView.context.getString(R.string.contributions, contributor.contributions.toString())


        }

        override fun onClick(v: View?) {
            listener.onItemCLick(contributorList[adapterPosition])
        }

    }

    interface OnItemClickListener {
        fun onItemCLick(contributor: Contributor)
    }
}