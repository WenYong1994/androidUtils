package com.yuyh.library.utils.io;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import android.widget.Toast;

import com.yuyh.library.utils.PackageUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拍照工具类
 * (1) 使用{@link #setOnPhotoResultListener}等回调方法
 * (2) 在调用者所在的Activity或者Fragment的onActivityResult方法中获取操作结果。
 *
 * @author yuyh.
 * @date 16/4/9.
 */
public class PhotoUtils {
    public static class RequestCode {
        public static final int TAKE_PHOTO_BY_SYSTEM = 0;
        public static final int PICK_PHOTO_FROM_GALLERY = 1;
        public static final int CROP_PHOTO_BY_SYSTEM = 2;
    }

    private static Uri tempPhotoUri = null;
    private static Uri tempPhotoCropUri = null;

    private static void startActivityForResult(Context context, Intent intent, int requestCode, Fragment fragment) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent, requestCode);
            }
        } else {
            Toast.makeText(context, "本手机没有安装对应的应用程序", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 照片返回监听接口
     *
     * @author king
     */
    public interface OnPhotoResultListener {
        /**
         * @param photoPath 照片的路径
         * @param photo     照片原文件
         */
        void onPhotoResult(String photoPath, Bitmap photo);
    }

    /**
     * 设置照片回调接口,一般用于只在Activity或Fragment使用了本类中的一种照片操作方法的结果回调
     * 在OnActivityResult里面调用
     *
     * @param context
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        onActivityResult返回的意图
     * @param l
     */
    public static void setOnPhotoResultListener(Context context, int requestCode, int resultCode, Intent data, OnPhotoResultListener l) {
        switch (requestCode) {
            case RequestCode.TAKE_PHOTO_BY_SYSTEM:
                setOnTakePhotoResultListener(resultCode, data, l);
                break;
            case RequestCode.PICK_PHOTO_FROM_GALLERY:
                setOnPickPhotoResultListener(context, resultCode, data, l);
                break;
            case RequestCode.CROP_PHOTO_BY_SYSTEM:
                setOnCropPhotoResultListener(resultCode, data, l);
                break;
        }
    }

    /**
     * 使用系统相机进行拍照
     *
     * @param context
     * @param fragment 若为null则表示在Activity中启动，否则在Fragment中启动F
     */
    public static String takePhotoBySystem(Context context, Fragment fragment) {
        checkPermission();
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = new File(FileUtils.getImageCachePath(), "imgTemp");
//            File photoFile =  getOutputMediaFile(1);
            tempPhotoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(1));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri);
            startActivityForResult(context, intent, RequestCode.TAKE_PHOTO_BY_SYSTEM, fragment);
            String tempPath[] = tempPhotoUri.getPath().split("camera_photos");
            if (tempPath.length > 1) {
                String tempUrl = "/storage/emulated/0/" + tempPath[1];
                return tempUrl;
            }else{
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");

            Log.d("", "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("", "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("",
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * 系统相机拍照回调
     *
     * @param resultCode
     * @param data
     * @param l
     */
    public static void setOnTakePhotoResultListener(int resultCode, Intent data, OnPhotoResultListener l) {
        if (l == null || resultCode != Activity.RESULT_OK || tempPhotoUri == null)
            return;

        String tempPath[] = tempPhotoUri.getPath().split("camera_photos");
        if (tempPath.length > 1) {
            String tempUrl = "/storage/emulated/0/" + tempPath[1];
            l.onPhotoResult(tempPhotoUri.getPath(), BitmapFactory.decodeFile(tempUrl));
        } else {
            l.onPhotoResult(tempPhotoUri.getPath(), BitmapFactory.decodeFile(tempPhotoUri.getPath()));
        }

    }

    /**
     * 从图库中选择照片
     *
     * @param context
     * @param fragment
     */
    public static void pickPhotoFormGallery(Context context, Fragment fragment) {
        checkPermission();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(context, intent, RequestCode.PICK_PHOTO_FROM_GALLERY, fragment);
    }

    /**
     * 系统图库照片选择回调
     *
     * @param context
     * @param resultCode
     * @param data
     * @param l
     */
    public static void setOnPickPhotoResultListener(Context context,
                                                    int resultCode, Intent data, OnPhotoResultListener l) {
        if (context == null || l == null || resultCode != Activity.RESULT_OK)
            return;
        try {
            ContentResolver mContentResolver = context.getContentResolver();
            Bitmap photo = MediaStore.Images.Media.getBitmap(mContentResolver,
                    data.getData());
            // 获取图片路径
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContentResolver.query(data.getData(), proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            l.onPhotoResult(path, photo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统裁剪图片
     *
     * @param context
     * @param fragment
     * @param photoPath 照片的路径
     * @param width     裁剪宽度
     * @param height    裁剪高度
     */
    public static void cropPhotoBySystem(Context context, Fragment fragment, String photoPath, int width, int height) {
        checkPermission();
        try {
            File cropFile = new File(FileUtils.getImageCropCachePath(), "imgCropTemp");
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.fromFile(new File(photoPath)), "image/*");
            tempPhotoCropUri = Uri.fromFile(cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoCropUri);
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
            intent.putExtra("return-data", false);
            intent.putExtra("circleCrop", true);
            startActivityForResult(context, intent, RequestCode.CROP_PHOTO_BY_SYSTEM, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统照片裁剪回调
     *
     * @param resultCode
     * @param data
     * @param l
     */
    public static void setOnCropPhotoResultListener(int resultCode, Intent data, OnPhotoResultListener l) {
        if (data == null || l == null || resultCode != Activity.RESULT_OK)
            return;
        Bitmap photo = data.getParcelableExtra("data");
        l.onPhotoResult(tempPhotoCropUri.getPath(), photo);
    }

    /**
     * 检查所需权限
     */
    private static void checkPermission() {
        if (!PackageUtils.checkPermission("android.permission.CAMERA")) {
            throw new RuntimeException("请在Manifest里面添加android.permission.CAMERA权限");
        }
        if (!PackageUtils.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            throw new RuntimeException("请在Manifest里面添加android.permission.WRITE_EXTERNAL_STORAGE权限");
        }
    }

}
