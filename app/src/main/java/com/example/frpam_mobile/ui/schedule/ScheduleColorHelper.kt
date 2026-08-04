package com.example.frpam_mobile.ui.schedule

import com.example.frpam_mobile.R

object ScheduleColorHelper {

    private val chipBackgrounds = intArrayOf(
        R.drawable.bg_event_chip_teal,
        R.drawable.bg_event_chip_blue,
        R.drawable.bg_event_chip_purple,
        R.drawable.bg_event_chip_orange,
        R.drawable.bg_event_chip_green
    )

    private val eventColors = intArrayOf(
        R.color.cal_event_teal,
        R.color.cal_event_blue,
        R.color.cal_event_purple,
        R.color.cal_event_orange,
        R.color.cal_event_green
    )

    fun chipBackground(colorIndex: Int): Int {
        return chipBackgrounds[colorIndex % chipBackgrounds.size]
    }

    fun eventColor(colorIndex: Int): Int {
        return eventColors[colorIndex % eventColors.size]
    }
}
