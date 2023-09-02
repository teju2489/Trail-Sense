package com.kylecorry.trail_sense.diagnostics.diagnostic2

import com.kylecorry.andromeda.core.coroutines.onDefault
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AggregateDiagnostic(
    private val diagnostics: List<Diagnostic>,
    private val progressChanged: (Int, Int) -> Unit = { _, _ -> }
) : Diagnostic {
    override suspend fun scan(): List<DiagnosticCode2> = onDefault {
        // Run diagnostics in parallel
        val codes = mutableSetOf<DiagnosticCode2>()
        val codeLock = Any()
        val progressLock = Any()
        val jobs = mutableListOf<Job>()
        var progress = 0
        for (diagnostic in diagnostics) {
            jobs.add(
                launch {
                    withTimeout(10000) {
                        val results = diagnostic.scan()
                        synchronized(codeLock) {
                            codes.addAll(results)
                        }
                        synchronized(progressLock) {
                            progress++
                            progressChanged(progress, diagnostics.size)
                        }
                    }
                }
            )
        }

        jobs.joinAll()

        codes.toList()
    }
}