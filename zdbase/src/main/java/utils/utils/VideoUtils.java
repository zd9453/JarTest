package utils.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * function description
 *
 * @author zhaiyong    2017/7/3
 * @version 1.0 <changes description>
 */

public class VideoUtils implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnVideoSizeChangedListener {

    private MediaPlayer mediaPlayer;
    private VideoListener videoListener;
    private int currentPlayPosition = -1;
    private Matrix matrix;
    private TextureView textureView;

    public void initMediaPlayer(MediaPlayer mediaPlayer, TextureView textureView) {
        this.mediaPlayer = mediaPlayer;
        this.textureView = textureView;
        textureView.setSurfaceTextureListener(new SurfaceListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                VideoUtils.this.mediaPlayer.setSurface(new Surface(surface));
            }
        });
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);

    }


    public void transformVideo(TextureView textureView, float videoWidth, float videoHeight) {
        if (textureView.getHeight() == 0 || textureView.getWidth() == 0) {

            return;
        }
        float sx = (float) textureView.getWidth() / videoWidth;
        float sy = (float) textureView.getHeight() / videoHeight;

        float maxScale = Math.max(sx, sy);
        if (this.matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //第2步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((textureView.getWidth() - videoWidth) / 2, (textureView.getHeight() - videoHeight) / 2);

        //第1步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth / (float) textureView.getWidth(), videoHeight / (float) textureView.getHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, textureView.getWidth() / 2, textureView.getHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        Log.d(TAG, "transformVideo, maxScale=" + maxScale);

        textureView.setTransform(matrix);
        textureView.postInvalidate();
        Log.d(TAG, "transformVideo, videoWidth=" + videoWidth + "," + "videoHeight=" + videoHeight);
    }

    public void setVideoListener(VideoListener videoListener) {
        this.videoListener = videoListener;
    }

    public void playVideo(Context context, String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(path));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseVideo() {
        currentPlayPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }

    public void stopVideo() {
        mediaPlayer.stop();
    }

    public void resumeVideo() {
        if (currentPlayPosition == -1) {
            mediaPlayer.seekTo(0);
        } else {
            mediaPlayer.seekTo(currentPlayPosition);
        }
        mediaPlayer.start();
    }

    public void releaseVideo() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (videoListener != null) videoListener.onCompleted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (videoListener != null) videoListener.onErrored();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (videoListener != null) videoListener.onPrepared();
        mp.start();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        int mVideoWidth = mp.getVideoWidth();
        int mVideoHeight = mp.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            textureView.getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
            textureView.requestLayout();
            transformVideo(textureView, mVideoWidth, mVideoHeight);
            Log.d(TAG, String.format("OnVideoSizeChangedListener, mVideoWidth=%d,mVideoHeight=%d", mVideoWidth, mVideoHeight));
        }
    }

    public interface VideoListener {
        void onPrepared();

        void onErrored();

        void onCompleted();
    }

    private class SurfaceListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    }
}
