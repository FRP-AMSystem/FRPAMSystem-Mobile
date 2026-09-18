package com.example.frpam_mobile.ui.equipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.frpam_mobile.data.model.AllocationEquipmentItem
import com.example.frpam_mobile.databinding.BottomSheetRequestEquipmentDetailBinding
import com.example.frpam_mobile.ui.assigned.barColor
import com.example.frpam_mobile.ui.assigned.statusColor
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RequestEquipmentDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetRequestEquipmentDetailBinding? = null
    private val binding get() = _binding!!

    private var item: AllocationEquipmentItem? = null
    private var showActions: Boolean = false
    private var onHandover: ((AllocationEquipmentItem) -> Unit)? = null
    private var onReturn: ((AllocationEquipmentItem) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetRequestEquipmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val equipment = item ?: return

        binding.tvDetailTitle.text = equipment.displayTitle()
        binding.tvDetailDateRange.text = equipment.displayDateRange()
        binding.tvDetailExperiment.text = equipment.experimentName?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailPhase.text = equipment.phaseName?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailAsset.text = equipment.assetCode?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailSerial.text = equipment.serialNumber?.takeIf { it.isNotBlank() } ?: "—"
        binding.tvDetailQuantity.text = equipment.quantity.toString()
        binding.tvDetailStatus.text = equipment.status.ifBlank { "—" }

        binding.viewColorBar.setBackgroundColor(requireContext().barColor(equipment.colorIndex()))
        binding.tvDetailStatus.setTextColor(requireContext().statusColor(equipment.status))

        val canHandover = showActions && equipment.canHandover()
        val canReturn = showActions && equipment.canReturn()
        binding.btnHandover.isVisible = canHandover
        binding.btnReturn.isVisible = canReturn
        binding.layoutActions.isVisible = canHandover || canReturn

        binding.btnHandover.setOnClickListener {
            onHandover?.invoke(equipment)
            dismiss()
        }
        binding.btnReturn.setOnClickListener {
            onReturn?.invoke(equipment)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            item: AllocationEquipmentItem,
            showActions: Boolean,
            onHandover: (AllocationEquipmentItem) -> Unit,
            onReturn: (AllocationEquipmentItem) -> Unit
        ): RequestEquipmentDetailBottomSheet {
            return RequestEquipmentDetailBottomSheet().apply {
                this.item = item
                this.showActions = showActions
                this.onHandover = onHandover
                this.onReturn = onReturn
            }
        }
    }
}
