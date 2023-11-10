package com.kylecorry.trail_sense.main

import com.kylecorry.trail_sense.astronomy.ui.AstronomyFragment
import com.kylecorry.trail_sense.calibration.ui.CalibrateAltimeterFragment
import com.kylecorry.trail_sense.calibration.ui.CalibrateBarometerFragment
import com.kylecorry.trail_sense.calibration.ui.CalibrateCompassFragment
import com.kylecorry.trail_sense.calibration.ui.CalibrateGPSFragment
import com.kylecorry.trail_sense.calibration.ui.CalibrateOdometerFragment
import com.kylecorry.trail_sense.databinding.FragmentAugmentedRealityBinding
import com.kylecorry.trail_sense.databinding.FragmentToolCliffHeightBinding
import com.kylecorry.trail_sense.databinding.FragmentToolWhistleBinding
import com.kylecorry.trail_sense.diagnostics.DiagnosticsFragment
import com.kylecorry.trail_sense.diagnostics.SensorDetailsFragment
import com.kylecorry.trail_sense.licenses.LicenseFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.BeaconDetailsFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.PlaceBeaconFragment
import com.kylecorry.trail_sense.navigation.beacons.ui.list.BeaconListFragment
import com.kylecorry.trail_sense.navigation.paths.ui.PathOverviewFragment
import com.kylecorry.trail_sense.navigation.paths.ui.PathsFragment
import com.kylecorry.trail_sense.navigation.ui.NavigatorFragment
import com.kylecorry.trail_sense.settings.ui.AstronomySettingsFragment
import com.kylecorry.trail_sense.settings.ui.CellSignalSettingsFragment
import com.kylecorry.trail_sense.settings.ui.ClinometerSettingsFragment
import com.kylecorry.trail_sense.settings.ui.ErrorSettingsFragment
import com.kylecorry.trail_sense.settings.ui.ExperimentalSettingsFragment
import com.kylecorry.trail_sense.settings.ui.FlashlightSettingsFragment
import com.kylecorry.trail_sense.settings.ui.MapSettingsFragment
import com.kylecorry.trail_sense.settings.ui.NavigationSettingsFragment
import com.kylecorry.trail_sense.settings.ui.PowerSettingsFragment
import com.kylecorry.trail_sense.settings.ui.PrivacySettingsFragment
import com.kylecorry.trail_sense.settings.ui.SensorSettingsFragment
import com.kylecorry.trail_sense.settings.ui.SettingsFragment
import com.kylecorry.trail_sense.settings.ui.ThermometerSettingsFragment
import com.kylecorry.trail_sense.settings.ui.TideSettingsFragment
import com.kylecorry.trail_sense.settings.ui.UnitSettingsFragment
import com.kylecorry.trail_sense.settings.ui.WeatherSettingsFragment
import com.kylecorry.trail_sense.shared.sensors.thermometer.CalibratedThermometerWrapper
import com.kylecorry.trail_sense.tools.augmented_reality.AugmentedRealityFragment
import com.kylecorry.trail_sense.tools.battery.ui.FragmentToolBattery
import com.kylecorry.trail_sense.tools.cliffheight.ui.ToolCliffHeightFragment
import com.kylecorry.trail_sense.tools.clinometer.ui.ClinometerFragment
import com.kylecorry.trail_sense.tools.clock.ui.ToolClockFragment
import com.kylecorry.trail_sense.tools.convert.ui.FragmentToolConvert
import com.kylecorry.trail_sense.tools.flashlight.ui.FragmentToolFlashlight
import com.kylecorry.trail_sense.tools.flashlight.ui.FragmentToolScreenFlashlight
import com.kylecorry.trail_sense.tools.guide.ui.GuideFragment
import com.kylecorry.trail_sense.tools.guide.ui.GuideListFragment
import com.kylecorry.trail_sense.tools.level.ui.LevelFragment
import com.kylecorry.trail_sense.tools.light.ui.ToolLightFragment
import com.kylecorry.trail_sense.tools.lightning.ui.FragmentToolLightning
import com.kylecorry.trail_sense.tools.maps.ui.MapListFragment
import com.kylecorry.trail_sense.tools.maps.ui.MapsFragment
import com.kylecorry.trail_sense.tools.metaldetector.ui.FragmentToolMetalDetector
import com.kylecorry.trail_sense.tools.notes.ui.FragmentToolNotes
import com.kylecorry.trail_sense.tools.notes.ui.FragmentToolNotesCreate
import com.kylecorry.trail_sense.tools.packs.ui.CreateItemFragment
import com.kylecorry.trail_sense.tools.packs.ui.PackItemListFragment
import com.kylecorry.trail_sense.tools.packs.ui.PackListFragment
import com.kylecorry.trail_sense.tools.pedometer.ui.FragmentStrideLengthEstimation
import com.kylecorry.trail_sense.tools.pedometer.ui.FragmentToolPedometer
import com.kylecorry.trail_sense.tools.qr.ui.ScanQRFragment
import com.kylecorry.trail_sense.tools.ruler.ui.RulerFragment
import com.kylecorry.trail_sense.tools.solarpanel.ui.FragmentToolSolarPanel
import com.kylecorry.trail_sense.tools.tides.ui.CreateTideFragment
import com.kylecorry.trail_sense.tools.tides.ui.TideListFragment
import com.kylecorry.trail_sense.tools.tides.ui.TidesFragment
import com.kylecorry.trail_sense.tools.triangulate.ui.FragmentToolTriangulate
import com.kylecorry.trail_sense.tools.ui.ToolsFragment
import com.kylecorry.trail_sense.tools.waterpurification.ui.WaterPurificationFragment
import com.kylecorry.trail_sense.tools.whistle.ui.ToolWhistleFragment
import com.kylecorry.trail_sense.tools.whitenoise.ui.FragmentToolWhiteNoise
import com.kylecorry.trail_sense.weather.ui.ClimateFragment
import com.kylecorry.trail_sense.weather.ui.TemperatureEstimationFragment
import com.kylecorry.trail_sense.weather.ui.WeatherFragment
import com.kylecorry.trail_sense.weather.ui.clouds.CloudFragment
import com.kylecorry.trail_sense.weather.ui.clouds.CloudResultsFragment

