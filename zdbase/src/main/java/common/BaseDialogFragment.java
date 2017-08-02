package common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongdongkeji.base.R;
import com.loaderskin.loader.SkinInflaterFactory;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 弹框基类
 * Created by CJ on 2016/12/26 0026.
 *
 * @version 1.3
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Context context;
    protected View contentView;
    private SkinInflaterFactory mSkinInflaterFactory;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        configDialog(dialog);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        inflater.setFactory(mSkinInflaterFactory);
        int resource = initLayoutResId();
        contentView = inflater.inflate(resource, container, false);

        unbinder = ButterKnife.bind(this, contentView);

        initView();
        initListener();
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
        context = null;
        unbinder.unbind();
    }

    protected void configDialog(Dialog dialog) {
    }

    protected void initView() {
    }

    protected void initListener() {
    }

    /**
     * 指定布局文件
     *
     * @return
     */
    protected abstract int initLayoutResId();
}