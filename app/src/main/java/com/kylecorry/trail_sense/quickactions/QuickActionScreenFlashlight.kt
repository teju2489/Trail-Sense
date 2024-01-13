package com.kylecorry.trail_sense.quickactions

import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.main.Navigation
import com.kylecorry.trail_sense.shared.CustomUiUtils
import com.kylecorry.trail_sense.shared.QuickActionButton
import com.kylecorry.trail_sense.shared.requireMyNavigation

class QuickActionScreenFlashlight(btn: ImageButton, fragment: Fragment) :
    QuickActionButton(btn, fragment) {

    override fun onCreate() {
        super.onCreate()
        button.setImageResource(R.drawable.ic_screen_flashlight)
        CustomUiUtils.setButtonState(button, false)
        button.setOnClickListener {
            fragment.requireMyNavigation().navigate(Navigation.SCREEN_FLASHLIGHT)
        }
    }
}