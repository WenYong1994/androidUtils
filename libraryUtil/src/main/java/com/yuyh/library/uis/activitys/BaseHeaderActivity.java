package com.yuyh.library.uis.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yuyh.library.R;

/**
 * created by arvin on 16/10/24 15:22
 * email：1035407623@qq.com
 * activity必须包含af_layout_header.xml文件,或者使用id:pre_tv_title和pre_v_back
 */
public abstract class BaseHeaderActivity extends BaseActivity {

    protected TextView tvTitle;
    protected View vBack;

    @Override
    public void init(Bundle savedInstanceState) {
        initHeader();
        initViews(savedInstanceState);
    }

    private void initHeader() {
        try {
            tvTitle = getView(R.id.pre_tv_title);
            tvTitle.setText(getTitleText());
            tvTitle.setOnClickListener(this);
        } catch (Exception e) {
            Logger.w("未设置标题id~");
        }

        try {
            vBack = getView(R.id.pre_v_back);
            vBack.setOnClickListener(this);
        } catch (Exception e) {
            Logger.w("未设置返回图标id~");
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.pre_tv_title) {
            onTitleClicked(view);
            return;
        }else if(view.getId() == R.id.pre_v_back){
            onBackPressed();
        }
    }

    protected void onTitleClicked(View view) {
    }

    protected abstract String getTitleText();

    protected abstract void initViews(Bundle savedInstanceState);
}
