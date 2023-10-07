package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.kylecorry.andromeda.core.coroutines.onDefault
import com.kylecorry.andromeda.core.coroutines.onIO
import com.kylecorry.sol.math.statistics.Statistics
import com.kylecorry.trail_sense.ml.CloudClassifierModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.model.Model
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
            val inputFeatures = TensorBuffer.createFixedSize(
                intArrayOf(1, image.width, image.height, 3),
                DataType.FLOAT32
            )
            val byteBuffer = ByteBuffer.allocateDirect(image.width * image.height * 3 * 4)
            byteBuffer.order(ByteOrder.nativeOrder())

            val pixels = IntArray(image.width * image.height)
            image.getPixels(pixels, 0, image.width, 0, 0, image.width, image.height)

            for (pixel in pixels) {
                byteBuffer.putFloat(pixel.red.toFloat())
                byteBuffer.putFloat(pixel.green.toFloat())
                byteBuffer.putFloat(pixel.blue.toFloat())
            }

            inputFeatures.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeatures)

            val values = outputs.outputFeature0AsTensorBuffer.floatArray.toList()

            Statistics.softmax(values)
        } finally {
            model.close()
        }
    }
}