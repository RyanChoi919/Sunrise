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

    private lateinit var toolbarBinding: ComponentToolbarBinding
    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

    /**
     * toolbar를 include하여 사용하는 레이아웃의 Toolbar를 설정하는 메소드
     *
     * @param toolbarBinding : include한 toolbarBinding 인스턴스
     * */
    fun setToolbar(toolbarBinding: ComponentToolbarBinding) {
        this.toolbarBinding = toolbarBinding
        this.toolbar = toolbarBinding.toolbar
    }

    /**
     * toolbar를 직접 구현하여 사용하는 레이아웃의 Toolbar를 설정하는 메소드
     *
     * @param toolbar : 레이아웃에서 구현한 Toolbar 인스턴스
     * */

    fun setToolbar(toolbar: Toolbar) {
        this.toolbar = toolbar
    }

    /**
     * 주어진 LocalDateTime 인스턴스를 기반으로 Toolbar에 날짜를 표시하는 메소드
     *
     * @param dateTime : Toolbar에 표시할 LocalDateTime 인스턴스
     *
    * */
    fun setToolbarTitleWithDateTime(dateTime: LocalDateTime) {
        toolbarBinding.toolbarTitleSmall.text =
            DateUtil.getLocalizedMonthAndDayOfMonthString(dateTime.toLocalDate())
        toolbarBinding.toolbarSubtitle.text =
            DateUtil.getLocalizedTimeString(dateTime.toLocalTime())
    }

    /**
     * ActionBar에 Menu를 생성하는 메소드
     *
     * */
    fun createMenu(
        toolbar: Toolbar,
        menuRes: Int?,
        onMenuItemClickListener: Toolbar.OnMenuItemClickListener
    ) {
        if (menuRes != null) {
            toolbar.inflateMenu(menuRes)
        }
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
    }

    /**
     * Toolbar의 Flag를 설정하는 메소드
     *
    * */
    fun setToolbarFlags(flags: Int) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
    }
}