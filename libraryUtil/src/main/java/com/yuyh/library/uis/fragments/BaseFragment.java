package com.yuyh.library.uis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuyh.library.mvp.presenters.BasePresenter;
import com.yuyh.library.mvp.views.IBaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * created by arvin on 16/10/24 15:02
 * email：1035407623@qq.com
 */
public abstract class BaseFragment extends Fragment implements IBaseView {
    protected View mRoot;
    protected CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private BasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = LayoutInflater.from(getActivity()).inflate(getContentViewId(), null);
        unbinder = ButterKnife.bind(this, mRoot);
        mCompositeSubscription = new CompositeSubscription();
        mPresenter = new BasePresenter(getActivity(), mRoot);
        init(savedInstanceState);
        return mRoot;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
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

        mPresenter.startActivity(clazz, null,false);
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle) {
//        mPresenter.startActivity(clazz, bundle,true);
        mPresenter.startActivity(clazz, bundle,false);//由于vivo手机切换动画不友好，所以这里舍弃使用
    }

    @Override
    public void startActivity(Class clazz, boolean isAnim) {
        mPresenter.startActivity(clazz, null,isAnim);
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle, boolean isAnim) {
        mPresenter.startActivity(clazz, bundle,isAnim);
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
    public abstract int getContentViewId();

    @Override
    public abstract void init(Bundle savedInstanceState);
}
