package com.kylecorry.trail_sense.shared

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import com.kylecorry.trail_sense.main.MainActivity

object NavigationUtils {

    fun pendingIntent(context: Context, route: String, args: Bundle? = null): PendingIntent {
        // TODO: Fix this by passing the route / args in the intent
        return MainActivity.pendingIntent(context)
    }

}