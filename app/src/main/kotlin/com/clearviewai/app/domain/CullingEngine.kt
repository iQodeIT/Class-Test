package com.clearviewai.app.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import com.clearviewai.app.data.local.entities.MediaItem
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class CullingEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun analyze(item: MediaItem, existingItems: List<MediaItem> = emptyList()): MediaItem {
        if (item.type == "VIDEO") return item // Basic video support for now

        val uri = Uri.parse(item.uri)
        val bitmap = loadBitmap(uri) ?: return item

        val focusScore = calculateFocusScore(bitmap)
        val brightness = calculateBrightness(bitmap)
        val labels = getLabels(bitmap)

        var clutterType: String? = null
        if (labels.any { it.contains("Meme", ignoreCase = true) || it.contains("Cartoon", ignoreCase = true) }) {
            clutterType = "MEME"
        } else if (item.uri.contains("Screenshot", ignoreCase = true)) {
            clutterType = "SCREENSHOT"
        }

        // Simple duplicate detection (same date and size would be better, but we don't have size here yet)
        // For now, let's just mark it if it has the exact same score and date as another one
        val isDuplicate = existingItems.any { it.id != item.id && it.dateAdded == item.dateAdded && it.score == (focusScore + brightness) / 2f }

        return item.copy(
            isBlurry = focusScore < 10.0f,
            isDuplicate = isDuplicate,
            clutterType = clutterType,
            score = (focusScore + brightness) / 2f
        )
    }

    private fun loadBitmap(uri: Uri): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }

            // Target size for analysis
            val targetSize = 512
            options.inSampleSize = calculateInSampleSize(options, targetSize, targetSize)
            options.inJustDecodeBounds = false

            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun calculateFocusScore(bitmap: Bitmap): Float {
        // Simplified focus score: variance of differences between adjacent pixels
        // A sharper image has higher differences at edges
        val width = bitmap.width
        val height = bitmap.height
        if (width < 2 || height < 2) return 0f

        var totalDiff = 0f
        val step = 10 // Sample pixels to save time
        var count = 0
        for (y in 0 until height step step) {
            for (x in 0 until width - 1 step step) {
                val p1 = bitmap.getPixel(x, y)
                val p2 = bitmap.getPixel(x + 1, y)
                val gray1 = (Color.red(p1) + Color.green(p1) + Color.blue(p1)) / 3
                val gray2 = (Color.red(p2) + Color.green(p2) + Color.blue(p2)) / 3
                totalDiff += abs(gray1 - gray2)
                count++
            }
        }
        return if (count > 0) totalDiff / count else 0f
    }

    private fun calculateBrightness(bitmap: Bitmap): Float {
        val width = bitmap.width
        val height = bitmap.height
        var totalBrightness = 0f
        val step = 20
        var count = 0
        for (y in 0 until height step step) {
            for (x in 0 until width step step) {
                val p = bitmap.getPixel(x, y)
                val brightness = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3f
                totalBrightness += brightness
                count++
            }
        }
        return if (count > 0) totalBrightness / count else 0f
    }

    private suspend fun getLabels(bitmap: Bitmap): List<String> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()
            labels.map { it.text }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
