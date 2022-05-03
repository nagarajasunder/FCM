package com.geekydroid.firebaselearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geekydroid.firebaselearn.R
import com.geekydroid.firebaselearn.data.User
import com.geekydroid.firebaselearn.utils.UserOnClickListener

class HomeFragmentAdapter(private val userOnClickListener: UserOnClickListener) :
    ListAdapter<User, HomeFragmentAdapter.ViewHolder>(UserItemCallback()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeFragmentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: HomeFragmentAdapter.ViewHolder, position: Int) {
        val currentUser = currentList[position]
        holder.userName.text = currentUser.emailAddress

        holder.itemView.setOnClickListener {
            userOnClickListener.onUserClick(currentUser)
        }
    }


    override fun getItemCount() = currentList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userName: TextView = itemView.findViewById(R.id.tv_user_name)
    }

    class UserItemCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }


        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }


}