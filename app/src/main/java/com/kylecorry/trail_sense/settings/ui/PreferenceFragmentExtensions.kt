package com.kylecorry.trail_sense.settings.ui

import androidx.preference.Preference
import com.kylecorry.andromeda.core.tryOrNothing
import com.kylecorry.andromeda.fragments.AndromedaPreferenceFragment
import com.kylecorry.trail_sense.shared.requireMyNavigation

fun AndromedaPreferenceFragment.navigateOnClick(pref: Preference?, route: String) {
    pref?.setOnPreferenceClickListener {
        tryOrNothing {
            requireMyNavigation().navigate(route)
        }
        true
    }
}