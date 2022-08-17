package com.nodes.sunrise.components.helpers

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.ChallengeResult
import com.nodes.sunrise.ui.challenge.check.ChallengeCheckFragment
import com.nodes.sunrise.ui.challenge.select.ChallengeSelectFragment
import com.nodes.sunrise.ui.entry.read.EntryReadFragment
import com.nodes.sunrise.ui.entry.write.EntryWriteFragment

open class NavigationHelper(val navController: NavController) {

    fun navigateToEntryReadFragment(entry: Entry) {
        val bundle: Bundle = bundleOf(EntryReadFragment.KEY_ENTRY to entry)
        navController.navigate(R.id.nav_entry_read, bundle)
    }

    fun navigateToEntryWriteFragmentToCreate() {
        navController.navigate(R.id.EntryWriteFragment)
    }

    fun navigateToEntryWriteFragmentToModify(entry: Entry) {
        val bundle = bundleOf(EntryWriteFragment.KEY_ENTRY to entry)
        navController.navigate(R.id.EntryWriteFragment, bundle)
    }

    fun navigateToChallengeSelectFragment() {
        navController.navigate(R.id.nav_challenge_select)
    }

    fun navigateToChallengeSelectFragment(challenge: Challenge?) {
        val bundle = bundleOf(ChallengeSelectFragment.KEY_CHALLENGE to challenge)
        navController.navigate(R.id.nav_challenge_select, bundle)
    }

    fun navigateToChallengeCheckFragment(challenge: Challenge) {
        val bundle = bundleOf(ChallengeCheckFragment.KEY_CHALLENGE to challenge)
        navController.navigate(R.id.nav_challenge_check, bundle)
    }
}