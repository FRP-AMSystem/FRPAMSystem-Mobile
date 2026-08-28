package com.example.frpam_mobile.ui.assigned

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.frpam_mobile.R
import com.example.frpam_mobile.ui.schedule.ScheduleColorHelper

object AssignedExperimentStatusHelper {

    fun statusColorRes(status: String): Int = when (status.lowercase()) {
        "inuse", "in use", "active" -> R.color.cal_event_green
        "allocated", "reserved" -> R.color.cal_event_blue
        "completed" -> R.color.text_muted
        "cancelled" -> R.color.error_red
        else -> R.color.cal_event_purple
    }
}

object AssignedExperimentColorHelper {

    fun barColorRes(colorIndex: Int): Int = ScheduleColorHelper.eventColor(colorIndex)
}

fun android.content.Context.statusColor(status: String): Int =
    ContextCompat.getColor(this, AssignedExperimentStatusHelper.statusColorRes(status))

fun android.content.Context.barColor(colorIndex: Int): Int =
    ContextCompat.getColor(this, AssignedExperimentColorHelper.barColorRes(colorIndex))

fun View.setStatusChipBackground(color: Int) {
    val radius = 8f * resources.displayMetrics.density
    background = GradientDrawable().apply {
        cornerRadius = radius
        setColor(color)
    }
}
