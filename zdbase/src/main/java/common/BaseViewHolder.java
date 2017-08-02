package common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * ViewHolder基类
 * 所有ViewHolder继承此类
 * <p>
 * Created by CJ on 2016/10/27 0027.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(Context context, ViewGroup root, @LayoutRes int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        ButterKnife.bind(this, itemView);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 抽象方法，用于绑定数据
     *
     * @param itemValue
     * @param position
     * @param onItemClickListener
     */
    protected abstract void bindData(T itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener);

    /**
     * 传递数据
     *
     * @param itemValue
     * @param position
     * @param onItemClickListener
     */
    public void setData(T itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        bindData(itemValue, position, onItemClickListener);
    }

    protected void setItemClick(final T itemValue, final int position, final BaseAdapter.OnItemClickListener onItemClickListener) {
        itemView.setOnClickListener(new SafeClickListener() {
            @Override
            public void onSafeClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemValue, position);
                }
            }
        });
    }
}
