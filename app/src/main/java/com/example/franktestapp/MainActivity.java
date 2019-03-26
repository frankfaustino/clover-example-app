package com.example.franktestapp;

import android.accounts.Account;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.Item;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Account mAccount;
    private InventoryConnector mInventoryConnector;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mListView = (ListView) findViewById(R.id.inventory_list);

        // Retrieve the Clover account
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                Log.i("WTF", "WTF");
                return;
            }
        }

        // Connect InventoryConnector
        connect();

        // Get Item
        new InventoryAsyncTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private void connect() {
        disconnect();
        if (mAccount != null) {
            mInventoryConnector = new InventoryConnector(this, mAccount, null);
            mInventoryConnector.connect();
        }
    }

    private void disconnect() {
        if (mInventoryConnector != null) {
            mInventoryConnector.disconnect();
            mInventoryConnector = null;
        }
    }

    private class InventoryAsyncTask extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected final List<Item> doInBackground(Void... params) {
            try {
                return mInventoryConnector.getItems();

            } catch (RemoteException | ClientException | ServiceException | BindingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected final void onPostExecute(List<Item> items) {
            if (items != null) {
                ArrayList<String> list = new ArrayList<>();
                for (Item item : items) {
                    list.add(item.getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                mListView.setAdapter(arrayAdapter);
            }
        }
    }
}
