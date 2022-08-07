package com.nodes.sunrise.components.utils

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller


class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_END
    }

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
    }
}