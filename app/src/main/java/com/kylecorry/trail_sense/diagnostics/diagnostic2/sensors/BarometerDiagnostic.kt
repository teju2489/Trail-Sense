package com.kylecorry.trail_sense.diagnostics.diagnostic2.sensors

import android.content.Context
import android.hardware.Sensor
import com.kylecorry.andromeda.sense.Sensors
import com.kylecorry.andromeda.sense.barometer.IBarometer
import com.kylecorry.trail_sense.diagnostics.diagnostic2.DiagnosticCode2
import com.kylecorry.trail_sense.shared.sensors.SensorService

class BarometerDiagnostic(context: Context) : BaseSensorDiagnostic<IBarometer>(context) {

    override fun getNoSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.NoBarometer
    }

    override fun getPoorSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.PoorBarometer
    }

    override fun getSensor(context: Context): IBarometer? {
        val sensors = SensorService(context)
        return if (Sensors.hasSensor(context, Sensor.TYPE_PRESSURE)) {
            sensors.getBarometer()
        } else {
            null
        }
    }

}