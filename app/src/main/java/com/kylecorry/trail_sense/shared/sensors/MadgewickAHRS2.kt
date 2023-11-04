package com.kylecorry.trail_sense.shared.sensors

import com.kylecorry.andromeda.sense.orientation.IGyroscope
import com.kylecorry.andromeda.sense.orientation.IOrientationSensor

import android.content.Context
import android.hardware.SensorManager
import android.os.SystemClock
import com.kylecorry.andromeda.core.sensors.AbstractSensor
import com.kylecorry.andromeda.sense.accelerometer.IAccelerometer
import com.kylecorry.andromeda.sense.accelerometer.LowPassAccelerometer
import com.kylecorry.andromeda.sense.magnetometer.IMagnetometer
import com.kylecorry.andromeda.sense.magnetometer.LowPassMagnetometer
import com.kylecorry.sol.math.*
import kotlin.math.sqrt

// Adapted from https://github.com/xioTechnologies/Fusion
class MadgwickAHRS2(
    private val context: Context,
    gain: Float = 0.2f,
    private val accelerometer: IAccelerometer? = null,
    private val gyro: IGyroscope? = null,
    private val magnetometer: IMagnetometer? = null,
    sensorDelay: Int = SensorManager.SENSOR_DELAY_FASTEST
) : AbstractSensor(),
    IOrientationSensor {

    private val _orientation = Quaternion.zero.toFloatArray()

    private val _accelerometer: IAccelerometer by lazy {
        accelerometer ?: LowPassAccelerometer(context, sensorDelay)
    }
    private val _gyro = Gyroscope2(context, sensorDelay)
    private val _magnetometer: IMagnetometer by lazy {
        magnetometer ?: LowPassMagnetometer(context, sensorDelay)
    }

    private val lock = Object()

    override val orientation
        get() = Quaternion.from(rawOrientation)

    override val rawOrientation: FloatArray
        get() {
            return synchronized(lock) {
                _orientation.clone()
            }
        }


    private var hasMag = false
    private var hasAcc = false
    private var hasGyr = false
    private val NS2S = 1.0f / 1000000000.0f

    private var madgwick = Madgwick(gain)

    override val hasValidReading: Boolean
        get() = hasReading

    private var hasReading = false

    private var lastTime = 0L
    private var dt = 0f

    private fun onSensorUpdate() {
        if (!hasGyr || !hasAcc || !hasMag) {
            return
        }

        madgwick.update(_gyro.deltaRotationVector, _magnetometer.magneticField, _accelerometer.acceleration, dt)

        synchronized(lock) {
            madgwick.quaternion.copyInto(_orientation)
            //QuaternionMath.inverse(_orientation, _orientation)
        }

        hasReading = true

        notifyListeners()
    }

    private fun onAccelUpdate(): Boolean {
        hasAcc = true
        return true
    }

    private fun onMagUpdate(): Boolean {
        hasMag = true
        return true
    }

    private fun onGyroUpdate(): Boolean {
        val time = SystemClock.elapsedRealtimeNanos()
        if (lastTime == 0L) {
            lastTime = time
            return true
        }
        dt = (time - lastTime) * NS2S
        lastTime = time
        hasGyr = true
        onSensorUpdate()
        return true
    }

    override fun startImpl() {
        _accelerometer.start(this::onAccelUpdate)
        _magnetometer.start(this::onMagUpdate)
        _gyro.start(this::onGyroUpdate)
    }

    override fun stopImpl() {
        _accelerometer.stop(this::onAccelUpdate)
        _magnetometer.stop(this::onMagUpdate)
        _gyro.stop(this::onGyroUpdate)
    }

    private class Madgwick(private val gain: Float = 0f) {

        private val lock = Object()
        private val refQuaternion = FloatArray(4)
        private val rotationMatrix = FloatArray(16)
        private var isFirst = true

        var quaternion = Quaternion.zero.toFloatArray()
            get() = synchronized(lock) {
                field
            }

        fun update(g: FloatArray, m: Vector3, a: Vector3, dt: Float) {
            synchronized(lock) {

                // Compute feedback only if accelerometer measurement valid (avoids NaN in accelerometer normalisation)
                SensorManager.getRotationMatrix(
                    rotationMatrix,
                    null,
                    a.toFloatArray(),
                    m.toFloatArray()
                )

                val trace = rotationMatrix[0] + rotationMatrix[5] + rotationMatrix[10]
                val r = sqrt(1 + trace)
                val s = 1 / (2 * r)
                val w = r / 2
                val x = (rotationMatrix[6] - rotationMatrix[9]) * s
                val y = (rotationMatrix[8] - rotationMatrix[2]) * s
                val z = (rotationMatrix[1] - rotationMatrix[4]) * s

                if (isFirst){
                    quaternion[0] = x
                    quaternion[1] = y
                    quaternion[2] = z
                    quaternion[3] = w
                    QuaternionMath.normalize(quaternion, quaternion)
                    isFirst = false
                    return
                }

                refQuaternion[0] = x
                refQuaternion[1] = y
                refQuaternion[2] = z
                refQuaternion[3] = w

                // Rotate by the gyro rotation vector
                QuaternionMath.multiply(quaternion, g, quaternion)

                // Nudge it back toward the reference quaternion
                QuaternionMath.subtractRotation(refQuaternion, quaternion, refQuaternion)
                refQuaternion[0] *= gain * dt
                refQuaternion[1] *= gain * dt
                refQuaternion[2] *= gain * dt
                QuaternionMath.multiply(quaternion, refQuaternion, quaternion)

                QuaternionMath.normalize(quaternion, quaternion)
            }
        }

    }

}