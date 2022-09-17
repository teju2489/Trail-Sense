package com.kylecorry.trail_sense.weather.ui.clouds

import android.content.Context
import com.kylecorry.andromeda.alerts.Alerts
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.ceres.list.ListItem
import com.kylecorry.ceres.list.ListItemData
import com.kylecorry.ceres.list.ListItemMapper
import com.kylecorry.ceres.list.ResourceListIcon
import com.kylecorry.sol.science.meteorology.Precipitation
import com.kylecorry.sol.science.meteorology.clouds.CloudGenus
import com.kylecorry.sol.time.Time.toZonedDateTime
import com.kylecorry.sol.units.Reading
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.shared.FormatService
import com.kylecorry.trail_sense.shared.colors.AppColor
import com.kylecorry.trail_sense.weather.domain.clouds.CloudService
import com.kylecorry.trail_sense.weather.infrastructure.clouds.CloudRepo


internal class CloudReadingListItemMapper(private val context: Context) :
    ListItemMapper<Reading<CloudGenus?>> {
    private val repo: CloudRepo = CloudRepo(context)
    private val cloudService: CloudService = CloudService()
    private val formatter = FormatService(context)

    override fun map(value: Reading<CloudGenus?>): ListItem {
        return ListItem(
            value.value?.ordinal?.toLong() ?: -1L,
            repo.getCloudName(value.value),
            repo.getCloudForecast(value.value),
            data = listOf(
                ListItemData(
                    formatter.formatDateTime(
                        value.time.toZonedDateTime(), true,
                        abbreviateMonth = true
                    ),
                    ResourceListIcon(
                        R.drawable.ic_tool_clock,
                        Resources.androidTextColorSecondary(context)
                    )
                )
            ),
            icon = ClippedResourceListIcon(
                repo.getCloudImage(value.value),
                if (value.value == null) AppColor.Blue.color else null,
                size = 48f,
                background = R.drawable.rounded_rectangle
            ) {
                Alerts.image(
                    context,
                    repo.getCloudName(value.value),
                    repo.getCloudImage(value.value)
                )
            }
        ) {
            if (value.value != null) {
                val precipitation = cloudService.getPrecipitation(value.value)
                Alerts.dialog(
                    context,
                    repo.getCloudName(value.value),
                    repo.getCloudDescription(value.value) + "\n\n" +
                            repo.getCloudForecast(value.value) + "\n\n" +
                            getPrecipitationDescription(
                                context,
                                value.value,
                                precipitation,
                                formatter
                            ),
                    cancelText = null
                )
            }
        }
    }

    private fun getPrecipitationDescription(
        context: Context,
        type: CloudGenus?,
        precipitation: List<Precipitation>,
        formatter: FormatService
    ): String {
        return context.getString(
            R.string.precipitation_chance,
            formatter.formatProbability(cloudService.getPrecipitationProbability(type))
        ) + "\n\n" +
                if (precipitation.isEmpty()) context.getString(R.string.precipitation_none) else precipitation.joinToString(
                    "\n"
                ) { formatter.formatPrecipitation(it) }
    }
}