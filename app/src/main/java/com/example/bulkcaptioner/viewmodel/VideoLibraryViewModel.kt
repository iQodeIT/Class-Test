package com.example.bulkcaptioner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bulkcaptioner.database.Video
import com.example.bulkcaptioner.database.VideoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bulkcaptioner.database.Video
import com.example.bulkcaptioner.database.VideoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class VideoLibraryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val videoDao: VideoDao
) : ViewModel() {

    val videos = videoDao.getAllVideos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addVideo(uriString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = Uri.parse(uriString)
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)

            val fileName = uri.lastPathSegment ?: "video.mp4"
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            val thumbnail = retriever.getFrameAtTime(0)

            val thumbnailFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
            FileOutputStream(thumbnailFile).use { out ->
                thumbnail?.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
            }

            val video = Video(
                serialNumber = System.currentTimeMillis().toString(),
                uri = uriString,
                fileName = fileName,
                duration = duration,
                thumbnailPath = thumbnailFile.absolutePath
            )
            videoDao.insertVideo(video)
        }
    }
}
