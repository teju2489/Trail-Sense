package com.kylecorry.trail_sense.tools.guide.infrastructure

import androidx.annotation.RawRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kylecorry.andromeda.fragments.show
import com.kylecorry.trail_sense.main.Navigation
import com.kylecorry.trail_sense.shared.requireMyNavigation
import com.kylecorry.trail_sense.tools.guide.ui.GuideBottomSheetFragment

object UserGuideUtils {

    fun showGuide(fragment: Fragment, @RawRes guideId: Int): BottomSheetDialogFragment? {
        val guides = Guides.guides(fragment.requireContext())
        val guide =
            guides.flatMap { it.guides }.firstOrNull { it.contents == guideId } ?: return null
        val sheet = GuideBottomSheetFragment(guide)
        sheet.show(fragment)
        return sheet
    }

    fun openGuide(fragment: Fragment, @RawRes guideId: Int) {
        val navController = fragment.requireMyNavigation()
        val guides = Guides.guides(fragment.requireContext())

        val guide = guides.flatMap { it.guides }.firstOrNull { it.contents == guideId }

        if (guide != null) {
            navController.navigate(
                Navigation.GUIDE, bundleOf(
                    "guide_name" to guide.name,
                    "guide_contents" to guide.contents
                )
            )
        }

    }

}