package com.example.frpam_mobile.ui.assigned

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.frpam_mobile.data.model.AllocationHumanItem
import com.example.frpam_mobile.databinding.BottomSheetAssignedExperimentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AssignedExperimentDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAssignedExperimentDetailBinding? = null
    private val binding get() = _binding!!

    private var item: AllocationHumanItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAssignedExperimentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assignment = item ?: return

        binding.tvDetailTitle.text = assignment.displayTitle()
        binding.tvDetailDateRange.text = assignment.displayDateRange()
        binding.tvDetailPhase.text = assignment.phaseName?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailRole.text = assignment.requiredRoleName?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailSkill.text = assignment.requiredSkillName?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailWorkingHours.text = buildWorkingHoursText(assignment)
        binding.tvDetailStatus.text = assignment.status.ifBlank { "—" }
        binding.tvDetailNote.text = assignment.requirementNote?.takeIf { it.isNotBlank() } ?: "—"

        binding.viewColorBar.setBackgroundColor(requireContext().barColor(assignment.colorIndex()))
        binding.tvDetailStatus.setTextColor(requireContext().statusColor(assignment.status))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildWorkingHoursText(item: AllocationHumanItem): String {
        val total = if (item.workingHours > 0) {
            String.format("%.1f h total", item.workingHours)
        } else {
            null
        }
        val maxPerDay = if (item.maxWorkingHoursPerDay > 0) {
            String.format("max %.1f h/day", item.maxWorkingHoursPerDay)
        } else {
            null
        }
        return listOfNotNull(total, maxPerDay).joinToString(" • ").ifBlank { "—" }
    }

    companion object {
        fun newInstance(item: AllocationHumanItem): AssignedExperimentDetailBottomSheet {
            return AssignedExperimentDetailBottomSheet().apply {
                this.item = item
            }
        }
    }
}