object Navigation {

    // TODO: Use nested routes and select bottom nav item based on base route
    // TODO: Add animations from parent route to children routes

    const val NAVIGATION = "navigation"
    const val ASTRONOMY = "astronomy"
    const val WEATHER = "weather"
    const val TOOLS = "tools"
    const val SETTINGS = "settings"
    const val BEACON_LIST = "beacon_list"
    const val CREATE_BEACON = "create_beacon"
    const val BEACON_DETAIL = "beacon_detail"
    const val GUIDE = "guide"
    const val GUIDE_LIST = "guide_list"

    const val CREATE_NOTE = "create_note"
    const val NOTES = "notes"

    const val TIDE_LIST = "tide_list"
    const val CREATE_TIDE = "create_tide"
    const val TIDES = "tides"

    const val MAP = "map"
    const val MAP_LIST = "map_list"

    const val PACK_LIST = "pack_list"
    const val PACK = "pack"
    const val CREATE_PACK_ITEM = "create_pack_item"

    const val FLASHLIGHT = "flashlight"
    const val WHISTLE = "whistle"

    const val RULER = "ruler"
    const val PEDOMETER = "pedometer"
    const val CLIFF_HEIGHT = "cliff_height"

    const val PATH_LIST = "path_list"
    const val PATH = "path"

    const val TRIANGULATION = "triangulation"
    const val CLINOMETER = "clinometer"
    const val LEVEL = "level"

    const val CLOCK = "clock"
    const val WATER_BOIL_TIMER = "water_boil_timer"

    const val BATTERY = "battery"
    const val SOLAR_PANEL_ALIGNER = "solar_panel_aligner"
    const val LIGHT_METER = "light_meter"

