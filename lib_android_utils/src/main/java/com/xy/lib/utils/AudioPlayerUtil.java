package com.xy.lib.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * create by XieYan on 2019/4/24
 * description:
 */
public class AudioPlayerUtil {
    private static final String TAG = "AudioPlayerUtil";

    private MediaPlayer mPlayer;
    private OnAudioPlayListener audioPlayListener;
    private MyHandler mHandler;
    private int progress;
    private AudioManager audioManager;
    private OnAudioFocusChangeListener audioFocusChangeListener;


    public AudioPlayerUtil(Context context,OnAudioPlayListener audioPlayListener) {
        this.audioPlayListener = audioPlayListener;
        mHandler = new MyHandler(this);
        if (audioManager == null){
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    /**
     * 开始播放
     * @param filePath
     */
    public void startAudio(String filePath) {
        if (mPlayer != null){
            if (isPlayingAudio()) {
                pauseAudio();
            }else {
                start();
            }
        }else {
            changeAudio(filePath);
            start();
        }
    }

    /**
     * 开始播放
     */
    public void startAudio() {
        if (mPlayer != null){
            if (!isPlayingAudio()) {
                start();
            }
        }
    }

    private void start(){
        if (mPlayer != null){
            mPlayer.start();
        }
        if (audioManager != null){
            audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    /**
     * 切换音频
     * @param filePath
     */
    public void changeAudio(String filePath) {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                setListener();
                mPlayer.setLooping(false);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            // 切音频之前先重置，释放掉之前的资源
            mPlayer.reset();
            // 设置播放源
            mPlayer.setDataSource(filePath);
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mPlayer.prepare();
//            // 开始播放
//            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (audioPlayListener != null) {
                audioPlayListener.onError(e.toString());
            }
        }

    }

    /**
     * 设置音频焦点监听
     * @param audioFocusChangeListener
     */
    public void setAudioFocusChangeListener(OnAudioFocusChangeListener audioFocusChangeListener) {
        this.audioFocusChangeListener = audioFocusChangeListener;
    }

    private void setListener() {
        if (mPlayer != null) {
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mHandler.sendEmptyMessage(MyHandler.CODE_START_PLAY);
                    if (audioPlayListener != null) {
                        audioPlayListener.onPrepared(progress);
                    }
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (audioPlayListener != null) {
                        audioPlayListener.onCompletion();
                    }
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (audioPlayListener != null) {
                        audioPlayListener.onError(String.format("OnErrorListener what:%d extra:%d", what, extra));
                    }
                    return true;
                }
            });
        }
    }

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.i(TAG, "onAudioFocusChange: " + focusChange);
            if (audioFocusChangeListener != null){
                audioFocusChangeListener.onAudioFocusChange(focusChange);
            }
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点，这个时候需要停止播放，并释放资源。根据不同的逻辑，有时候还会释放焦点
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂失去焦点，这时可以暂停播放，但是不必要释放资源，因为很快又会获取到焦点
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂失去焦点，但是可以跟新的焦点拥有者同时播放，并做降噪处理
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    //获得了音频焦点，可以播放声音
                    break;
            }
        }
    };

    /**
     * 获取音频时长
     * @return
     */
    public long getAudioDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 停止播放
     */
    public void stopAudio() {
        if (this.mPlayer != null) {
            this.endPlay();
            if (this.audioPlayListener != null) {
                this.audioPlayListener.onInterrupt();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pauseAudio() {
        if (this.mPlayer != null && isPlayingAudio()) {
            mPlayer.pause();
        }
    }

    /**
     * 拖动音频
     * @param progress
     */
    public void seekTo(int progress) {
        if (this.mPlayer != null) {
            this.progress = 0;
            mPlayer.seekTo(progress);
        }else {
            this.progress = progress;
        }
    }

    private void endPlay() {
        if (audioManager != null){
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
            mHandler.removeMessages(MyHandler.CODE_START_PLAY);
        }

    }

    /**
     * 结束播放，释放资源
     */
    public void release(){
        endPlay();
    }

    /**
     * 是否正在播放
     * @return
     */
    public boolean isPlayingAudio() {
        return this.mPlayer != null && this.mPlayer.isPlaying();
    }

    public static class MyHandler extends Handler {
        WeakReference<AudioPlayerUtil> mWeakReference;

        public static final int CODE_START_PLAY = 0;
        long mIntervalTime = 500L;

        public MyHandler(AudioPlayerUtil audioPlayerUtil) {
            mWeakReference = new WeakReference<>(audioPlayerUtil);
        }

        @Override
        public void handleMessage(Message msg) {
            AudioPlayerUtil audioPlayerUtil = mWeakReference.get();
            if (audioPlayerUtil != null) {
                switch (msg.what) {
                    case CODE_START_PLAY:
                        if (audioPlayerUtil.audioPlayListener != null) {
                            audioPlayerUtil.audioPlayListener.onPlaying(audioPlayerUtil.mPlayer.getCurrentPosition());
                        }
                        sendEmptyMessageDelayed(CODE_START_PLAY, mIntervalTime);
                        break;
                }
            }
        }
    }

    public interface OnAudioPlayListener {
        void onPrepared(int progress);

        void onCompletion();

        void onInterrupt();

        void onError(String var1);

        void onPlaying(int var1);
    }

    public interface OnAudioFocusChangeListener{
        void onAudioFocusChange(int focusChange);
    }
}
