package com.example.frpam_mobile.ui.equipment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frpam_mobile.data.model.AllocationEquipmentItem
import com.example.frpam_mobile.databinding.ItemAssignedExperimentBinding
import com.example.frpam_mobile.ui.assigned.barColor
import com.example.frpam_mobile.ui.assigned.setStatusChipBackground
import com.example.frpam_mobile.ui.assigned.statusColor

class RequestEquipmentAdapter(
    private val onItemClick: (AllocationEquipmentItem) -> Unit
) : ListAdapter<AllocationEquipmentItem, RequestEquipmentAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<AllocationEquipmentItem>() {
        override fun areItemsTheSame(
            oldItem: AllocationEquipmentItem,
            newItem: AllocationEquipmentItem
        ) = oldItem.allocationEquipmentDetailId == newItem.allocationEquipmentDetailId

        override fun areContentsTheSame(
            oldItem: AllocationEquipmentItem,
            newItem: AllocationEquipmentItem
        ) = oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemAssignedExperimentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllocationEquipmentItem) {
            binding.tvTitle.text = item.displayTitle()
            binding.tvSubtitle.text = item.displaySubtitle()
            binding.tvDateRange.text = item.displayDateRange()
            binding.tvStatus.text = item.status.ifBlank { "—" }
            binding.viewColorBar.setBackgroundColor(binding.root.context.barColor(item.colorIndex()))
            binding.tvStatus.setStatusChipBackground(binding.root.context.statusColor(item.status))
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAssignedExperimentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
