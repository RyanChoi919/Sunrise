package com.nodes.sunrise.components.decorators

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nodes.sunrise.R
import com.nodes.sunrise.enums.ListOrientation

class ListMarginDecorator(resources: Resources, val orientation: ListOrientation) :
    RecyclerView.ItemDecoration() {

    private val spaceSize = resources.getDimensionPixelSize(R.dimen.list_item_margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (orientation == ListOrientation.VERTICAL) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> outRect.bottom += spaceSize
                parent.adapter!!.itemCount.minus(1) -> outRect.top += spaceSize
                else -> outRect.set(0, spaceSize, 0, spaceSize)
            }
        } else {
            when (parent.getChildAdapterPosition(view)) {
                0 -> outRect.right += spaceSize
                parent.adapter!!.itemCount.minus(1) -> outRect.left += spaceSize
                else -> outRect.set(spaceSize, 0, spaceSize, 0)
            }
        }
    }
}