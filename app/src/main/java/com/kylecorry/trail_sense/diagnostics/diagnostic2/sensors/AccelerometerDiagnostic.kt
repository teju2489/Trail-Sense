package com.kylecorry.trail_sense.diagnostics.diagnostic2.sensors

import android.content.Context
import android.hardware.Sensor
import com.kylecorry.andromeda.sense.Sensors
import com.kylecorry.andromeda.sense.accelerometer.Accelerometer
import com.kylecorry.andromeda.sense.accelerometer.IAccelerometer
import com.kylecorry.trail_sense.diagnostics.diagnostic2.DiagnosticCode2
import com.kylecorry.trail_sense.shared.sensors.SensorService

class AccelerometerDiagnostic(context: Context) : BaseSensorDiagnostic<IAccelerometer>(context) {

    override fun getNoSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.NoAccelerometer
    }

    override fun getPoorSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.PoorAccelerometer
    }

    override fun getSensor(context: Context): IAccelerometer? {
        return if (Sensors.hasSensor(context, Sensor.TYPE_ACCELEROMETER)) {
            Accelerometer(context, SensorService.MOTION_SENSOR_DELAY)
        } else {
            null
        }
    }

}