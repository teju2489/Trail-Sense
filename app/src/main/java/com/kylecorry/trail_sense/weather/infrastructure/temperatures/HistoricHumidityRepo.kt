package com.kylecorry.trail_sense.weather.infrastructure.temperatures

import android.content.Context
import com.kylecorry.sol.math.Range
import com.kylecorry.sol.math.interpolation.NewtonInterpolator
import com.kylecorry.sol.time.Time
import com.kylecorry.sol.time.Time.daysUntil
import com.kylecorry.sol.units.Coordinate
import com.kylecorry.sol.units.Reading
import com.kylecorry.sol.units.Temperature
import com.kylecorry.trail_sense.shared.extensions.onDefault
import com.kylecorry.trail_sense.weather.infrastructure.temperatures.calculators.DailyTemperatureCalculator
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.ZonedDateTime

internal class HistoricHumidityRepo(private val context: Context) {

    private val interpolator = NewtonInterpolator()

    suspend fun getYearlyHumidity(
        year: Int,
        location: Coordinate
    ): List<Pair<LocalDate, Float>> = onDefault {
        val monthly =
            HistoricMonthlyHumidityRepo.getMonthlyHumidity(context, location)

        Time.getYearlyValues(year) {
            getDailyHumidity(location, it, monthly)
        }
    }

    suspend fun getHumidity(
        location: Coordinate,
        date: LocalDate
    ): Float {
        return getDailyHumidity(location, date)
    }

    private suspend fun getDailyHumidity(
        location: Coordinate,
        date: LocalDate,
        monthlyRanges: Map<Month, Float>? = null
    ): Float = onDefault {
        val months =
            monthlyRanges ?: HistoricMonthlyHumidityRepo.getMonthlyHumidity(
                context, location
            )

        val lookupMonths = getSurroundingMonths(date)

        val start = lookupMonths.first()

        val xs = lookupMonths.map {
            start.daysUntil(it).toFloat()
        }

        val values = lookupMonths.map {
            months[it.month] ?: 0f
        }

        val x = start.daysUntil(date).toFloat()

        interpolator.interpolate(x, xs, values)
    }

    private fun getSurroundingMonths(date: LocalDate): List<LocalDate> {
        val midMonth = LocalDate.of(date.year, date.month, 15)
        return if (date > midMonth) {
            listOf(
                midMonth.minusMonths(1),
                midMonth,
                // The date is between these
                midMonth.plusMonths(1),
                midMonth.plusMonths(2)
            )
        } else {
            listOf(
                midMonth.minusMonths(2),
                midMonth.minusMonths(1),
                // The date is between these
                midMonth,
                midMonth.plusMonths(1)
            )
        }
    }

}