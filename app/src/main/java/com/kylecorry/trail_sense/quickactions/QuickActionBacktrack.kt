package com.kylecorry.trail_sense.quickactions

import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kylecorry.andromeda.core.topics.generic.ITopic
import com.kylecorry.andromeda.core.topics.generic.replay
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.main.Navigation
import com.kylecorry.trail_sense.navigation.paths.infrastructure.subsystem.BacktrackSubsystem
import com.kylecorry.trail_sense.shared.FeatureState
import com.kylecorry.trail_sense.shared.requireMyNavigation

class QuickActionBacktrack(btn: ImageButton, fragment: Fragment) :
    TopicQuickAction(btn, fragment, hideWhenUnavailable = false) {

    override fun onCreate() {
        super.onCreate()
        button.setImageResource(R.drawable.ic_tool_backtrack)
        button.setOnClickListener {
            fragment.requireMyNavigation().navigate(Navigation.PATH_LIST)
        }
    }

    override val state: ITopic<FeatureState> =
        BacktrackSubsystem.getInstance(context).state.replay()

}