package common;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.dongdongkeji.base.utils.Utils;

/**
 * Author: ChenJing
 * Date: 2017-06-19 下午 5:31
 * Version: 1.0
 */

public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

        MultiDex.install(this);
    }
}
