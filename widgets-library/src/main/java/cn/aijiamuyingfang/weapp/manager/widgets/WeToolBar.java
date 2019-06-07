package cn.aijiamuyingfang.weapp.manager.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.aijiamuyingfang.weapp.manager.commons.R;


/**
 * Created by pc on 2018/4/1.
 */
@SuppressLint("RestrictedApi")
public class WeToolBar extends Toolbar {
    private final Context mContext;

    private View mView;
    private TextView mTextTitle;
    private TextView mSearchView;
    private Button mRightButton;
    private ImageButton mLeftButton;

    public WeToolBar(Context context) {
        this(context, null);
    }

    public WeToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
        setContentInsetsRelative(10, 10);

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.WeToolBar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.WeToolBar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.WeToolBar_isShowSearchView,
                    false);

            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }

            CharSequence rightButtonText = a.getText(R.styleable.WeToolBar_rightButtonText);
            if (rightButtonText != null) {
                setRightButtonText(rightButtonText);
            }

            boolean isShowLeft = a.getBoolean(R.styleable.WeToolBar_isShowLeftIcon, false);
            if (isShowLeft) {
                showLeftButton();
            } else {
                mLeftButton.setVisibility(GONE);
            }

            a.recycle();
        }
    }

    private void initView() {
        if (mView == null) {
            LayoutInflater mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightButton = mView.findViewById(R.id.toolbar_rightButton);
            mLeftButton = mView.findViewById(R.id.toolbar_leftButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon) {

        if (mRightButton != null) {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIcon(int icon) {
        setRightButtonIcon(mContext.getDrawable(icon));
    }


    public void setRightButtonOnClickListener(OnClickListener li) {
        mRightButton.setOnClickListener(li);
    }

    public void setRightButtonText(CharSequence text) {
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id) {
        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton() {
        return this.mRightButton;
    }


    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showLeftButton() {
        mLeftButton.setVisibility(View.VISIBLE);
        mLeftButton.setOnClickListener(view -> {
            ((Activity) mContext).finish();
            ((Activity) mContext).overridePendingTransition(0, 0);
        });
    }


    public void showSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);
    }


    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

}
