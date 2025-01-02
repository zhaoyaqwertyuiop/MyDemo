package com.zy.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.zy.player.databinding.PlayerActivityMainBinding


class PlayerActivity : AppCompatActivity(){

    private val binding by lazy { PlayerActivityMainBinding.inflate(layoutInflater)}
    private val context by lazy {this}

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Media3Util.o.init(
            this,
            binding.playView1,
            "https://storage.googleapis.com/exoplayer-test-media-1/mp4/dizzy-with-tx3g.mp4"
        )
        Media3Util.o.init(
            this,
            binding.playView2,
            "https://storage.googleapis.com/exoplayer-test-media-1/mp4/dizzy-with-tx3g.mp4"
        )

        binding.playBtn1.setOnClickListener {
            binding.playView1.player?.run {
                // 开始缓冲
                prepare()

                if (isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
        }

        binding.playBtn2.setOnClickListener {
            binding.playView2.player?.run {
                // 开始缓冲
                prepare()
                if (isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
        }
    }
}