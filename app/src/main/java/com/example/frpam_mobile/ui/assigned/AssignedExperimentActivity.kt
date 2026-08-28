package com.example.frpam_mobile.ui.assigned

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frpam_mobile.data.model.AllocationHumanItem
import com.example.frpam_mobile.data.repository.AssignedExperimentRepository
import com.example.frpam_mobile.data.repository.AssignedExperimentsResult
import com.example.frpam_mobile.databinding.ActivityAssignedExperimentBinding
import kotlinx.coroutines.launch

class AssignedExperimentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignedExperimentBinding
    private lateinit var adapter: AssignedExperimentAdapter

    private val repository = AssignedExperimentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignedExperimentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AssignedExperimentAdapter(::onAssignmentClick)
        binding.rvAssignments.layoutManager = LinearLayoutManager(this)
        binding.rvAssignments.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        loadAssignments()
    }

    private fun loadAssignments() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.tvEmpty.isVisible = false

            when (val result = repository.getMyAssignments()) {
                is AssignedExperimentsResult.Success -> {
                    adapter.submitList(result.items)
                    binding.tvEmpty.isVisible = result.items.isEmpty()
                }
                is AssignedExperimentsResult.Error -> {
                    binding.tvEmpty.isVisible = adapter.itemCount == 0
                    Toast.makeText(this@AssignedExperimentActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }

            binding.progressBar.isVisible = false
        }
    }

    private fun onAssignmentClick(item: AllocationHumanItem) {
        lifecycleScope.launch {
            val detail = repository.getAssignmentDetail(item.allocationHumanDetailId) ?: item
            AssignedExperimentDetailBottomSheet.newInstance(detail)
                .show(supportFragmentManager, "assigned_experiment_detail")
        }
    }
}
