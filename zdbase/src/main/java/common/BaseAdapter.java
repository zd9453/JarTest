package common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter基类, RecyclerView的适配器继承自此类
 * <p/>
 * Created by CJ on 2016/10/27 0027.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 装载每个Item的数据的列表
     */
    protected List<T> valueList;

    protected OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setData(valueList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return valueList == null ? 0 : valueList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 刷新数据
     *
     * @param valueList 新的数据列表
     */
    public void refreshData(List<T> valueList) {
        this.valueList = valueList;
        notifyDataSetChanged();
    }

    /**
     * 移除一项数据
     *
     * @param position 索引
     */
    public void removeItem(int position) {
        this.valueList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 添加一项数据
     *
     * @param t 添加的数据
     */
    public void insertItem(int position, T t) {
        if (valueList == null || valueList.size() == 0) {
            if (valueList == null) {
                valueList = new ArrayList<T>();
            }
            this.valueList.add(t);
            notifyItemInserted(position);
        } else {
            this.valueList.add(position, t);
            notifyItemInserted(position);
        }
    }

    /**
     * 添加一项数据
     *
     * @param t 添加的数据
     */
    public void insertItems(int position, List<T> t) {
        if (valueList == null || valueList.size() == 0) {
            if (valueList == null) {
                valueList = new ArrayList<T>();
            }
            this.valueList.addAll(t);
            notifyItemInserted(position);
        } else {
            this.valueList.addAll(position, t);
            notifyItemInserted(position);
        }
    }

    /**
     * 添加一项数据
     *
     * @param list 添加的数据
     */
    public void addItems(List<T> list) {
        if (valueList == null) {
            this.valueList = list;
        } else {
            this.valueList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 移动数据
     *
     * @param from
     * @param to
     */
    public void moveItem(int from, int to) {
        Collections.swap(valueList, from, to);
        notifyItemMoved(from, to);
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        if (this.valueList != null) {
            this.valueList.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 获取数据列表
     *
     * @return
     */
    public List<T> getValueList() {
        return this.valueList;
    }

    /**
     * 创建ViewHolder
     *
     * @param context 上下文
     * @param parent  根视图
     * @return
     */
    protected abstract BaseViewHolder createViewHolder(Context context, ViewGroup parent);

    /**
     * RecyclerView Item点击事件接口
     * <p/>
     * Created by CJ on 2016/10/27 0027.
     */
    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }
}
