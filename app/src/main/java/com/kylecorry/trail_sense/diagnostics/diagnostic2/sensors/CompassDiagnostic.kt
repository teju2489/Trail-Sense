package com.kylecorry.trail_sense.diagnostics.diagnostic2.sensors

import android.content.Context
import com.kylecorry.andromeda.sense.compass.ICompass
import com.kylecorry.trail_sense.diagnostics.diagnostic2.DiagnosticCode2
import com.kylecorry.trail_sense.shared.sensors.SensorService

class CompassDiagnostic(context: Context) : BaseSensorDiagnostic<ICompass>(context) {

    override fun getNoSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.NoCompass
    }

    override fun getPoorSensorCode(): DiagnosticCode2 {
        return DiagnosticCode2.PoorCompass
    }

    override fun getSensor(context: Context): ICompass? {
        val sensors = SensorService(context)
        return if (sensors.hasCompass()) sensors.getCompass() else null
    }

}