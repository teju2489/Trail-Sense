package com.kylecorry.trail_sense.diagnostics.diagnostic2

interface Diagnostic {
    suspend fun scan(): List<DiagnosticCode2>
}