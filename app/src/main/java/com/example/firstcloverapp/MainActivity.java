package com.example.firstcloverapp;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.merchant.MerchantConnector;

public class MainActivity extends AppCompatActivity {

    private Account mAccount;
    private MerchantConnector mMerchantConnector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                return;
            }
        }
        connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private void connect() {
        mMerchantConnector.disconnect();

        if (mAccount != null) {
            Log.i("DOES THIS WORK?", "HALP!?");
            mMerchantConnector = new MerchantConnector(this, mAccount, null);
            mMerchantConnector.connect();
        }
    }

    private void disconnect() {
        if (mMerchantConnector != null) {
            mMerchantConnector.disconnect();
            mMerchantConnector = null;
        }
    }
}
