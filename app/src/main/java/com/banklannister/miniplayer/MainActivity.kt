package com.banklannister.miniplayer

import android.app.PictureInPictureParams
import androidx.media3.session.MediaSession
import android.net.Uri
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.banklannister.miniplayer.ui.theme.MiniPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPlayer(videoUrl = "https://joy1.videvo.net/videvo_files/video/free/2019-11/large_watermarked/190301_1_25_11_preview.mp4")
                }
            }
        }
    }

    @Composable
    fun VideoPlayer(videoUrl: String) {
        val context = LocalContext.current
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                prepare()
                play()
            }
        }

        val session = MediaSession.Builder(context, exoPlayer).build()
        session.player = exoPlayer

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                }
            }, modifier = Modifier.fillMaxSize()
        )

    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        onEnterPipMode()

    }

    private fun onEnterPipMode() {
        val aspectRatio = Rational(16, 9)
        val params = PictureInPictureParams
            .Builder()
            .setAspectRatio(aspectRatio)
            .build()

        enterPictureInPictureMode(params)
    }
}

