package com.example.frpam_mobile.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frpam_mobile.R
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.databinding.FragmentHomeBinding
import com.example.frpam_mobile.databinding.ItemWorkMenuBinding
import com.example.frpam_mobile.ui.schedule.ScheduleActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

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

        sessionManager = SessionManager(requireContext())

        bindRow(binding.rowIssue, R.string.menu_issue, R.drawable.ic_issue, R.drawable.bg_icon_green)
        bindRow(binding.rowRequestEquipment, R.string.menu_request_equipment, R.drawable.ic_equipment, R.drawable.bg_icon_blue)
        bindRow(binding.rowAssignedExperiment, R.string.menu_assigned_experiment, R.drawable.ic_experiment, R.drawable.bg_icon_purple)
        bindRow(
            row = binding.rowSchedule,
            titleRes = R.string.menu_schedule,
            iconRes = R.drawable.ic_schedule,
            iconBgRes = R.drawable.bg_icon_orange,
            onClick = {
                if (sessionManager.canAccessSchedule()) {
                    startActivity(Intent(requireContext(), ScheduleActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), R.string.schedule_not_allowed, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun bindRow(
        row: ItemWorkMenuBinding,
        titleRes: Int,
        iconRes: Int,
        iconBgRes: Int,
        onClick: (() -> Unit)? = null
    ) {
        row.tvTitle.setText(titleRes)
        row.ivIcon.setImageResource(iconRes)
        row.ivIcon.setBackgroundResource(iconBgRes)
        row.rowRoot.setOnClickListener {
            if (onClick != null) {
                onClick()
            } else {
                Toast.makeText(requireContext(), R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
