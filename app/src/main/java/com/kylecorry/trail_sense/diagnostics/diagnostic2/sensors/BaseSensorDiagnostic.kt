package com.kylecorry.trail_sense.diagnostics.diagnostic2.sensors

import android.content.Context
import com.kylecorry.andromeda.core.coroutines.onDefault
import com.kylecorry.andromeda.core.sensors.ISensor
import com.kylecorry.andromeda.core.sensors.Quality
import com.kylecorry.trail_sense.diagnostics.diagnostic2.Diagnostic
import com.kylecorry.trail_sense.diagnostics.diagnostic2.DiagnosticCode2
import kotlinx.coroutines.withTimeout

abstract class BaseSensorDiagnostic<T : ISensor>(
    private val context: Context,
    private val timeout: Long = 1000L
) : Diagnostic {

    private val sensor by lazy { getSensor(context) }

    override suspend fun scan(): List<DiagnosticCode2> = onDefault {
        val sensor = sensor ?: return@onDefault listOfNotNull(getNoSensorCode())

        withTimeout(timeout) {
            sensor.read()
        }

        if (sensor.quality == Quality.Poor) {
            listOfNotNull(getPoorSensorCode())
        } else {
            emptyList()
        }
    }

    protected open fun getNoSensorCode(): DiagnosticCode2? {
        return null
    }

    protected open fun getPoorSensorCode(): DiagnosticCode2? {
        return null
    }

    protected abstract fun getSensor(context: Context): T?

}