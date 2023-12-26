package com.kylecorry.trail_sense.shared.dem

import android.content.Context
import android.util.Size
import com.kylecorry.andromeda.core.cache.LRUCache
import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.units.Coordinate
import com.kylecorry.trail_sense.shared.data.GeographicImageSource
import com.kylecorry.trail_sense.shared.extensions.onIO

object DigitalElevationModel {
    // Cache
    private val cache = LRUCache<PixelCoordinate, Float>(size = 5)

    // Image data source
    private const val A1 = 0.0880867f
    private const val B1 = 2894.875f
    private const val A2 = 0.31875f
    private const val B2 = 0f
    private const val A3 = 0.03465919f
    private const val B3 = -800f

    private const val latitudePixelsPerDegree = 1440 / 180.0
    private const val longitudePixelsPerDegree = 2880 / 360.0
    private val size = Size(2880, 1440)

    private val source1 = GeographicImageSource(
        size,
        latitudePixelsPerDegree,
        longitudePixelsPerDegree,
        decoder = GeographicImageSource.scaledDecoder(A1, B1)
    )

    private val source2 = GeographicImageSource(
        size,
        latitudePixelsPerDegree,
        longitudePixelsPerDegree,
        decoder = GeographicImageSource.scaledDecoder(A2, B2)
    )

    private val source3 = GeographicImageSource(
        size,
        latitudePixelsPerDegree,
        longitudePixelsPerDegree,
        decoder = GeographicImageSource.scaledDecoder(A3, B3)
    )

    suspend fun getElevation(
        context: Context,
        location: Coordinate
    ): Float = onIO {
        val pixel = source2.getPixel(location)

        // TODO: Allow interpolation, but check against the non interpolated value if close to the bounds

        // TODO: Better handle interpolation
        cache.getOrPut(pixel){
            val pixel2 = source2.read(context, "dem/dem-2.webp", location)[0]
            if (pixel2 <= 50f){
                source1.read(context, "dem/dem-1.webp", location)[0]
            } else if (pixel2 >= 750f){
                source3.read(context, "dem/dem-3.webp", location)[0]
            } else {
                pixel2
            }
        }
    }
}