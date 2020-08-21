package com.example.flutter_3des_plugin;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** Flutter3desPlugin */
public class Flutter3desPlugin implements FlutterPlugin, MethodCallHandler {
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_3des_plugin");
        channel.setMethodCallHandler(new Flutter3desPlugin());
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_3des_plugin");
        channel.setMethodCallHandler(new Flutter3desPlugin());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "encrypt":
                String body = call.argument("data");
                String keys = call.argument("key");
                String key = keys + keys.substring(0,16);
                byte [] text = encrypt(hexStr2Bytes(key),hexStr2Bytes(body));
                result.success(bytes2HexStr(text));
                break;
            case "deccode":
                Log.i("tag","加载。。。。。。");
                String decbody = call.argument("data");
                String deckeys = call.argument("key");
                String resultData = null;
                try {
                    resultData = dec(decbody,deckeys);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.success(resultData);
                break;
            case "user_encrypt":
                Log.i("tag","加密。。。。。。");
                String user_body = call.argument("data");
                String first_key = call.argument("first_key");
                String second_key = call.argument("second_key");
                String third_key = call.argument("third_key");
                String ency_Data = null;
                try {
                    String newDataString=  new Base64().encodeToString(user_body.getBytes("UTF-8"));
                    ency_Data = new DesUtil().strEnc(newDataString, first_key, second_key, third_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.success(ency_Data);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    public static String dec(String data,String key) throws Exception{
        String md5Str=getMD5Str(key);
        String s8t16=md5Str.substring(8, 24);
        String s0t8=s8t16.substring(0, 8);
        StringBuilder sBuilder=new StringBuilder();
        sBuilder.append(s8t16);
        sBuilder.append(s0t8);
        try {
            String utf_8=new String(sBuilder.toString().getBytes("gbk"),"utf-8");
//			byte[] by_base64= Base64.getMimeDecoder().decode(data);
            return decode3Des(data, utf_8.getBytes());

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return "";
    }

    public static String decode3Des( String desStr,byte[] key){
        byte[] src = Base64.decode(desStr,Base64.DEFAULT);

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(key, "DESede");
            //解密
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.DECRYPT_MODE, deskey);
//            String pwd = new String(c1.doFinal(src), "utf-8");
//            return c1.doFinal(src);
            return  new String(c1.doFinal(src), "utf-8");
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    public static String getMD5Str(String str) {

        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }


    private static final String algorithm = "DESede";

    // 方法的实现
    public static byte[] encrypt(byte[] key, byte[] body) {
        try {
            SecretKey deskey = new SecretKeySpec(key, algorithm);
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(body);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static byte[] hexStr2Bytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String bytes2HexStr(byte[] data) {
        final StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data)
            stringBuilder.append(String.format("%02X", byteChar));
        return stringBuilder.toString();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }
}
