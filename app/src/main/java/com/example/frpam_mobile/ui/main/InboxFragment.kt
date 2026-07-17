package com.example.frpam_mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frpam_mobile.data.model.NotificationItem
import com.example.frpam_mobile.data.repository.NotificationRepository
import com.example.frpam_mobile.data.repository.NotificationsResult
import com.example.frpam_mobile.databinding.FragmentInboxBinding
import kotlinx.coroutines.launch

class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    private val repository = NotificationRepository()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotificationAdapter(::onNotificationClick)
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = adapter

        loadNotifications()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadNotifications()
        }
    }

    private fun loadNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.tvEmpty.isVisible = false

            when (val result = repository.getMyNotifications()) {
                is NotificationsResult.Success -> {
                    adapter.submitList(result.items)
                    binding.tvEmpty.isVisible = result.items.isEmpty()
                }
                is NotificationsResult.Error -> {
                    binding.tvEmpty.isVisible = adapter.itemCount == 0
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }

            binding.progressBar.isVisible = false
        }
    }

    private fun onNotificationClick(item: NotificationItem) {
        if (item.isRead) return
        viewLifecycleOwner.lifecycleScope.launch {
            if (repository.markAsRead(item.notificationId)) {
                adapter.submitList(
                    adapter.currentList.map {
                        if (it.notificationId == item.notificationId) it.copy(isRead = true) else it
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
