package com.nodes.sunrise.components.listeners

import android.view.View

interface OnEntityClickListener<T> {
    fun onClick() {}
    fun onClick(entity: T) {}
    fun onClick(pos: Int) {}
    fun onClick(view: View, pos: Int, entity: T) {}
}