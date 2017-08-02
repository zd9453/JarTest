package common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dongdongkeji.base.mvp.BasePresenter;
import com.dongdongkeji.base.mvp.MvpView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: ChenJing
 * Date: 2017-06-20 上午 11:50
 * Version: 1.0
 */

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment implements MvpView {

    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = checkNotNull(createPresenter(), "Presenter is null!!");
        presenter.attachView(this);
    }

    protected abstract P createPresenter();
}
