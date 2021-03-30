package com.kylecorry.trail_sense.tools.clock.infrastructure

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kylecorry.trail_sense.NotificationChannels
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.shared.CustomNotificationUtils
import com.kylecorry.trailsensecore.infrastructure.system.NotificationUtils

class NextMinuteBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val time = intent?.getStringExtra(EXTRA_TIME)
        val notification = CustomNotificationUtils.alert(
            context,
            CHANNEL_ID,
            context.getString(R.string.clock_sync_notification, time),
            null,
            R.drawable.ic_tool_clock,
            group = NotificationChannels.GROUP_CLOCK
        )
        NotificationUtils.send(context, NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "ClockSync"
        private const val EXTRA_TIME = "extra_time"
        private const val PENDING_INTENT_ID = 632854823
        private const val NOTIFICATION_ID = 49852323

        fun intent(context: Context, timeString: String): Intent {
            val i = Intent(context, NextMinuteBroadcastReceiver::class.java)
            i.putExtra(EXTRA_TIME, timeString)
            return i
        }

        fun pendingIntent(context: Context, timeString: String): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                PENDING_INTENT_ID,
                intent(context, timeString),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }
}