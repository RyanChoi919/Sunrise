package com.nodes.sunrise.ui

import androidx.fragment.app.Fragment
import com.nodes.sunrise.MainActivity
import com.nodes.sunrise.components.utils.DateUtil
import java.time.LocalDateTime

abstract class BaseFragment : Fragment() {
    val TAG = "LOG_TAG:" + this::class.java.simpleName

    val mainAct by lazy {
        activity as MainActivity
    }

    fun setToolbarTitleLarge(titleString: String) {
        mainAct.setTitleLarge(titleString)
    }

    fun setToolbarTitleSmallWithSubtitle(titleString: String, subtitleString: String?) {
        mainAct.setTitleSmallAndSubtitle(titleString, subtitleString)
    }

    fun setToolbarWithDateTime(dateTime: LocalDateTime) {
        setToolbarTitleSmallWithSubtitle(
            DateUtil.getLocalizedMonthAndDayOfMonthString(dateTime.toLocalDate()),
            DateUtil.getLocalizedTimeString(dateTime.toLocalTime())
        )
    }
}