    const val CLIMATE = "climate"
    const val TEMPERATURE_ESTIMATION = "temperature_estimation"
    const val CLOUDS = "clouds"
    const val CLOUD_PICKER = "cloud_picker"
    const val LIGHTNING_STRIKE_DISTANCE = "lightning_strike_distance"

    const val AUGMENTED_REALITY = "augmented_reality"
    const val CONVERT = "convert"
    const val METAL_DETECTOR = "metal_detector"
    const val WHITE_NOISE = "white_noise"
    const val QR_CODE_SCANNER = "qr_code_scanner"

    const val CALIBRATE_GPS = "calibrate_gps"
    const val CALIBRATE_ALTIMETER = "calibrate_altimeter"
    const val CALIBRATE_COMPASS = "calibrate_compass"
    const val CALIBRATE_THERMOMETER = "calibrate_thermometer"
    const val CALIBRATE_BAROMETER = "calibrate_barometer"
    const val CALIBRATE_PEDOMETER = "calibrate_pedometer"

    const val POWER_SETTINGS = "power_settings"
    const val WEATHER_SETTINGS = "weather_settings"
    const val CELL_SIGNAL_SETTINGS = "cell_signal_settings"
    const val UNIT_SETTINGS = "unit_settings"
    const val PRIVACY_SETTINGS = "privacy_settings"
    const val EXPERIMENTAL_SETTINGS = "experimental_settings"
    const val ERROR_SETTINGS = "error_settings"
    const val SENSOR_SETTINGS = "sensor_settings"
    const val NAVIGATION_SETTINGS = "navigation_settings"
    const val ASTRONOMY_SETTINGS = "astronomy_settings"
    const val FLASHLIGHT_SETTINGS = "flashlight_settings"
    const val MAP_SETTINGS = "map_settings"
    const val TIDE_SETTINGS = "tide_settings"
    const val CLINOMETER_SETTINGS = "clinometer_settings"

    const val LICENSES = "licenses"
    const val DIAGNOSTICS = "diagnostics"

    const val SCREEN_FLASHLIGHT = "screen_flashlight"

    const val SENSOR_DETAILS = "sensor_details"

    const val STRIDE_LENGTH_ESTIMATION = "stride_length_estimation"


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

        // Guide
        controller.addRoute<GuideFragment>(GUIDE)
        controller.addRoute<GuideListFragment>(GUIDE_LIST)

        // Notes
        controller.addRoute<FragmentToolNotesCreate>(CREATE_NOTE)
        controller.addRoute<FragmentToolNotes>(NOTES)

        // Tides
        controller.addRoute<TideListFragment>(TIDE_LIST)
        controller.addRoute<CreateTideFragment>(CREATE_TIDE)
        controller.addRoute<TidesFragment>(TIDES)

        // Maps
        controller.addRoute<MapsFragment>(MAP)
        controller.addRoute<MapListFragment>(MAP_LIST)

        // Packs
        controller.addRoute<PackListFragment>(PACK_LIST)
        controller.addRoute<PackItemListFragment>(PACK)
        controller.addRoute<CreateItemFragment>(CREATE_PACK_ITEM)

        // Flashlight
        controller.addRoute<FragmentToolFlashlight>(FLASHLIGHT)
        controller.addRoute<FragmentToolScreenFlashlight>(SCREEN_FLASHLIGHT)

        // Whistle
        controller.addRoute<ToolWhistleFragment>(WHISTLE)

        // Ruler
        controller.addRoute<RulerFragment>(RULER)

        // Pedometer
        controller.addRoute<FragmentToolPedometer>(PEDOMETER)

        // Cliff height
        controller.addRoute<ToolCliffHeightFragment>(CLIFF_HEIGHT)

        // Paths
        controller.addRoute<PathsFragment>(PATH_LIST)
        controller.addRoute<PathOverviewFragment>(PATH)

        // Triangulation
        controller.addRoute<FragmentToolTriangulate>(TRIANGULATION)

