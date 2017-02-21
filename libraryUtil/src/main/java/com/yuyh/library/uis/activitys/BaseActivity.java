package com.yuyh.library.uis.activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuyh.library.R;
import com.yuyh.library.mvp.presenters.BasePresenter;
import com.yuyh.library.mvp.views.IBaseView;

import butterknife.ButterKnife;

/**
 * created by arvin on 16/10/24 14:55
 * email：1035407623@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private BasePresenter mPresenter;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        mPresenter = new BasePresenter(this);
        init(savedInstanceState);
    }

    public void showProgress(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message != null ? message
                    : "处理中...");
            progressDialog.show();
        } else {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(message != null ? message
                    : "处理中...");
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public final <T extends View> T getView(int id) {
        return mPresenter.getView(id);
    }

    @Override
    public void showToast(String message) {
        mPresenter.showToast(message);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void startActivity(Class clazz) {
        mPresenter.startActivity(clazz, null, true);
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle) {
//        mPresenter.startActivity(clazz, bundle, true);
        mPresenter.startActivity(clazz, bundle, false);//由于vivo的页面切换不友好，这里舍弃
    }

    @Override
    public void startActivity(Class clazz, boolean isAnim) {
//        mPresenter.startActivity(clazz, null, isAnim);
        mPresenter.startActivity(clazz, null, false);//由于vivo的页面切换不友好，这里舍弃
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle, boolean isAnim) {
//        mPresenter.startActivity(clazz, bundle, isAnim);
        mPresenter.startActivity(clazz, bundle, false);//由于vivo的页面切换不友好，这里舍弃
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode) {
        mPresenter.startActivityForResult(clazz, requestCode, null);
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode, Bundle bundle) {
        mPresenter.startActivityForResult(clazz, requestCode, bundle);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.af_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public abstract int getContentViewId();

    @Override
    public abstract void init(Bundle savedInstanceState);
}
