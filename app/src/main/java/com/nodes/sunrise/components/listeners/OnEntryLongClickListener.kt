package com.nodes.sunrise.components.listeners

import android.view.View
import com.nodes.sunrise.db.entity.Entry

interface OnEntryLongClickListener {
    fun onItemLongClick(view: View, pos: Int, entry: Entry)
}