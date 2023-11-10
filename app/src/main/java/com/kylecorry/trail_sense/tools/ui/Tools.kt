package com.kylecorry.trail_sense.tools.ui

import android.content.Context
import android.hardware.Sensor
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.kylecorry.andromeda.sense.Sensors
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.main.Navigation
import com.kylecorry.trail_sense.shared.UserPreferences
import com.kylecorry.trail_sense.shared.sensors.SensorService

data class ToolGroup(val name: String, val tools: List<Tool>)
data class Tool(
    val name: String,
    @DrawableRes val icon: Int,
    val route: String,
    val description: String? = null
)

object Tools {
    fun getTools(context: Context): List<ToolGroup> {
        val hasLightMeter = Sensors.hasSensor(context, Sensor.TYPE_LIGHT)
        val hasPedometer = Sensors.hasSensor(context, Sensor.TYPE_STEP_COUNTER)
        val hasCompass = SensorService(context).hasCompass()
        val prefs = UserPreferences(context)
        val signaling = ToolGroup(
            context.getString(R.string.tool_category_signaling), listOf(
                Tool(
                    context.getString(R.string.flashlight_title),
                    R.drawable.flashlight,
                    Navigation.FLASHLIGHT,
                ),
                Tool(
                    context.getString(R.string.tool_whistle_title),
                    R.drawable.ic_tool_whistle,
                    Navigation.WHISTLE,
                )
            )
        )
        val distance = ToolGroup(
            context.getString(R.string.distance), listOfNotNull(
                Tool(
                    context.getString(R.string.tool_ruler_title),
                    R.drawable.ruler,
                    Navigation.RULER,
                ),
                if (hasPedometer) Tool(
                    context.getString(R.string.pedometer),
                    R.drawable.steps,
                    Navigation.PEDOMETER,
                ) else null,
                if (prefs.isCliffHeightEnabled) Tool(
                    context.getString(R.string.tool_cliff_height_title),
                    R.drawable.ic_tool_cliff_height,
                    Navigation.CLIFF_HEIGHT,
                    context.getString(R.string.experimental) + " - " + context.getString(R.string.tool_cliff_height_description)
                ) else null
            )
        )

        val location = ToolGroup(
            context.getString(R.string.location), listOfNotNull(
                Tool(
                    context.getString(R.string.photo_maps),
                    R.drawable.maps,
                    Navigation.MAP_LIST,
                    context.getString(R.string.photo_map_summary)
                ),
                Tool(
                    context.getString(R.string.paths),
                    R.drawable.ic_tool_backtrack,
                    Navigation.PATH_LIST
                ),
                Tool(
                    context.getString(R.string.tool_triangulate_title),
                    R.drawable.ic_tool_triangulate,
                    Navigation.TRIANGULATION,
                ),
            )
        )

        val angles = ToolGroup(
            context.getString(R.string.tool_category_angles), listOfNotNull(
                Tool(
                    context.getString(R.string.clinometer_title),
                    R.drawable.clinometer,
                    Navigation.CLINOMETER,
                    context.getString(R.string.tool_clinometer_summary)
                ),
                Tool(
                    context.getString(R.string.tool_bubble_level_title),
                    R.drawable.level,
                    Navigation.LEVEL,
                    context.getString(R.string.tool_bubble_level_summary)
                )
            )
        )

        val time = ToolGroup(
            context.getString(R.string.time), listOfNotNull(
                Tool(
                    context.getString(R.string.tool_clock_title),
                    R.drawable.ic_tool_clock,
                    Navigation.CLOCK,
                ),
                Tool(
                    context.getString(R.string.water_boil_timer),
                    R.drawable.ic_tool_boil,
                    Navigation.WATER_BOIL_TIMER,
                    context.getString(R.string.tool_boil_summary)
                ),
                Tool(
                    context.getString(R.string.tides),
                    R.drawable.ic_tide_table,
                    Navigation.TIDES,
                )
            )
        )

        val power = ToolGroup(
            context.getString(R.string.power), listOfNotNull(
                Tool(
                    context.getString(R.string.tool_battery_title),
                    R.drawable.ic_tool_battery,
                    Navigation.BATTERY,
                ),
                if (hasCompass) Tool(
                    context.getString(R.string.tool_solar_panel_title),
                    R.drawable.ic_tool_solar_panel,
                    Navigation.SOLAR_PANEL_ALIGNER,
                    context.getString(R.string.tool_solar_panel_summary)
                ) else null,
                if (hasLightMeter) Tool(
                    context.getString(R.string.tool_light_meter_title),
                    R.drawable.flashlight,
                    Navigation.LIGHT_METER,
                    context.getString(R.string.guide_light_meter_description)
                ) else null
            )
        )

        val weather = ToolGroup(
            context.getString(R.string.weather), listOfNotNull(
                Tool(
                    context.getString(R.string.tool_climate),
                    R.drawable.ic_temperature_range,
                    Navigation.CLIMATE,
                    context.getString(R.string.tool_climate_summary)
                ),
                Tool(
                    context.getString(R.string.tool_temperature_estimation_title),
                    R.drawable.thermometer,
                    Navigation.TEMPERATURE_ESTIMATION,
                    context.getString(R.string.tool_temperature_estimation_description)
                ),
                Tool(
                    context.getString(R.string.clouds),
                    R.drawable.ic_tool_clouds,
                    Navigation.CLOUDS,
                ),
                Tool(
                    context.getString(R.string.tool_lightning_title),
                    R.drawable.ic_torch_on,
                    Navigation.LIGHTNING_STRIKE_DISTANCE,
                    context.getString(R.string.tool_lightning_description)
                )
            )
        )

        val other = ToolGroup(
            context.getString(R.string.other), listOfNotNull(
                if (prefs.isAugmentedRealityEnabled && hasCompass) Tool(
                    context.getString(R.string.augmented_reality),
                    R.drawable.ic_camera,
                    Navigation.AUGMENTED_REALITY,
                    context.getString(R.string.augmented_reality_description)
                ) else null,
                Tool(
                    context.getString(R.string.convert),
                    R.drawable.ic_tool_distance_convert,
                    Navigation.CONVERT,
                ),
                Tool(
                    context.getString(R.string.packing_lists),
                    R.drawable.ic_tool_pack,
                    Navigation.PACK_LIST,
                ),
                if (hasCompass) Tool(
                    context.getString(R.string.tool_metal_detector_title),
                    R.drawable.ic_tool_metal_detector,
                    Navigation.METAL_DETECTOR,
                ) else null,
                Tool(
                    context.getString(R.string.tool_white_noise_title),
                    R.drawable.ic_tool_white_noise,
                    Navigation.WHITE_NOISE,
                    context.getString(R.string.tool_white_noise_summary)
                ),
                Tool(
                    context.getString(R.string.tool_notes_title),
                    R.drawable.ic_tool_notes,
                    Navigation.NOTES,
                ),
                Tool(
                    context.getString(R.string.qr_code_scanner),
                    R.drawable.ic_qr_code,
                    Navigation.QR_CODE_SCANNER,
                ),
                Tool(
                    context.getString(R.string.tool_user_guide_title),
                    R.drawable.ic_user_guide,
                    Navigation.GUIDE_LIST,
                    context.getString(R.string.tool_user_guide_summary)
                )
            )
        )

        return listOf(
            signaling,
            distance,
            location,
            angles,
            time,
            power,
            weather,
            other
        )

    }
}
