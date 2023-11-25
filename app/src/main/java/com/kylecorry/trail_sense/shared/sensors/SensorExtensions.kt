package com.kylecorry.trail_sense.shared.sensors

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.kylecorry.andromeda.core.coroutines.BackgroundMinimumState
import com.kylecorry.andromeda.core.sensors.ISensor
import com.kylecorry.andromeda.core.topics.ITopic
import com.kylecorry.andromeda.core.tryOrLog
import com.kylecorry.andromeda.fragments.repeatInBackground
import com.kylecorry.luna.coroutines.IFlowable
import com.kylecorry.luna.coroutines.ListenerFlowWrapper
import com.kylecorry.trail_sense.shared.extensions.onDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.time.Duration
import kotlin.coroutines.CoroutineContext

suspend fun readAll(
    sensors: List<ISensor>,
    timeout: Duration = Duration.ofMinutes(1),
    onlyIfInvalid: Boolean = false,
    forceStopOnCompletion: Boolean = false
) = onDefault {
    try {
        withTimeoutOrNull(timeout.toMillis()) {
            val jobs = mutableListOf<Job>()
            for (sensor in sensors) {
                if (!onlyIfInvalid || !sensor.hasValidReading) {
                    jobs.add(launch { sensor.read() })
                }
            }
            jobs.joinAll()
        }
    } finally {
        if (forceStopOnCompletion) {
            sensors.forEach {
                tryOrLog {
                    it.stop(null)
                }
            }
        }
    }
}

fun ITopic.asFlowable(): IFlowable<Unit> {

    val name = this@asFlowable.javaClass.simpleName + "@" + Integer.toHexString(hashCode())

    return object : ListenerFlowWrapper<Unit>() {
        override fun start() {
            Log.d("Sensor", "Starting $name")
            subscribe(this::onSensorUpdate)
        }

        override fun stop() {
            Log.d("Sensor", "Stopping $name")
            unsubscribe(this::onSensorUpdate)
        }

        private fun onSensorUpdate(): Boolean {
            emit(Unit)
            return true
        }
    }
}

fun <T> Fragment.observe(liveData: LiveData<T>, listener: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) {
        listener(it)
    }
}

fun Fragment.observe(topic: ITopic, listener: () -> Unit) {
    observeFlow2(topic.asFlowable().flow, state = BackgroundMinimumState.Started){
        listener()
    }
}

fun <T> Fragment.observeFlow2(
    flow: Flow<T>,
    state: BackgroundMinimumState = BackgroundMinimumState.Started,
    collectOn: CoroutineContext = Dispatchers.Default,
    observeOn: CoroutineContext = Dispatchers.Main,
    listener: suspend (T) -> Unit
) {
    repeatInBackground(state) {
        withContext(collectOn) {
            flow.collect {
                withContext(observeOn) {
                    listener(it)
                }
            }
        }
    }
}