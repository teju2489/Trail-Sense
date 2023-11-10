package com.kylecorry.trail_sense.settings.ui

import android.hardware.Sensor
import android.os.Bundle
import android.text.InputType
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.fragments.AndromedaPreferenceFragment
import com.kylecorry.andromeda.sense.Sensors
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.main.Navigation

class SensorSettingsFragment : AndromedaPreferenceFragment() {

    private val navigationMap = mapOf(
        R.string.pref_sensor_details to Navigation.SENSOR_DETAILS,
        R.string.pref_cell_signal_settings to Navigation.CELL_SIGNAL_SETTINGS,
        R.string.pref_compass_sensor to Navigation.CALIBRATE_COMPASS,
        R.string.pref_altimeter_calibration to Navigation.CALIBRATE_ALTIMETER,
        R.string.pref_gps_calibration to Navigation.CALIBRATE_GPS,
        R.string.pref_barometer_calibration to Navigation.CALIBRATE_BAROMETER,
        R.string.pref_temperature_settings to Navigation.CALIBRATE_THERMOMETER,
        R.string.pref_odometer_calibration to Navigation.CALIBRATE_PEDOMETER,
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.sensor_preferences, rootKey)

        setIconColor(Resources.androidTextColorSecondary(requireContext()))

        for (nav in navigationMap) {
            navigateOnClick(preference(nav.key), nav.value)
        }

        preference(R.string.pref_barometer_calibration)?.isVisible = Sensors.hasBarometer(requireContext())
        preference(R.string.pref_odometer_calibration)?.isVisible = Sensors.hasSensor(requireContext(), Sensor.TYPE_STEP_COUNTER)

        editText(R.string.pref_ruler_calibration)
            ?.setOnBindEditTextListener { editText ->
                editText.inputType =
                    InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            }
    }

}