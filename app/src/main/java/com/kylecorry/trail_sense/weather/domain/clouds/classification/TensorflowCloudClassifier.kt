package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import com.kylecorry.sol.math.SolMath.positive
import com.kylecorry.sol.math.sumOfFloat
import com.kylecorry.sol.science.meteorology.clouds.CloudGenus
import com.kylecorry.trail_sense.shared.ClassificationResult
import com.kylecorry.trail_sense.shared.extensions.onDefault

class TensorflowCloudClassifier(private val context: Context) : ICloudClassifier {

    private val helper by lazy {
        ImageClassifierHelper(
            0.0f,
            2,
            12,
            ImageClassifierHelper.DELEGATE_GPU,
            context
        )
    }


    override suspend fun classify(bitmap: Bitmap): List<ClassificationResult<CloudGenus?>> = onDefault {
        val results = helper.classify(bitmap)

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

        println(results)
        println(results?.size)

        val nnResults = results?.firstOrNull()?.categories?.map { category ->
            ClassificationResult(clouds[category.index], category.score / 100f)
        }?.toMutableList() ?: mutableListOf()
            // Add missing clouds
            clouds.forEach {
                if (nnResults.none { result -> result.value == it }) {
                    nnResults.add(ClassificationResult(it, 0.0f))
                }
            }

        // Normalize
        val total = nnResults.sumOfFloat { it.confidence }
        nnResults.map {
            it.copy(confidence = it.confidence / total.positive(1f))
        }
    }
}