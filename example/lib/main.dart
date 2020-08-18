import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_3des_plugin/flutter_3des_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
 
  // _data， _key 数据为测试数据， 实际开发根据项目需求规则生成
  final String _data="sayayfAVDcS8LoY1Gbsy914rGiBSx+zxP8vKmqKB67Fmv9xLQSBGVroMZTKP4Xq5B9kxlCCcGLaeVsf+nndi8D8opnVOG6x5VAGEZ0qYQoAXygaAplHut5jyx3Tghq7/s6Zkl5xQzM5uVpSCL8r+mJb+pGqt9baklzi7Nq3bBJXw7H7hP7IImINTI4Xch+Xv644XN85divHf4019ShRQ9Rw1yDCx3Iw1xPcxdrxoDvRX6pX9eBreThtEBfZBV2aANi+ogGLmHVSx3IAdtJ0MjN9cd8E33S1Jh9IEliETBflOOJMAn1ij2NaLy9kBmcCoT0knld+Akjp1FiQ6zodQ7BE47ps0rR/6j10YPVvpzvZA5CgXU0VNwhOUD9LsT5JBf7cz3/7BIAZHPsWkp8thbLoR2VkInPi+REip43TtrBZ3I2osgu9IxjohYSsKLoqmYxH8qNzbgSLWq5qM+pH8AkH9cfH8G1Xi3EE/mXADGURYYX85MJ/a+wSe0tJ3afeo3JbvdyiEzujVpkvcGYNBxZ2IXeY0VIOeic5JZ6uom0TJYPP3GVKnQGHz0bR+hNADGm8v8hkggZs/a8h+UY8KE2LB1WclQG/0yvOvHgGYhp1QreA+JvhCrmBU/rSBjV7i7A5+5RTafbkecwJLjYWM9h4x6iUev1Ze78isnRqATesgxgSIUjt1sPnUha7h1kJXHpwpBLKgvElLP9YKxCojNEIgMb7PixtZOAPuV4DiXAo=";
  final String _key="TMA2019";
  String _result  = '';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // 3des 加密
  encrypt () {
    Flutter3desPlugin.deccode( _data,_key).then((res) {
      // TODO: res就是加密后的数据
      setState(() {
        _result = res; 
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Flutter3desPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('flutter 3des plugin example'),
        ),
        body: Center(
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              new Text('Running on: $_platformVersion'),
              new Text('加密的data： $_data'),
              new Text('加密的key： $_key'),
              RaisedButton(
              onPressed: (){
                encrypt();
              },
              child: Text("执行3des加密"),
            ),
              new Text('加密的结果： $_result'),
            ],
          ),
        ),
      ),
    );
  }
}
