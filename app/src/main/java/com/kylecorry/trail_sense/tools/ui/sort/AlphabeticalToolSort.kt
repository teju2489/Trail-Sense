package com.kylecorry.trail_sense.tools.ui.sort

import com.kylecorry.trail_sense.tools.ui.Tool

class AlphabeticalToolSort : ToolSort {
    override fun sort(tools: List<Tool>): List<CategorizedTools> {
        return listOf(CategorizedTools(null, tools.sortedBy { it.name }))
    }

}