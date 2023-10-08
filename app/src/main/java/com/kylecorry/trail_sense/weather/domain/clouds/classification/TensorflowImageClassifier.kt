package com.kylecorry.trail_sense.weather.domain.clouds.classification

import android.content.Context
import android.graphics.Bitmap
import com.kylecorry.andromeda.core.coroutines.onDefault
import com.kylecorry.andromeda.core.coroutines.onIO
import com.kylecorry.sol.math.statistics.Statistics
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class TensorflowImageClassifier(
    private val context: Context,
    private val modelFilePath: String,
    private val imageProcessor: ImageProcessor = ImageProcessor.Builder().build(),
    private val shouldApplySoftmax: Boolean = false,
    private val numThreads: Int = 4,
    private val useNNAPI: Boolean = false
) {

    private suspend fun loadModel(): Interpreter = onIO {
        val options = Interpreter.Options()
        options.numThreads = numThreads
        options.useNNAPI = useNNAPI
        val file = FileUtil.loadMappedFile(context, modelFilePath)
        Interpreter(file, options)
    }

    suspend fun classify(image: Bitmap): List<Float> = onDefault {
        loadModel().use { model ->
            val inputType = model.getInputTensor(0).dataType()
            val input = TensorImage(inputType).also {
                it.load(image)
                imageProcessor.process(it)
            }

            val outputType = model.getOutputTensor(0).dataType()
            val outputShape = model.getOutputTensor(0).shape()
            val output = TensorBuffer.createFixedSize(outputShape, outputType)
            model.run(input.buffer, output.buffer)

            val values = output.floatArray.toList()

            if (shouldApplySoftmax) {
                Statistics.softmax(values)
            } else {
                values
            }
        }
    }
}