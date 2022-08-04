package com.nodes.sunrise.components.listeners

import android.view.View
import com.nodes.sunrise.db.entity.Entry

interface OnEntryClickListener {
    fun onClick(entry: Entry) {}
    fun onClick(view: View, pos: Int, entry: Entry) {}
}