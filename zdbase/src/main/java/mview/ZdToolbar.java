package mview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zd.zdbase.R;

import static com.zd.zdbase.R.id.tool_left_img;

/**
 * use to toolbar
 * <p>基本点击功能，后面根据使用再继续完善
 * Created by zhangdong on 2017/8/1.
 *
 * @version 1.0
 */

public class ZdToolbar extends RelativeLayout implements View.OnClickListener {

    public static final int LEFT_TEXT = 1;
    public static final int LEFT_IMG = 2;
    public static final int RIGHT_TEXT = 3;
    public static final int RIGHT_IMG = 4;

    private Drawable left_img_drawable;
    private String left_text;
    private String title_text;
    private Drawable right_img_drawable;
    private String right_text;
    private ImageButton ib_left_img;
    private TextView tv_left_text;
    private TextView tv_title_text;
    private TextView tv_right_text;
    private ImageButton ib_right_img;
    private ToolbarItemClick itemClick;

    public void setItemClick(ToolbarItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ZdToolbar(Context context) {
        super(context);
        initView(context);
    }

    public ZdToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.ZdToolbar, 0, 0);
        left_img_drawable = typedArray.getDrawable(R.styleable.ZdToolbar_left_img);
        left_text = typedArray.getString(R.styleable.ZdToolbar_left_text);
        title_text = typedArray.getString(R.styleable.ZdToolbar_title);
        right_text = typedArray.getString(R.styleable.ZdToolbar_right_text);
        right_img_drawable = typedArray.getDrawable(R.styleable.ZdToolbar_right_img);
        typedArray.recycle();
        initView(context);
    }

    public ZdToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toolbar, null);
        ib_left_img = ((ImageButton) view.findViewById(tool_left_img));
        tv_left_text = ((TextView) view.findViewById(R.id.tool_left_text));
        tv_title_text = ((TextView) view.findViewById(R.id.tool_title_text));
        tv_right_text = ((TextView) view.findViewById(R.id.tool_right_text));
        ib_right_img = ((ImageButton) view.findViewById(R.id.tool_right_img));

        if (left_img_drawable != null) {
            ib_left_img.setImageDrawable(left_img_drawable);
            ib_left_img.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(left_text)) {
            tv_left_text.setText(left_text);
            tv_left_text.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(title_text)) {
            tv_title_text.setText(title_text);
            tv_title_text.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(right_text)) {
            tv_right_text.setText(right_text);
            tv_right_text.setVisibility(VISIBLE);
        }
        if (right_img_drawable != null) {
            ib_right_img.setImageDrawable(right_img_drawable);
            ib_right_img.setVisibility(VISIBLE);
        }
        setListener();
        addView(view);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        ib_left_img.setOnClickListener(this);
        tv_left_text.setOnClickListener(this);
        tv_right_text.setOnClickListener(this);
        ib_right_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tool_left_img) {
            itemClick.onClick(LEFT_IMG);
        } else if (id == R.id.tool_left_text) {
            itemClick.onClick(LEFT_TEXT);
        } else if (id == R.id.tool_right_text) {
            itemClick.onClick(RIGHT_TEXT);
        } else if (id == R.id.tool_right_img) {
            itemClick.onClick(RIGHT_IMG);
        }
    }

    /**
     * 点击的监听，做相应的操纵
     */
    public interface ToolbarItemClick {
        void onClick(int viewTag);
    }
}
