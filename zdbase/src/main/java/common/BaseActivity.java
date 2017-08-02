package common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.loaderskin.entity.DynamicAttr;
import com.loaderskin.listener.IDynamicNewView;
import com.loaderskin.listener.ISkinUpdate;
import com.loaderskin.loader.SkinInflaterFactory;
import com.loaderskin.loader.SkinManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Author: ChenJing
 * Date: 2017-06-19 下午 6:17
 * Version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    protected Context mContext;
    protected CompositeDisposable disposables;
    private Unbinder unbinder;
    private ActivityResult result;
    private boolean isResponseOnSkinChanging = true;
    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        getLayoutInflater().setFactory(mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
        setContentView(appointLayoutId());
        mContext = this;
        disposables = new CompositeDisposable();
        initPresenter();

        unbinder = ButterKnife.bind(this);

        ActivityManager.getInstance().addActivity(this);

        /*
          状态保存， 子类需要保存状态的全局变量须加入@State注解
         */
        Icepick.restoreInstanceState(this, savedInstanceState);

        initView();
        setListener();
        initRxEvent();
        getData();
    }

    protected void initPresenter() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (result != null) {
            result.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setResult(ActivityResult result) {
        this.result = result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
        unbinder.unbind();
        unbinder = null;
        View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        ActivityManager.unbindReferences(contentView);
        ActivityManager.getInstance().removeActivity(this);
    }

    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }

    @Override
    public void onThemeUpdate() {
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    protected void initRxEvent() {
    }

    protected void getData() {
    }

    protected abstract int appointLayoutId();

    /**
     * 隐藏软键盘
     *
     * @param view editText
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
