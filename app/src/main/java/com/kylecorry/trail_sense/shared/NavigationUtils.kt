package com.kylecorry.trail_sense.shared

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import com.kylecorry.trail_sense.main.MainActivity

object NavigationUtils {

    fun pendingIntent(context: Context, route: String, args: Bundle? = null): PendingIntent {
        return PendingIntent.getActivity(
            context,
            27383254 + route.hashCode() + (args?.hashCode() ?: 0),
            MainActivity.intent(context).also {
                it.putExtra("route", route)
                it.putExtra("route_args", args)
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun pendingIntent(context: Context): PendingIntent {
        return PendingIntent.getActivity(
            context,
            27383254,
            MainActivity.intent(context),
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

}