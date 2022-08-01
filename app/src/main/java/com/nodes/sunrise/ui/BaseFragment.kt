package com.nodes.sunrise.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    val TAG = "LOG_TAG:" + this::class.java.simpleName
}