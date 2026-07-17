package com.example.frpam_mobile.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frpam_mobile.data.model.NotificationItem
import com.example.frpam_mobile.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val onItemClick: (NotificationItem) -> Unit
) : ListAdapter<NotificationItem, NotificationAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<NotificationItem>() {
        override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem) =
            oldItem.notificationId == newItem.notificationId

        override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem) =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            binding.tvTitle.text = item.title
            binding.tvMessage.text = item.message
            binding.tvTime.text = item.createdAt.replace('T', ' ').take(16)
            binding.viewUnreadDot.visibility = if (item.isRead) View.INVISIBLE else View.VISIBLE
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
