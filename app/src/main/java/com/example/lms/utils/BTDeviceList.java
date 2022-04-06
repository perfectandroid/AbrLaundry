package com.example.lms.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.lms.R;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class BTDeviceList extends ListActivity {

    static public final int REQUEST_CONNECT_BT = 0x2300;

    static private final int REQUEST_ENABLE_BT = 0x1000;

    static private BluetoothAdapter mBluetoothAdapter = null;

    static private ArrayAdapter<String> mArrayAdapter = null;

    static private ArrayAdapter<BluetoothDevice> btDevices = null;
    private String ErrorMessageToUser="";
    private Boolean shouldFinish=true;
    private static final UUID SPP_UUID = UUID
            .fromString("00001105-0000-1000-8000-00805F9B34FB");
    // UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    static private BluetoothSocket mbtSocket = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Bluetooth Devices");

        try {
            if (initDevicesList() != 0) {
                this.finish();
                return;
            }

        } catch (Exception ex) {
            this.finish();
            return;
        }

        IntentFilter btIntentFilter = new IntentFilter(
                BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver, btIntentFilter);
    }

    public static BluetoothSocket getSocket() {
        return mbtSocket;
    }

    private void flushData() {
        try {
            if (mbtSocket != null) {
                mbtSocket.close();
                mbtSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            if (btDevices != null) {
                btDevices.clear();
                btDevices = null;
            }

            if (mArrayAdapter != null) {
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
                mArrayAdapter.notifyDataSetInvalidated();
                mArrayAdapter = null;
            }

            finalize();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private int initDevicesList() {

        flushData();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth not supported!!", Toast.LENGTH_LONG).show();
            return -1;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1);

        setListAdapter(mArrayAdapter);

        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        try {
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } catch (Exception ex) {
            return -2;
        }

        Toast.makeText(getApplicationContext(),
                "Getting all available Bluetooth Devices", Toast.LENGTH_SHORT)
                .show();

        return 0;

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);

        switch (reqCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {
                    Set<BluetoothDevice> btDeviceList = mBluetoothAdapter
                            .getBondedDevices();
                    try {
                        if (btDeviceList.size() > 0) {

                            for (BluetoothDevice device : btDeviceList) {
                                if (btDeviceList.contains(device) == false) {

                                    btDevices.add(device);

                                    mArrayAdapter.add(device.getName() + "\n"
                                            + device.getAddress());
                                    mArrayAdapter.notifyDataSetInvalidated();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),
                                ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

        mBluetoothAdapter.startDiscovery();

    }

    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                try {
                    if (btDevices == null) {
                        btDevices = new ArrayAdapter<BluetoothDevice>(
                                getApplicationContext(), android.R.id.text1);
                    }

                    if (btDevices.getPosition(device) < 0) {
                        btDevices.add(device);
                        mArrayAdapter.add(device.getName() + "\n"
                                + device.getAddress() + "\n" );
                        mArrayAdapter.notifyDataSetInvalidated();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, final int position,
                                   long id) {
        super.onListItemClick(l, v, position, id);

        if (mBluetoothAdapter == null) {
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        Toast.makeText(
                getApplicationContext(),
                "Please wait... Connecting to " + btDevices.getItem(position).getName() + ","
                        + btDevices.getItem(position).getAddress(),
                Toast.LENGTH_LONG).show();
        print(position);


    }

    private void print(final int position) {
        Thread connectThread = new Thread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void run() {
                //for (int i = 1; i <= 2; i++) {
                try {
                    boolean gotuuid = btDevices.getItem(position)
                            .fetchUuidsWithSdp();

                        UUID uuid = btDevices.getItem(position).getUuids()[0]
                                .getUuid();
                        mbtSocket = btDevices.getItem(position)
                                .createRfcommSocketToServiceRecord(uuid);

                    mBluetoothAdapter.cancelDiscovery();
                    mbtSocket.connect();

                } catch (IOException ex) {
                    shouldFinish=false;
                    ErrorMessageToUser = ex.getMessage();
                    runOnUiThread(socketErrorRunnable);
                    try {
                        mbtSocket.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                        shouldFinish=false;
                    }
                    mbtSocket = null;
                    return;
                } catch (Exception ee){
                    shouldFinish=false;
                    ErrorMessageToUser += ee.getMessage()+ee.getLocalizedMessage()+ee.toString();
                    mBluetoothAdapter.startDiscovery();
                    runOnUiThread(socketErrorRunnable);
                }finally {
                    if(shouldFinish){
                        finish();
                    }
                }
            }
            //}
        });

        connectThread.start();

    }

    private Runnable socketErrorRunnable = new Runnable() {

        @Override
        public void run() {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(BTDeviceList.this);
            builder1.setTitle(R.string.app_name);

            builder1.setMessage("Your bluetooth printer is not ready to connect. Please restart your printer or pair the device again.");
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mBluetoothAdapter.startDiscovery();
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, Menu.FIRST, Menu.NONE, "Refresh Scanning");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case Menu.FIRST:
                initDevicesList();
                break;
        }

        return true;
    }
}
