package com.kylecorry.trail_sense.shared.navigation

import androidx.core.os.bundleOf
import com.kylecorry.trail_sense.main.MyNavController

class NavControllerAppNavigation(private val controller: MyNavController) : IAppNavigation {
    override fun navigate(route: String, params: List<Pair<String, Any?>>) {
        controller.navigate(route, bundleOf(pairs = params.toTypedArray()))
    }
}