package com.kylecorry.trail_sense.tools.ui

import android.content.Context
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.core.tryOrNothing
import com.kylecorry.ceres.list.ListItem
import com.kylecorry.ceres.list.ListItemMapper
import com.kylecorry.ceres.list.ResourceListIcon
import com.kylecorry.trail_sense.shared.navigation.IAppNavigation

class ToolListItemMapper(private val context: Context, private val navigation: IAppNavigation) :
    ListItemMapper<ToolsFragment.ToolListItem> {
    override fun map(value: ToolsFragment.ToolListItem): ListItem {
        val iconTint = Resources.androidTextColorSecondary(context)
        val headerColor =
            Resources.getAndroidColorAttr(context, androidx.appcompat.R.attr.colorPrimary)

        val isHeader = value.action == null && value.icon == null

        return ListItem(
            0,
            buildSpannedString {
                if (isHeader) {
                    color(headerColor) {
                        append(value.name)
                    }
                } else {
                    append(value.name)
                }
            },
            value.description,
            icon = value.icon?.let { ResourceListIcon(it, iconTint) }
        ) {
            value.action?.let {
                tryOrNothing {
                    navigation.navigate(it, listOf())
                }
            }
        }
    }
}