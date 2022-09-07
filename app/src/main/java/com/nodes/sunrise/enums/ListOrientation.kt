package com.nodes.sunrise.enums

import androidx.recyclerview.widget.LinearLayoutManager

enum class ListOrientation(val linearLayoutMangerInt: Int) {
    HORIZONTAL(LinearLayoutManager.HORIZONTAL),
    VERTICAL(LinearLayoutManager.VERTICAL)
}