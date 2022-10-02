package com.nodes.sunrise.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.nodes.sunrise.components.utils.DateUtil
import com.nodes.sunrise.databinding.ComponentToolbarBinding
import java.time.LocalDateTime

abstract class BaseFragment : Fragment() {
    val TAG = "LOG_TAG:" + this::class.java.simpleName

    private lateinit var toolbarBinding: ComponentToolbarBinding
    private lateinit var toolbar: Toolbar

    fun setToolbarBinding(toolbarBinding: ComponentToolbarBinding) {
        this.toolbarBinding = toolbarBinding
        setToolbar(toolbarBinding.toolbar)
    }

    fun setToolbar(toolbar: Toolbar) {
        this.toolbar = toolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(
            toolbar, navController, appBarConfiguration
        )
    }

    fun setToolbarTitleWithDateTime(dateTime: LocalDateTime) {
        toolbarBinding.toolbarTitleSmall.text =
            DateUtil.getLocalizedMonthAndDayOfMonthString(dateTime.toLocalDate())
        toolbarBinding.toolbarSubtitle.text =
            DateUtil.getLocalizedTimeString(dateTime.toLocalTime())
    }

    fun createMenu(
        toolbar: Toolbar,
        menuRes: Int,
        onMenuItemClickListener: Toolbar.OnMenuItemClickListener
    ) {
        toolbar.inflateMenu(menuRes)
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
    }

    fun setToolbarFlags(flags: Int) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
    }
}