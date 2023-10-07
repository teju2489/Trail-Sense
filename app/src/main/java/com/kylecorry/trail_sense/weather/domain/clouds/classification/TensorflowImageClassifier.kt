package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import com.kylecorry.andromeda.core.coroutines.onDefault
import com.kylecorry.andromeda.core.coroutines.onIO
import com.kylecorry.sol.math.statistics.Statistics
import com.kylecorry.trail_sense.ml.CloudClassifierModel
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class TensorflowImageClassifier(val context: Context) {

    // To make this generic, consider using the Interpreter API instead: https://github.com/tensorflow/tflite-support/issues/534#issuecomment-865465219
    private suspend fun loadModel(): CloudClassifierModel = onIO {
        val options = Model.Options.Builder()
            .setDevice(Model.Device.CPU)
            .setNumThreads(4)
            .build()
        CloudClassifierModel.newInstance(context, options)
    }

    suspend fun classify(image: Bitmap): List<Float> = onDefault {
        val model = loadModel()

        try {
            val outputs = model.process(TensorImage.fromBitmap(image))
            val scores = outputs.probabilityAsCategoryList.sortedBy { it.index }.map { it.score }
            Statistics.softmax(scores)
        } finally {
            model.close()
        }
    }
}