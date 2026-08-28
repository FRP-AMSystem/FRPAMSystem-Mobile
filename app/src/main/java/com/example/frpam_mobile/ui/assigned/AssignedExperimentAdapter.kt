package com.example.frpam_mobile.ui.assigned

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frpam_mobile.data.model.AllocationHumanItem
import com.example.frpam_mobile.databinding.ItemAssignedExperimentBinding

class AssignedExperimentAdapter(
    private val onItemClick: (AllocationHumanItem) -> Unit
) : ListAdapter<AllocationHumanItem, AssignedExperimentAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<AllocationHumanItem>() {
        override fun areItemsTheSame(oldItem: AllocationHumanItem, newItem: AllocationHumanItem) =
            oldItem.allocationHumanDetailId == newItem.allocationHumanDetailId

        override fun areContentsTheSame(oldItem: AllocationHumanItem, newItem: AllocationHumanItem) =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemAssignedExperimentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AllocationHumanItem) {
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
