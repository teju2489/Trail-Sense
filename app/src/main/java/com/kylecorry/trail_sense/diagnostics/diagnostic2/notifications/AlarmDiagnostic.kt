package com.kylecorry.trail_sense.diagnostics.diagnostic2.notifications

import android.content.Context
import com.kylecorry.andromeda.permissions.Permissions
import com.kylecorry.andromeda.permissions.SpecialPermission
import com.kylecorry.trail_sense.diagnostics.diagnostic2.Diagnostic
import com.kylecorry.trail_sense.diagnostics.diagnostic2.DiagnosticCode2

class AlarmDiagnostic(private val context: Context) : Diagnostic {
    override suspend fun scan(): List<DiagnosticCode2> {
        if (!Permissions.hasPermission(context, SpecialPermission.SCHEDULE_EXACT_ALARMS)) {
            return listOf(DiagnosticCode2.NoScheduleExactAlarmPermission)
        }

        return emptyList()
    }
}