package com.yuyh.library.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.yuyh.library.utils.log.LogUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * @author yuyh.
 * @date 16/4/10.
 */
public class Utils {

    public static final String SCHEME_TEL = "tel:";
    protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;


    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void callPhone(final Context context, final String phoneNumber) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null.");
        }
        try {
            final Uri uri = Uri.parse(SCHEME_TEL + phoneNumber);
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * 发送短信息
     *
     * @param phoneNumber 接收号码
     * @param content     短信内容
     */
    private void toSendSMS(Context context, String phoneNumber, String content) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null.");
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();

        if (content.length() >= 70) {
            //短信字数大于70，自动分条
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                //短信发送
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * 获取唯一标识码
     *
     * @param mContext
     * @return
     */
    public synchronized static String getUDID(Context mContext) {
        if (uuid == null) {
            if (uuid == null) {
                final SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);

                if (id != null) {
                    uuid = id;
                } else {
                    final String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        } else {
                            final String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }
            }
        }
        return uuid;
    }
}
