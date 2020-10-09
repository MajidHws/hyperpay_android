package com.hyper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.util.*;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;

public class HyperpayActivity extends AppCompatActivity {
    String checkoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyperpay);
        Set<String> paymentBrands = new LinkedHashSet<String>();
        paymentBrands.add("VISA");
        paymentBrands.add("MASTER");
        Intent intentGet = getIntent();
        if (intentGet.hasExtra("checkoutId")) {
            checkoutID = intentGet.getStringExtra("checkoutId");
        }

        CheckoutSettings checkoutSettings = new CheckoutSettings(checkoutID, paymentBrands, Connect.ProviderMode.TEST);
        // Set shopper result URL
//        checkoutSettings.setShopperResultUrl("com.hyper://result");
        Intent intent = checkoutSettings.createCheckoutActivityIntent(this);
        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);
    }

    @Override
    protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      if (intent.getScheme().equals("com.hyper")) {
        String checkoutId = intent.getData().getQueryParameter("id");
        Log.d("resourcePath", "onNewIntent: " + checkoutId);
        Log.d("resourcePath", "onNewIntent: " + intent.getData());
        /* request payment status */
      }
    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       switch (resultCode) {
         case CheckoutActivity.RESULT_OK:
           /* transaction completed */
           Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);
           /* resource path if needed */
           String resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);
           Log.d("resourcePath", "onActivityResult: " + resourcePath);
           WritableMap dataMap = Arguments.createMap();
           dataMap.putString("resourcePath", resourcePath);
           NativeMethodModule.onSuccess.invoke(dataMap);
           if (transaction.getTransactionType() == TransactionType.SYNC) {
             /* check the result of synchronous transaction */
           } else {
             /* wait for the asynchronous transaction callback in the onNewIntent() */
           }
           break;
         case CheckoutActivity.RESULT_CANCELED:
           NativeMethodModule.onFail.invoke("There is an error while process payment");
           /* shopper canceled the checkout process */
           break;
          case CheckoutActivity.RESULT_ERROR:
           /* error occurred */
           PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);
           NativeMethodModule.onFail.invoke("Payment Fail");
       }
       finish();
     }
}