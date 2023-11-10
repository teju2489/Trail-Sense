package com.kylecorry.trail_sense.shared.navigation

interface IAppNavigation {
    fun navigate(route: String, params: List<Pair<String, Any?>> = emptyList())
}