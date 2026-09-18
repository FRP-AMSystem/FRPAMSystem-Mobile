package com.example.frpam_mobile.ui.equipment

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frpam_mobile.R
import com.example.frpam_mobile.data.model.AllocationEquipmentItem
import com.example.frpam_mobile.data.model.ReturnMineRequest
import com.example.frpam_mobile.data.prefs.SessionManager
import com.example.frpam_mobile.data.repository.RequestEquipmentActionResult
import com.example.frpam_mobile.data.repository.RequestEquipmentRepository
import com.example.frpam_mobile.data.repository.RequestEquipmentResult
import com.example.frpam_mobile.databinding.ActivityRequestEquipmentBinding
import com.example.frpam_mobile.databinding.DialogReturnEquipmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RequestEquipmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestEquipmentBinding
    private lateinit var adapter: RequestEquipmentAdapter
    private lateinit var sessionManager: SessionManager

    private val repository = RequestEquipmentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestEquipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        if (!sessionManager.canAccessRequestEquipment()) {
            Toast.makeText(this, R.string.request_equipment_not_allowed, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        adapter = RequestEquipmentAdapter(::onEquipmentClick)
        binding.rvEquipment.layoutManager = LinearLayoutManager(this)
        binding.rvEquipment.adapter = adapter
        binding.btnBack.setOnClickListener { finish() }

        loadEquipment()
    }

    private fun loadEquipment() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.tvEmpty.isVisible = false

            when (val result = repository.getMyEquipment()) {
                is RequestEquipmentResult.Success -> {
                    adapter.submitList(result.items)
                    binding.tvEmpty.isVisible = result.items.isEmpty()
                }
                is RequestEquipmentResult.Error -> {
                    binding.tvEmpty.isVisible = adapter.itemCount == 0
                    Toast.makeText(this@RequestEquipmentActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }

            binding.progressBar.isVisible = false
        }
    }

    private fun onEquipmentClick(item: AllocationEquipmentItem) {
        lifecycleScope.launch {
            val detail = repository.getEquipmentDetail(item.allocationEquipmentDetailId) ?: item
            RequestEquipmentDetailBottomSheet.newInstance(
                item = detail,
                showActions = sessionManager.canAccessRequestEquipment(),
                onHandover = ::confirmHandover,
                onReturn = ::showReturnDialog
            ).show(supportFragmentManager, "request_equipment_detail")
        }
    }

    private fun confirmHandover(item: AllocationEquipmentItem) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.request_equipment_handover)
            .setMessage(R.string.request_equipment_handover_confirm)
            .setNegativeButton(R.string.request_equipment_cancel, null)
            .setPositiveButton(R.string.request_equipment_handover) { _, _ ->
                submitHandover(item.allocationEquipmentDetailId)
            }
            .show()
    }

    private fun submitHandover(id: Int) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            when (val result = repository.handover(id)) {
                is RequestEquipmentActionResult.Success -> {
                    Toast.makeText(
                        this@RequestEquipmentActivity,
                        R.string.request_equipment_handover_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    loadEquipment()
                }
                is RequestEquipmentActionResult.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this@RequestEquipmentActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showReturnDialog(item: AllocationEquipmentItem) {
        val dialogBinding = DialogReturnEquipmentBinding.inflate(LayoutInflater.from(this))
        dialogBinding.cbDamaged.setOnCheckedChangeListener { _, checked ->
            dialogBinding.layoutDamageDescription.isVisible = checked
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.request_equipment_return_title)
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.request_equipment_cancel, null)
            .setPositiveButton(R.string.request_equipment_submit, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val conditionAfter = dialogBinding.etConditionAfter.text?.toString()?.trim().orEmpty()
                val isDamaged = dialogBinding.cbDamaged.isChecked
                val damageDescription = dialogBinding.etDamageDescription.text?.toString()?.trim()
                val note = dialogBinding.etNote.text?.toString()?.trim()

                if (conditionAfter.isBlank()) {
                    Toast.makeText(this, R.string.request_equipment_condition_required, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (isDamaged && damageDescription.isNullOrBlank()) {
                    Toast.makeText(this, R.string.request_equipment_damage_required, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog.dismiss()
                submitReturn(
                    item.allocationEquipmentDetailId,
                    ReturnMineRequest(
                        conditionAfter = conditionAfter,
                        isDamaged = isDamaged,
                        damageDescription = if (isDamaged) damageDescription else null,
                        note = note?.takeIf { it.isNotBlank() }
                    )
                )
            }
        }
        dialog.show()
    }

    private fun submitReturn(id: Int, request: ReturnMineRequest) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            when (val result = repository.returnEquipment(id, request)) {
                is RequestEquipmentActionResult.Success -> {
                    Toast.makeText(
                        this@RequestEquipmentActivity,
                        R.string.request_equipment_return_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    loadEquipment()
                }
                is RequestEquipmentActionResult.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this@RequestEquipmentActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
