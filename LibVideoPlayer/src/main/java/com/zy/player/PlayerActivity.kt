package com.zy.player

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import com.zy.player.databinding.PlayerActivityMainBinding


class PlayerActivity : AppCompatActivity(){

    private val binding by lazy { PlayerActivityMainBinding.inflate(layoutInflater)}
    private val context by lazy {this}

    fun getFileType(fileName: String): String {
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        val videoExtensions = listOf("mp4", "avi", "mov", "wmv", "flv", "mkv")

        val extension = fileName.substringAfterLast('.').toLowerCase()

        return when {
            imageExtensions.contains(extension) -> "Image"
            videoExtensions.contains(extension) -> "Video"
            else -> "Unknown"
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Media3Util.o.init(
            this,
            binding.playView1,
//            "https://storage.googleapis.com/exoplayer-test-media-1/mp4/dizzy-with-tx3g.mp4"
            "http://oss-beijing-sh-internal.openstorage.cn/iptv-oss-prod/common/use_help/heart_help_kangbao.mp4"
        )
        binding.playView1.player?.apply {
            playWhenReady = false
            repeatMode = Player.REPEAT_MODE_OFF
        }
        Media3Util.o.init(
            this,
            binding.playView2,
            "file:///sdcard/Android/data/com.zy.demo/files/bg_video.mp4"
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