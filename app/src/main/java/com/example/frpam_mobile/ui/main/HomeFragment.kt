package com.example.frpam_mobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frpam_mobile.R
import com.example.frpam_mobile.databinding.FragmentHomeBinding
import com.example.frpam_mobile.databinding.ItemWorkMenuBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRow(binding.rowIssue, R.string.menu_issue, R.drawable.ic_issue, R.drawable.bg_icon_green)
        bindRow(binding.rowRequestEquipment, R.string.menu_request_equipment, R.drawable.ic_equipment, R.drawable.bg_icon_blue)
        bindRow(binding.rowAssignedExperiment, R.string.menu_assigned_experiment, R.drawable.ic_experiment, R.drawable.bg_icon_purple)
        bindRow(binding.rowSchedule, R.string.menu_schedule, R.drawable.ic_schedule, R.drawable.bg_icon_orange)
    }

    private fun bindRow(row: ItemWorkMenuBinding, titleRes: Int, iconRes: Int, iconBgRes: Int) {
        row.tvTitle.setText(titleRes)
        row.ivIcon.setImageResource(iconRes)
        row.ivIcon.setBackgroundResource(iconBgRes)
        // API chưa có — gắn màn hình thật sau
        row.rowRoot.setOnClickListener {
            Toast.makeText(requireContext(), R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
