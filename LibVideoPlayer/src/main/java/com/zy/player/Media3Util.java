package com.zy.player;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

/**
 * @Author zhaoya
 * @Date 2024/12/27 10:11
 * @describe
 */
public enum Media3Util {
    o;

    private final String TAG = this.getClass().getSimpleName();

    public void init(AppCompatActivity activity, PlayerView playerView, String url) {
        playerView.setPlayer(new ExoPlayer.Builder(activity).build());
        playerView.getPlayer().setMediaItem(MediaItem.fromUri(url));

        activity.getLifecycle().addObserver((LifecycleEventObserver) (lifecycleOwner, event) -> {
            if (event == Lifecycle.Event.ON_RESUME) {
                playerView.onResume();
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                playerView.onPause();
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                playerView.getPlayer().release();
            }
        });

        // Player.REPEAT_MODE_ALL 无限重复
        // Player.REPEAT_MODE_ONE 重复一次
        // Player.REPEAT_MODE_OFF 不重复
        playerView.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL); // 设置重复模式
        // 设置当缓冲完毕后直接播放视频
        playerView.getPlayer().setPlayWhenReady(true);
        playerView.getPlayer().addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                // 播放状态变化回调
                Log.d(TAG, "onIsPlayingChanged()：" + isPlaying);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                Log.d(TAG, "onPlaybackStateChanged()：" + playbackState);
                switch (playbackState) {
                    case Player.STATE_IDLE: {
                        //播放器停止时的状态
                        break;
                    }
                    case Player.STATE_BUFFERING: {
                        // 正在缓冲数据
                        break;
                    }
                    case Player.STATE_READY: {
                        // 可以开始播放
                        break;
                    }
                    case Player.STATE_ENDED: {
                        // 播放结束
                        break;
                    }
                    default:
                        break;
                }
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                // 获取播放错误信息
                Log.e(TAG, "播放错误：" + error.getMessage());
            }
        });
    }
}
