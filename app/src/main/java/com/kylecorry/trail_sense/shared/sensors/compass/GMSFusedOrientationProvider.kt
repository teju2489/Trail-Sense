package com.kylecorry.trail_sense.shared.sensors.compass

import android.content.Context
import android.hardware.SensorManager
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.FusedOrientationProviderClient
import com.google.android.gms.location.LocationServices
import com.kylecorry.andromeda.core.sensors.AbstractSensor
import com.kylecorry.andromeda.sense.orientation.IOrientationSensor
import com.kylecorry.sol.math.Quaternion
import java.util.concurrent.Executors

class GMSFusedOrientationProvider(context: Context) : IOrientationSensor, AbstractSensor() {
    override var hasValidReading: Boolean = false
        private set
    override var headingAccuracy: Float? = null
        private set
    override val orientation: Quaternion
        get() = Quaternion.from(rawOrientation)
    override val rawOrientation: FloatArray = Quaternion.zero.toFloatArray()

    private val tempQuaternion = Quaternion.zero.toFloatArray()

    private val fusedOrientationProviderClient: FusedOrientationProviderClient =
        LocationServices.getFusedOrientationProviderClient(context)

    private val listener: DeviceOrientationListener =
        DeviceOrientationListener { orientation: DeviceOrientation ->
            orientation.attitude.copyInto(rawOrientation)
            SensorManager.getQuaternionFromVector(tempQuaternion, orientation.attitude)
            val w = tempQuaternion[0]
            val x = tempQuaternion[1]
            val y = tempQuaternion[2]
            val z = tempQuaternion[3]
            rawOrientation[0] = x
            rawOrientation[1] = y
            rawOrientation[2] = z
            rawOrientation[3] = w
            headingAccuracy = orientation.headingErrorDegrees
            hasValidReading = true
            notifyListeners()
        }

    override fun startImpl() {
        val request =
            DeviceOrientationRequest
                .Builder(DeviceOrientationRequest.OUTPUT_PERIOD_DEFAULT)
                .build()

        // Create (or re-use) an Executor or Looper, e.g.
        val executor = Executors.newSingleThreadExecutor()

        // Register the request and listener
        fusedOrientationProviderClient
            .requestOrientationUpdates(request, executor, listener)
            .addOnSuccessListener { }
            .addOnFailureListener { e: Exception? ->
                e?.printStackTrace()
            }
    }

    override fun stopImpl() {
        fusedOrientationProviderClient.removeOrientationUpdates(listener)
    }
}