package common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongdongkeji.base.BaseLayoutInflater;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Author: ChenJing
 * Date: 2017-06-20 上午 11:31
 * Version: 1.0
 */

public abstract class BaseFragment extends Fragment {

    private static final String PARAM_STATE_IS_HIDDEN = "is_hidden";

    protected View rootView;
    private Unbinder unbinder;
    protected Context mContext;

    protected CompositeDisposable disposables;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (savedInstanceState != null) {
            /*
              根据保存的状态，控制当前Fragment的显示隐藏，解决内存重启时多个Fragment重叠问题
             */
            boolean isHidden = savedInstanceState.getBoolean(PARAM_STATE_IS_HIDDEN, false);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isHidden) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }

        /*
          状态保存， 子类需要保存状态的全局变量须加入@State注解
         */
        Icepick.restoreInstanceState(this, savedInstanceState);

        disposables = new CompositeDisposable();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            BaseLayoutInflater baseLayoutInflater = new BaseLayoutInflater(mContext);
            rootView = baseLayoutInflater.inflate(appointLayoutId(), null);
            unbinder = ButterKnife.bind(this, rootView);

            initView();
            setListener();
            getData();
            initEvent();
        } else {
            //避免重复添加View
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(PARAM_STATE_IS_HIDDEN, isHidden());
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (rootView != null) {
            ActivityManager.unbindReferences(rootView);
            rootView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.dispose();
            disposables.clear();
            disposables = null;
        }
    }

    /**
     * 初始化控件
     */
    protected void initView() {
    }

    /**
     * 设置事件监听
     */
    protected void setListener() {
    }

    /**
     * 获取数据
     */
    protected void getData() {
    }

    /**
     * 初始化RxBus Event
     */
    protected void initEvent() {
    }

    protected abstract int appointLayoutId();
}
