package com.example.bt_8;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import static com.example.bt_8.my_pref.my_settings_xxx;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    Button btn_connect;
    TextView txt_top;
    TextView txt_bottom;
    Button btn_send_1;

    public static String deviceAddress = "30:AE:A4:38:74:D2";
    public static String deviceName = "ESP32test2";
    public static BluetoothDevice result = null;
    public static BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    public static BluetoothSocket socket;
    public static OutputStream outputStream;
    public static InputStream inputStream;
    public static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Serial Port Service ID
    public static int serial_msg;
    public boolean bt_conn = false;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    Toast.makeText(MainActivity.this, "does nothing", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Intent aaa = new Intent(getApplicationContext(), my_pref.class);
                    startActivity(aaa);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    Intent bbb = new Intent(getApplicationContext(), my_list.class);
                    startActivity(bbb);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //added
        txt_top = (TextView) findViewById(R.id.txt_top);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setBackgroundColor(Color.CYAN);
        txt_bottom = (TextView) findViewById(R.id.txt_bottom);

        //start_up_show_this();
    }
    //===========


    //=======================Button clicks
    @SuppressLint("SetTextI18n")
    public void buttonOnClick(View view) {
        int the_id = view.getId();
        if (the_id == R.id.btn_connect) {
//Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show();
            connect_bt_now();
        }
        if (the_id == R.id.btn_send_1) {
            send_data_now("a");
            //Toast.makeText(this, "send data now", Toast.LENGTH_SHORT).show();
        }
        if (the_id == R.id.btn_send_2) {
            send_data_now("b");
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            txt_bottom.setText("btn_2 = "+mydate + System.getProperty ("line.separator") + txt_bottom.getText().toString());
            //had to click alt+enter a few times but now more warning !!
            //Toast.makeText(this, "222", Toast.LENGTH_SHORT).show();
        }
    }//end button clicks
//use fold selection to hide comments !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//************ Copied Code ************
//<<<$$$ - place up top
//--           bare essentials
//Button btn_connect;
//TextView txt_top;
//Button btn_send_1;
//txt_top = (TextView) findViewById(R.id.txt_top);
//btn_connect = (Button) findViewById(R.id.btn_connect);
//btn_connect.setBackgroundColor(Color.CYAN);


//---------place in manifest
//<uses-permission android:name="android.permission.BLUETOOTH" />
//<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
//possibly add the location permission as advised in Android documentation

    // public static String deviceAddress = "30:AE:A4:38:74:D2";
//    public static String deviceName = "ESP32test2";
//    public static BluetoothDevice result = null;
//    public static BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//    public static BluetoothSocket socket;
//    public static OutputStream outputStream;
//    public static InputStream inputStream;
//    public static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Serial Port Service ID
//    public static int serial_msg;
//    public boolean bt_conn = false;
//<EASY copy> - send_data("a");
    @SuppressLint("SetTextI18n")
    public void connect_bt_now(){
//************ Copied Code ************
        if (bt_conn == false){
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            if (devices != null) {
                for (BluetoothDevice device : devices) {
                    //if (deviceAddress.equals(device.getAddress())) {
                    if (deviceName.equals(device.getName())) {
                        Toast.makeText(getApplicationContext(),"ESP32 Pairing Found",Toast.LENGTH_SHORT).show();
                        txt_top.setText("ESP32 Pairing Found" + System.getProperty ("line.separator") + txt_top.getText().toString());
                        result = device;
                        boolean connected=true;
                        try {
                            socket = result.createRfcommSocketToServiceRecord(PORT_UUID);
                            socket.connect();
                            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                            txt_top.setText("Connected" + System.getProperty ("line.separator") + txt_top.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            connected=false;
                            Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
                            txt_top.setText("Connection Failed" + System.getProperty ("line.separator") + txt_top.getText().toString());
                        }
                        if(connected)//put indicator
                        {
                            //will crash (without warning if button is not defined)
                            btn_connect.setBackgroundColor(Color.GREEN);
                            //conn_ttl.setVisibility(View.VISIBLE);
                            //srch_ttl.setVisibility(View.INVISIBLE);
                            //splash_btn.setEnabled(true);
                            //splash_btn.setBackgroundResource(R.drawable.btn_splash_nor);
                            try {outputStream=socket.getOutputStream();
                            }catch (IOException e) {e.printStackTrace();}
                            try {inputStream=socket.getInputStream();} catch (IOException e){e.printStackTrace();}
                            bt_conn = true;}break;
                    }
                }
            }
        }
    }
    //<EASY copy> send_data_now("a");
    public void send_data_now(String data_to_send){
        // String data_to_send = "a";
        if (socket!=null) {
            try {
                socket.getOutputStream().write(data_to_send.getBytes());
                //Toast.makeText(getApplicationContext(), "Send: "+data_to_send, Toast.LENGTH_SHORT).show();
                txt_top.setText("Send: "+data_to_send + " = Success"+System.getProperty ("line.separator") + txt_top.getText().toString());
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Post Error",Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(getApplicationContext(),"Not Connected to Device",Toast.LENGTH_SHORT).show();
    }
//************ end of Copied Code ************


    //==========
    public void start_up_show_this(){
        //do this 1x I guess since its declared as Public Static
        //public static final String my_settings_xxx = "MyPrefsFile";
        //import static com.example.laowai.bt_with_pref.pref_simple.my_settings_xxx;(i clicked alt enter)
        SharedPreferences prefs = getSharedPreferences(my_settings_xxx, MODE_PRIVATE);
        //
        String temp_xxx = prefs.getString("key_first_name","default_x");

        //defined the button 2x need to fix this.
        Button b1_b1 = (Button)findViewById(R.id.btn_send_1);
        b1_b1.setText(temp_xxx);

        temp_xxx = prefs.getString("key_url","default_x");
        mTextMessage.setText(temp_xxx);

        //Check if device is still connected
        if(bt_conn != false){
            txt_top.setText("Device still connected !!"+System.getProperty ("line.separator") + txt_top.getText().toString());
        }else {
            txt_top.setText("Device NOT connected "+System.getProperty ("line.separator") + txt_top.getText().toString());
        }
    }

    //================


}