        // Clinometer
        controller.addRoute<ClinometerFragment>(CLINOMETER)

        // Level
        controller.addRoute<LevelFragment>(LEVEL)

        // Clock
        controller.addRoute<ToolClockFragment>(CLOCK)

        // Water boil timer
        controller.addRoute<WaterPurificationFragment>(WATER_BOIL_TIMER)

        // Battery
        controller.addRoute<FragmentToolBattery>(BATTERY)

        // Solar panel aligner
        controller.addRoute<FragmentToolSolarPanel>(SOLAR_PANEL_ALIGNER)

        // Light meter
        controller.addRoute<ToolLightFragment>(LIGHT_METER)

        // Climate
        controller.addRoute<ClimateFragment>(CLIMATE)

        // Temperature estimation
        controller.addRoute<TemperatureEstimationFragment>(TEMPERATURE_ESTIMATION)

        // Clouds
        controller.addRoute<CloudFragment>(CLOUDS)
        controller.addRoute<CloudResultsFragment>(CLOUD_PICKER)

        // Lightning strike distance
        controller.addRoute<FragmentToolLightning>(LIGHTNING_STRIKE_DISTANCE)

        // Augmented reality
        controller.addRoute<AugmentedRealityFragment>(AUGMENTED_REALITY)

        // Convert
        controller.addRoute<FragmentToolConvert>(CONVERT)

        // Metal detector
        controller.addRoute<FragmentToolMetalDetector>(METAL_DETECTOR)

        // White noise
        controller.addRoute<FragmentToolWhiteNoise>(WHITE_NOISE)

        // QR code scanner
        controller.addRoute<ScanQRFragment>(QR_CODE_SCANNER)

        // Calibration
        controller.addRoute<CalibrateGPSFragment>(CALIBRATE_GPS)
        controller.addRoute<CalibrateAltimeterFragment>(CALIBRATE_ALTIMETER)
        controller.addRoute<CalibrateCompassFragment>(CALIBRATE_COMPASS)
        controller.addRoute<ThermometerSettingsFragment>(CALIBRATE_THERMOMETER)
        controller.addRoute<CalibrateBarometerFragment>(CALIBRATE_BAROMETER)
        controller.addRoute<CalibrateOdometerFragment>(CALIBRATE_PEDOMETER)
        controller.addRoute<FragmentStrideLengthEstimation>(STRIDE_LENGTH_ESTIMATION)

        // Settings
        controller.addRoute<PowerSettingsFragment>(POWER_SETTINGS)
        controller.addRoute<WeatherSettingsFragment>(WEATHER_SETTINGS)
        controller.addRoute<CellSignalSettingsFragment>(CELL_SIGNAL_SETTINGS)
        controller.addRoute<UnitSettingsFragment>(UNIT_SETTINGS)
        controller.addRoute<PrivacySettingsFragment>(PRIVACY_SETTINGS)
        controller.addRoute<ExperimentalSettingsFragment>(EXPERIMENTAL_SETTINGS)
        controller.addRoute<ErrorSettingsFragment>(ERROR_SETTINGS)
        controller.addRoute<SensorSettingsFragment>(SENSOR_SETTINGS)
        controller.addRoute<NavigationSettingsFragment>(NAVIGATION_SETTINGS)
        controller.addRoute<AstronomySettingsFragment>(ASTRONOMY_SETTINGS)
        controller.addRoute<FlashlightSettingsFragment>(FLASHLIGHT_SETTINGS)
        controller.addRoute<MapSettingsFragment>(MAP_SETTINGS)
        controller.addRoute<TideSettingsFragment>(TIDE_SETTINGS)
        controller.addRoute<ClinometerSettingsFragment>(CLINOMETER_SETTINGS)

        // Licenses
        controller.addRoute<LicenseFragment>(LICENSES)

        // Diagnostics
        controller.addRoute<DiagnosticsFragment>(DIAGNOSTICS)
        controller.addRoute<SensorDetailsFragment>(SENSOR_DETAILS)
    }

}