package com.nodes.sunrise.components.listeners

import android.view.View

interface OnEntityLongClickListener<T> {
    fun onItemLongClick(view: View, pos: Int, entity: T)
}