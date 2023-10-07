package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import com.kylecorry.andromeda.core.bitmap.BitmapUtils.resizeExact
import com.kylecorry.sol.science.meteorology.clouds.CloudGenus
import com.kylecorry.trail_sense.shared.ClassificationResult
import com.kylecorry.trail_sense.shared.extensions.onDefault

class TensorflowCloudClassifier(context: Context) : ICloudClassifier {

    private val helper = TensorflowImageClassifier(context)


    override suspend fun classify(bitmap: Bitmap): List<ClassificationResult<CloudGenus?>> =
        onDefault {
            val resized = bitmap.resizeExact(IMAGE_SIZE, IMAGE_SIZE)
            val results = try {
                helper.classify(resized)
            } finally {
                if (resized != bitmap) {
                    resized.recycle()
                }
            }

            val clouds = listOf(
                CloudGenus.Altocumulus,
                CloudGenus.Altostratus,
                CloudGenus.Cirrocumulus,
                CloudGenus.Cirrostratus,
                CloudGenus.Cirrus,
                null,
                CloudGenus.Cumulonimbus,
                CloudGenus.Cumulus,
                CloudGenus.Nimbostratus,
                CloudGenus.Stratocumulus,
                CloudGenus.Stratus
            )

            results.mapIndexed { index, confidence ->
                ClassificationResult(clouds[index], confidence)
            }.sortedByDescending { it.confidence }
        }

    companion object {
        const val IMAGE_SIZE = 64
    }
}