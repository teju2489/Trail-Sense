package com.kylecorry.trail_sense.shared

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import com.kylecorry.trail_sense.main.MainActivity
import com.kylecorry.trail_sense.main.MyNavController

object NavigationUtils {

    fun pendingIntent(context: Context, route: String, args: Bundle? = null): PendingIntent {
        return PendingIntent.getActivity(
            context,
            27383254 + route.hashCode() + (args?.hashCode() ?: 0),
            MainActivity.intent(context).also {
                MyNavController.populateDeepLink(it, route, args)
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}