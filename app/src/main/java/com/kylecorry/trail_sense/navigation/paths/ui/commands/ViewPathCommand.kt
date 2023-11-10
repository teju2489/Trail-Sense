package com.kylecorry.trail_sense.navigation.paths.ui.commands

import androidx.core.os.bundleOf
import com.kylecorry.trail_sense.main.MyNavController
import com.kylecorry.trail_sense.main.Navigation
import com.kylecorry.trail_sense.navigation.paths.domain.Path


class ViewPathCommand(
    private val navController: MyNavController
) : IPathCommand {

    override fun execute(path: Path) {
        execute(path.id)
    }

    fun execute(id: Long) {
        navController.navigate(Navigation.PATH, bundleOf("path_id" to id))
    }
}