package common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dongdongkeji.base.mvp.BasePresenter;
import com.dongdongkeji.base.mvp.MvpView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: ChenJing
 * Date: 2017-06-20 上午 11:18
 * Version: 1.0
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity implements MvpView {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = checkNotNull(createPresenter(), "Presenter is null!!!!");
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }

    protected abstract P createPresenter();
}