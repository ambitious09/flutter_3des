import 'dart:async';

import 'package:flutter/services.dart';

class Flutter3desPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_3des_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> encrypt(String key, String data) async{
    return await _channel.invokeMethod('encrypt' , <String,dynamic>{'data':data,'key':key});
  }
  static Future<String> deccode( String data,String key) async{
    return await _channel.invokeMethod('deccode' , <String,dynamic>{'data':data,'key':key});
  }
  static Future<String> user_encrypt( String data,String psd_first_key,String psd_secondt_key,String psd_third_key) async{
    return await _channel.invokeMethod('user_encrypt' , <String,dynamic>{'data':data,'first_key':psd_first_key,'second_key':psd_secondt_key,'third_key':psd_third_key});
  }
}
