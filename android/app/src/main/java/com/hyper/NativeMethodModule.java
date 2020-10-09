package com.hyper;

import android.widget.Toast;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.content.Intent;

import android.view.SurfaceHolder.Callback;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.HashMap;

public class NativeMethodModule extends ReactContextBaseJavaModule {
  public static com.facebook.react.bridge.Callback onFail;
  public static com.facebook.react.bridge.Callback onSuccess;
  private static ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

//  private Callback onSuccess;
//  private Callback onFail;


  NativeMethodModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }

  @Override
  public String getName() {
    return "NativeMethodModule";
  }

  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public String greeting(String name) {
    return name;
  }

  @ReactMethod
public void openHyperPay(ReadableMap data, com.facebook.react.bridge.Callback onSuccess, com.facebook.react.bridge.Callback onFail) {
   this.onSuccess = onSuccess;
   this.onFail = onFail;
  Intent intent = new Intent(getCurrentActivity(), HyperpayActivity.class);
  if(data.hasKey("checkoutId")) {
    intent.putExtra("checkoutId", data.getString("checkoutId"));
  }
  getCurrentActivity().startActivity(intent);
}

}