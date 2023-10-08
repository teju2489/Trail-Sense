package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import com.kylecorry.sol.science.meteorology.clouds.CloudGenus
import com.kylecorry.trail_sense.shared.ClassificationResult
import com.kylecorry.trail_sense.shared.extensions.onDefault
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp

class TensorflowCloudClassifier(context: Context) : ICloudClassifier {

    private val classifier = TensorflowImageClassifier(
        context,
        "cloud_classifier_model.tflite",
        shouldApplySoftmax = true,
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(IMAGE_SIZE, IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    )


    override suspend fun classify(bitmap: Bitmap): List<ClassificationResult<CloudGenus?>> =
        onDefault {
            val results = classifier.classify(bitmap)

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