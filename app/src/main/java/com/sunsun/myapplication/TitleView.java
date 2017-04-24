package com.sunsun.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pengguolin on 2016/4/11.
 */
public class TitleView extends RelativeLayout {
    public static final int ID_LEFT = R.id.tv_left;
    public static final int ID_RIGHT = R.id.tv_right;
    public static final int ID_RIGHT_SECONDARY = R.id.view_title_right;
    public int size;//图标边长
    private Context context;
    private int imgWidth;

    @Bind(R.id.layout_title)
    public RelativeLayout layoutTitle;
    @Bind(R.id.tv_padding)
    TextView tvPadding;
    @Bind(R.id.txt_title)
    public TextView txtTitle;

    @Bind(R.id.txt_left)
    TextView tvLeft;

    @Bind(R.id.txt_right)
    TextView tvRight;

    private ImageView imgLeft;
    private ImageView imgRight;

    private ImageView imgSecRight;
    private ProgressBar progressBar;
    private boolean aboveKitkat = false;
    private boolean fullScreen = true;
    private int statusBarHeight;

    private ImageView ivTitleRightIcon;

    public TitleView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.common_title, this);
        ButterKnife.bind(this);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.common_title, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        String titleText = ta.getString(R.styleable.TitleView_titleText);
        setTitle(titleText);
        fullScreen = ta.getBoolean(R.styleable.TitleView_fullScreen, true);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && fullScreen) {
//            aboveKitkat = true;
//            statusBarHeight = UIUtils.getStatusBarHeight(getContext());
//            tvPadding.getLayoutParams().height = statusBarHeight;
//        }

        size = (int) context.getResources().getDimension(R.dimen.title_height_default);
        imgWidth = (int) context.getResources().getDimension(R.dimen.title_width_default);

        int drawableLeft = ta.getResourceId(R.styleable.TitleView_leftImage, 0);
        if (drawableLeft != 0) {
            LayoutParams params1 = new LayoutParams(imgWidth, size);
            params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imgLeft = addImageButton(drawableLeft, params1);
            imgLeft.setId(ID_LEFT);
            setOnLeftClickListener(null);
        }

        int drawableRight = ta.getResourceId(R.styleable.TitleView_rightImage, 0);
        if (drawableRight != 0) {
            addRightIcon(drawableRight);
        }

        ta.recycle();

        ivTitleRightIcon = (ImageView) findViewById(R.id.txt_title_right_icon);

    }

    private ImageView imgSecondLeft = null;

    public void setImgSecondLeft(int id, OnClickListener listener) {
        if (id != 0 && imgLeft != null) {
            LayoutParams params1 = new LayoutParams(imgWidth, size);
            params1.addRule(RelativeLayout.RIGHT_OF, imgLeft.getId());
            imgSecondLeft = addImageButton(id, params1);
            imgSecondLeft.setOnClickListener(listener);
        }
    }

    public void setImgSecondVisible(int visible) {
        if (imgSecondLeft != null) {
            imgSecondLeft.setVisibility(visible);
        }
    }

    /**
     * 设置标题文本旁 的icon
     */
    public void setTitleRightIcon(int id) {
        if (ivTitleRightIcon != null) {
            ivTitleRightIcon.setVisibility(View.VISIBLE);
            ivTitleRightIcon.setImageResource(id);
        }
    }

    /**
     * 设置标题文本旁 icon 的点击监听
     */
    public void setTitleRightClickListener(View.OnClickListener listen) {
        if (ivTitleRightIcon != null) {
            ivTitleRightIcon.setOnClickListener(listen);
        }
    }

    @Override
    public void setAlpha(float alpha) {
        tvPadding.setAlpha(alpha);
        layoutTitle.setAlpha(alpha);
    }

    public void setOnLeftClickListener(final OnClickListener l) {
        if (imgLeft != null) {
            imgLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (l != null) {
                        l.onClick(v);
                    } else if (context instanceof Activity) {//点击返回
                        ((Activity) (context)).onBackPressed();
                    }
                }
            });
        }
    }

    public void setLeftTextView(String text, OnClickListener listener) {
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(text);
        tvLeft.setOnClickListener(listener);
    }

    public void setRightTextView(String text, OnClickListener listener) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
        tvRight.setOnClickListener(listener);
    }

    public void setOnRightClickListener(OnClickListener l) {
        if (imgRight != null) {
            imgRight.setOnClickListener(l);
        }
    }

    public void setRightImageVisibility(int visibility) {
        if (imgRight != null) {
            imgRight.setVisibility(visibility);
        }
    }

    public ImageView getImgRight() {
        if (imgRight == null) {
            LayoutParams params2 = new LayoutParams(imgWidth, size);
            params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imgRight = addImageButton(-1, params2);
            imgRight.setId(ID_RIGHT);
        }
        return imgRight;
    }


    public ImageView getImgSecRight() {
        return imgSecRight;
    }


    public void setRightSecImageVisibility(int visibility) {
        if (imgSecRight != null) {
            imgSecRight.setVisibility(visibility);
        }
    }

    public void setLeftImageVisibility(int visibility) {
        if (imgLeft != null) {
            imgLeft.setVisibility(visibility);
        }
    }

    public void setRightImageResource(int id) {
        if (imgRight != null) {
            imgRight.setImageResource(id);
        }
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void addRightIcon(int resId, OnClickListener l) {
        if (imgRight == null) {
            addRightIcon(resId);
        }
        imgRight.setOnClickListener(l);
    }

    public void setRightSecondaryIcon(int resId, OnClickListener l) {
        if (imgSecRight != null)
            return;
        LayoutParams params = new LayoutParams(imgWidth, size);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(LEFT_OF, ID_RIGHT);
        imgSecRight = addImageButton(resId, params);
        imgSecRight.setId(ID_RIGHT_SECONDARY);
        imgSecRight.setOnClickListener(l);
    }

    private void addRightIcon(int drawableResId) {
        LayoutParams params2 = new LayoutParams(imgWidth, size);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imgRight = addImageButton(drawableResId, params2);
        imgRight.setId(ID_RIGHT);
    }

    /**
     * 在标题栏上动态生成图标按钮
     *
     * @return
     */
    public ImageView addImageButton(int resId, LayoutParams params) {
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (resId > 0) {
            img.setImageResource(resId);
        }
        if (aboveKitkat) {
            params.setMargins(0, statusBarHeight, 0, 0);
        }
        addView(img, params);
        return img;
    }

}
