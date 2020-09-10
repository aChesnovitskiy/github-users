package com.achesnovitskiy.githubusers.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.*

class UsersAdapter(
    private val onItemClickListener: (UserItem) -> Unit,
    private val onReachingLastItemListener: () -> Unit
) :
    ListAdapter<UserItem, RepoViewHolder>(
        UsersDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)

        if (position == itemCount - 1) {
            onReachingLastItemListener.invoke()
        }
    }
}

class UsersDiffCallback : DiffUtil.ItemCallback<UserItem>() {

    override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean =
        oldItem == newItem
}

class RepoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(userItem: UserItem, onItemClickListener: (UserItem) -> Unit) {
        Picasso.get()
            .load(userItem.avatarUrl)
            .error(R.drawable.ic_error_black_48)
            .resize(200, 200)
            .centerCrop()
            .into(user_item_avatar_image_view)

        user_item_name_text_view.text = userItem.name

        itemView.setOnClickListener { onItemClickListener(userItem) }
    }
}