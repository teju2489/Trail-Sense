package com.kylecorry.trail_sense.shared.data

import com.kylecorry.luna.cache.StateEffect
import com.kylecorry.sol.units.Coordinate
import com.kylecorry.sol.units.Distance
import java.time.Duration
import java.time.Instant

class CustomHooks {
    private val effects = mutableMapOf<String, StateEffect>()
    private val effectLock = Any()

    fun effect(key: String, vararg values: Any?, action: () -> Unit) {
        val evaluated = values.map {
            if (it is HookTrigger) {
                it.evaluate(key)
            } else {
                it
            }
        }

        val effect = synchronized(effectLock) {
            effects.getOrPut(key) { StateEffect() }
        }

        effect.runIfChanged(*evaluated.toTypedArray()) {
            action()
        }
    }
}

fun interface HookTrigger {
    fun evaluate(name: String): Boolean
}

class HookTriggers {
    private val locationTriggers = mutableMapOf<String, LocationHookTrigger>()
    private val locationLock = Any()

    private val timeTriggers = mutableMapOf<String, TimeHookTrigger>()
    private val timeLock = Any()

    fun location(location: Coordinate, threshold: Distance): HookTrigger {
        return HookTrigger { name ->
            val conditional = synchronized(locationLock) {
                locationTriggers.getOrPut(name) { LocationHookTrigger() }
            }
            conditional.getValue(location, threshold)
        }
    }

    fun time(threshold: Duration): HookTrigger {
        return HookTrigger { name ->
            val conditional = synchronized(timeLock) {
                timeTriggers.getOrPut(name) { TimeHookTrigger() }
            }
            conditional.getValue(Instant.now(), threshold)
        }
    }

    private class LocationHookTrigger {

        private var lastLocation: Coordinate? = null
        private val lock = Any()
        private var lastReturnValue = false

        fun getValue(location: Coordinate, threshold: Distance): Boolean {
            synchronized(lock) {
                if (lastLocation == null) {
                    lastLocation = location
                    lastReturnValue = !lastReturnValue
                    return lastReturnValue
                }

                val distance = location.distanceTo(lastLocation!!)
                if (distance >= threshold.meters().distance) {
                    lastLocation = location
                    lastReturnValue = !lastReturnValue
                    return lastReturnValue
                }

                return lastReturnValue
            }
        }

    }

    private class TimeHookTrigger {

        private var lastTime: Instant? = null
        private val lock = Any()
        private var lastReturnValue = false

        fun getValue(time: Instant, threshold: Duration): Boolean {
            synchronized(lock) {
                if (lastTime == null) {
                    lastTime = time
                    lastReturnValue = !lastReturnValue
                    return lastReturnValue
                }

                if (Duration.between(lastTime, time) >= threshold) {
                    lastTime = time
                    lastReturnValue = !lastReturnValue
                    return lastReturnValue
                }

                return lastReturnValue
            }
        }
    }
}