package com.nodes.sunrise.components.helpers

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.challenge.select.ChallengeSelectFragment
import com.nodes.sunrise.ui.entry.read.EntryReadFragment
import com.nodes.sunrise.ui.entry.write.EntryWriteFragment

open class NavigationHelper(val fragment: Fragment) {

    fun navigateToEntryReadFragment(entry: Entry) {
        val bundle: Bundle = bundleOf(EntryReadFragment.KEY_ENTRY to entry)
        fragment.findNavController().navigate(R.id.nav_entry_read, bundle)
    }

    fun navigateToEntryWriteFragmentToCreate() {
        fragment.findNavController().navigate(R.id.EntryWriteFragment)
    }

    fun navigateToEntryWriteFragmentToModify(entry: Entry) {
        val bundle = bundleOf(EntryWriteFragment.KEY_ENTRY to entry)
        fragment.findNavController().navigate(R.id.EntryWriteFragment, bundle)
    }

    fun navigateToChallengeSelectFragment() {
        fragment.findNavController().navigate(R.id.nav_challenge_select)
    }

    fun navigateToChallengeSelectFragment(challenge: Challenge?) {
        val bundle = bundleOf(ChallengeSelectFragment.KEY_CHALLENGE to challenge)
        fragment.findNavController().navigate(R.id.nav_challenge_select, bundle)
    }
}