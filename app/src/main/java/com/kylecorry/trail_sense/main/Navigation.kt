package com.kylecorry.trail_sense.main

import com.kylecorry.trail_sense.astronomy.ui.AstronomyFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.BeaconDetailsFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.PlaceBeaconFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.list.BeaconListFragment
import com.kylecorry.trail_sense.navigation.ui.NavigatorFragment
import com.kylecorry.trail_sense.settings.ui.SettingsFragment
import com.kylecorry.trail_sense.tools.ui.ToolsFragment
import com.kylecorry.trail_sense.weather.ui.WeatherFragment

object Navigation {

    const val NAVIGATION = "navigation"
    const val ASTRONOMY = "astronomy"
    const val WEATHER = "weather"
    const val TOOLS = "tools"
    const val SETTINGS = "settings"
    const val BEACON_LIST = "beacon_list"
    const val CREATE_BEACON = "create_beacon"
    const val BEACON_DETAIL = "beacon_detail"

    fun initialize(controller: MyNavController){
        controller.addRoute<NavigatorFragment>(NAVIGATION)
        controller.addRoute<AstronomyFragment>(ASTRONOMY)
        controller.addRoute<WeatherFragment>(WEATHER)
        controller.addRoute<ToolsFragment>(TOOLS)
        controller.addRoute<SettingsFragment>(SETTINGS)

        // Beacons
        controller.addRoute<BeaconListFragment>(BEACON_LIST)
        controller.addRoute<PlaceBeaconFragment>(CREATE_BEACON)
        controller.addRoute<BeaconDetailsFragment>(BEACON_DETAIL)
    }

}