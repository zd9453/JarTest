package common;

import android.view.View;

/**
 * 定义一个抽象类，用来处理点击事件，防止快速点击
 * <p>
 * Author: ChenJing
 * Date: 2017-03-22 上午 11:31
 * Version: 1.0
 */

public abstract class SafeClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 800;
    private long minClickTime = MIN_CLICK_DELAY_TIME;

    private long lastClickTime = 0L;

    public SafeClickListener() {
    }

    public SafeClickListener(long minClickTime) {
        this.minClickTime = minClickTime;
    }

    @Override
    public void onClick(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime > minClickTime) {
            lastClickTime = clickTime;
            onSafeClick(view);
        }
    }

    public abstract void onSafeClick(View view);
}