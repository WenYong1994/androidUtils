package com.yuyh.library.mvp.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yuyh.library.R;


/**
 * created by arvin on 16/10/24 14:47
 * email：1035407623@qq.com
 */
public class BasePresenter {
    private FragmentActivity mActivity;
    private View mRootView;

    public BasePresenter(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    public BasePresenter(FragmentActivity mActivity, View rootView) {
        this.mActivity = mActivity;
        this.mRootView = rootView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        if (mRootView != null) {
            try {
                return (T) mRootView.findViewById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        try {
            return (T) mActivity.findViewById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message) || mActivity == null) {
            return;
        }
        Toast.makeText(mActivity, message, message.length() > 8 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }


    public void startActivity(Class clazz, Bundle bundle, boolean isAnim) {
        Intent intent = new Intent(mActivity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mActivity.startActivity(intent);
        if (isAnim) {
            mActivity.overridePendingTransition(R.anim.ui_right_in, 0);
        }
    }

    public void startActivityForResult(Class clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(mActivity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mActivity.startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.ui_right_in, 0);
    }

    public void onDestroy() {
        mActivity = null;
        mRootView = null;
    }
}
