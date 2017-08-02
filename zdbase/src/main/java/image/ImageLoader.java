package image;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dongdongkeji.base.R;
import com.dongdongkeji.base.utils.ScreenUtils;
import com.dongdongkeji.base.utils.SizeUtils;

/**
 * Author: ChenJing
 * Date: 2017-06-20 下午 2:04
 * Version: 1.0
 */

public class ImageLoader {

    private static final int DEFAULT_PLACEHOLDER = R.drawable.img_placeholder;
    private static final int DEFAULT_ERROR = R.drawable.img_placeholder;

    private static ImageLoader instance;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    /**
     * 将图片地址转换成获取缩略图的地址
     *
     * @param url    图片URL
     * @param width  显示宽度(单位dp，全屏传0，显示原图片尺寸)
     * @param height 显示高度(单位dp，根据宽度自适应传0)
     * @return
     */
    public String convertUrl(String url, int width, int height) {
        if (width == -1) {
            return url;
        }
        width = SizeUtils.dp2px(width);
        height = SizeUtils.dp2px(height);
        StringBuffer stringBuffer = new StringBuffer(url);
        if (width == 0) {
            width = ScreenUtils.getScreenWidth();
        }
        if (height == 0) {
            stringBuffer.append("?imageView2/2/w/");
            stringBuffer.append(width);
        } else {
            stringBuffer.append("?imageView2/1/w/");
            stringBuffer.append(width);
            stringBuffer.append("/h/");
            stringBuffer.append(height);
        }
        return stringBuffer.toString();
    }

    /**
     * 加载七牛网络图片（使用默认占位图）
     *
     * @param context
     * @param imageView
     * @param url
     * @param width
     * @param height
     */
    public void loadQiNiuImage(Context context, ImageView imageView, String url, int width, int height) {
        String loadUrl = convertUrl(url, width, height);
        loadImage(context, imageView, loadUrl);
    }

    /**
     * 加载图片（使用指定占位图）
     *
     * @param context
     * @param imageView
     * @param url
     * @param width
     * @param height
     * @param placeholder
     * @param error
     */
    public void loadQiNiuImage(Context context, ImageView imageView, String url, int width, int height, int placeholder, int error) {
        String loadUrl = convertUrl(url, width, height);
        loadImage(context, imageView, loadUrl, placeholder, error);
    }

    /**
     * 加载图片（带请求监听）
     *
     * @param context
     * @param imageView
     * @param url
     * @param width
     * @param height
     * @param placeholder
     * @param error
     * @param requestListener
     */
    public void loadQiNiuImage(Context context, ImageView imageView, String url, int width, int height, int placeholder, int error, RequestListener<String, GlideDrawable> requestListener) {
        String loadUrl = convertUrl(url, width, height);
        load(context, imageView, loadUrl, placeholder, error, 0, 0, requestListener);
    }

    /**
     * 加载图片（使用默认占位图）
     *
     * @param context
     * @param imageView
     * @param url
     */
    public void loadImage(Context context, ImageView imageView, String url) {
        load(context, imageView, url, 0, 0, 0, 0, null);
    }

    /**
     * 加载图片（仅使用错误占位图）
     *
     * @param context   .
     * @param imageView .
     * @param url       .
     */
    public void loadImageView(Context context, ImageView imageView, String url) {
//        load(context, imageView, url, 0, 0, 0, 0, null);
        Glide.with(context)
                .load(url)
                .error(DEFAULT_ERROR)
                .into(imageView);
    }

    /**
     * 加图不缓存，用于相同名字的图片更新
     *
     * @param context   .
     * @param imageView .
     * @param url       .
     */
    public void loadImageWithNew(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     * 加载Gif动图
     *
     * @param context   .
     * @param imageView .
     * @param url       .
     */
    public void loadGifImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .asGif()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /**
     * 加图圆图
     *
     * @param context   .
     * @param imageView .
     * @param url       .
     */
    public void loadCircle(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载图片（使用指定占位图）
     *
     * @param context
     * @param imageView
     * @param url
     */
    public void loadImage(Context context, ImageView imageView, String url, int placeholder, int error) {
        load(context, imageView, url, placeholder, error, 0, 0, null);
    }

    /**
     * 加载图片（带请求监听）
     *
     * @param cxt
     * @param imageView
     * @param url
     * @param placeholder
     * @param error
     * @param width
     * @param height
     * @param requestListener
     */
    public void load(Context cxt, ImageView imageView, String url, int placeholder, int error, int width, int height, RequestListener<String, GlideDrawable> requestListener) {
        RequestManager manager;
        if (cxt instanceof Activity) {
            //使用Activity生命周期
            manager = Glide.with((Activity) cxt);
        } else {
            manager = Glide.with(cxt);
        }

        DrawableRequestBuilder<String> requestBuilder = manager.load(url)
                .crossFade();
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        if (placeholder != 0) {
            requestBuilder.placeholder(placeholder);
        } else {
            requestBuilder.placeholder(DEFAULT_PLACEHOLDER);
        }
        if (error != 0) {
            requestBuilder.error(error);
        } else {
            requestBuilder.error(DEFAULT_ERROR);
        }
        if (width != 0 && height != 0) {
            requestBuilder.override(width, height);
        }
        if (requestListener != null) {
            requestBuilder.listener(requestListener);
        }
        requestBuilder.into(imageView);
    }

    public void loadImage(Context cxt, SimpleTarget<GlideDrawable> target, String url) {
        loadImage(cxt, target, url, 0, 0, 0, 0, null);
    }

    public void loadImage(Context cxt, SimpleTarget<GlideDrawable> target, String url, int placeholder, int error, int width, int height, RequestListener<String, GlideDrawable> requestListener) {
        RequestManager manager;
        if (cxt instanceof Activity) {
            //使用Activity生命周期
            manager = Glide.with((Activity) cxt);
        } else {
            manager = Glide.with(cxt);
        }

        DrawableRequestBuilder<String> requestBuilder = manager.load(url)
                .crossFade();
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        if (placeholder != 0) {
            requestBuilder.placeholder(placeholder);
        } else {
            requestBuilder.placeholder(DEFAULT_PLACEHOLDER);
        }
        if (error != 0) {
            requestBuilder.error(error);
        } else {
            requestBuilder.error(DEFAULT_ERROR);
        }
        if (width != 0 && height != 0) {
            requestBuilder.override(width, height);
        }
        if (requestListener != null) {
            requestBuilder.listener(requestListener);
        }
        requestBuilder.into(target);
    }
}
