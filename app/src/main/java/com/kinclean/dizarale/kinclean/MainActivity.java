package com.kinclean.dizarale.kinclean;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.EditTextPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kinclean.dizarale.kinclean.R;
import com.kinclean.dizarale.kinclean.fragment_controller.cart_activity;
import com.kinclean.dizarale.kinclean.fragment_controller.contact_activity;
import com.kinclean.dizarale.kinclean.fragment_controller.history_activity;
import com.kinclean.dizarale.kinclean.fragment_controller.menu_activity;
import com.kinclean.dizarale.kinclean.fragment_controller.menu_show_activity;
import com.kinclean.dizarale.kinclean.service.APP_config;
import com.kinclean.dizarale.kinclean.service.HTTPReq;

import org.parceler.Parcels;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RequestBody formBody;
    int page_id;
    private static final String TAG = "MainActivity";
    APP_config app_config;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    TextView user_name_display;
    TextView user_tel_display;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();

                cart_activity mycart_Fragment = new cart_activity();

                if(app_config.getSdkVersion() > 20){
                    Slide slidecart = new Slide(Gravity.BOTTOM);
                    slidecart.setDuration(500);
                    mycart_Fragment.setEnterTransition(slidecart);
                }



                FragmentTransaction transaction = manager.beginTransaction();
                if(manager.findFragmentById(R.id.layout_fragment_container).getTag()!="mycart"){

                    transaction.add(R.id.layout_fragment_container, mycart_Fragment, "mycart");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    fab.setImageResource(R.drawable.ic_cart_down);
                }else{
                    fab.setImageResource(R.drawable.ic_cart);
                    manager.popBackStack();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        user_name_display= (TextView) headerView.findViewById(R.id.user_name_display);
        user_tel_display= (TextView) headerView.findViewById(R.id.tel_display);


        menu_activity menu_fragment = new menu_activity();

        sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
        String user_name = sp.getString("user_name", "");
        String user_tel = sp.getString("user_tel", "");
        String user_imei = sp.getString("user_imei", "");
        Log.v("user_name1", user_name);
        user_name_display.setText(user_name);
        user_tel_display.setText(user_tel);


        if(user_name==""){
            app_config = new APP_config(this);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            app_config.imei = telephonyManager.getDeviceId();
            Log.v("user_name", app_config.user_name);
            sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("user_imei", app_config.imei);
            editor.commit();
            dialogShow(false);
        }else{
            app_config = new APP_config(this);
            app_config.user_name = user_name;
            app_config.user_tel = user_tel;
            app_config.imei = user_imei;
        }
        
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout_fragment_container, menu_fragment,"menu_tag");
        transaction.commit();

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager manager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(manager.findFragmentById(R.id.layout_fragment_container).getTag()=="menu_tag"){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getBaseContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            app_config = new APP_config(this);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            app_config.imei = telephonyManager.getDeviceId();
            Log.v("user_name", app_config.user_name);
            sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("user_imei", app_config.imei);
            editor.commit();
            dialogShow(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        page_id = id;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if(manager.findFragmentById(R.id.layout_fragment_container).getTag()!="menu_tag"){
            manager.popBackStack();
        }
        if (id == R.id.nav_home) {
            menu_activity menu_fragment = new menu_activity();
            transaction.replace(R.id.layout_fragment_container, menu_fragment,"menu_tag");
            transaction.commit();
            transaction.disallowAddToBackStack();
        } else if (id == R.id.nav_contact) {
            contact_activity contact_fragment = new contact_activity();
            transaction.add(R.id.layout_fragment_container, contact_fragment, "contact_tag");
            transaction.addToBackStack(null);
            transaction.commit();
        }else if (id == R.id.nav_set) {
            app_config = new APP_config(this);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            app_config.imei = telephonyManager.getDeviceId();
            Log.v("user_name", app_config.user_name);
            sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("user_imei", app_config.imei);
            editor.commit();
            dialogShow(true);
        }else if (id == R.id.nav_history) {
            history_activity contact_fragment = new history_activity();
            transaction.add(R.id.layout_fragment_container, contact_fragment, "history_tag");
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_qrscanner) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            //ส่ง Mode ในการ Scan ให้กับ โปรแกรม Barcode Scanner
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            //เริ่ม Activity จาก intent ที่กำหนด โดยกำหนด requestCode เป็น 0
            startActivityForResult(intent, 0);
        }

        Log.v("page", "" + manager.findFragmentById(R.id.layout_fragment_container).getTag());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        if (requestCode == 0) //ทำการตรวจสอบว่า requestCode ตรงกับที่ Barcode Scanner คืนค่ามาหรือไม่
        {
            if (resultCode == RESULT_OK) //ถ้า Barcode Scanner ทำงานสมบูรณ์
            {
                //รับข้อมูลจาก Barcode Scanner ที่ได้จากการสแกน
                String contents = intent.getStringExtra("SCAN_RESULT");
                //รับรูปแบบจาก Barcode Scanner ที่ได้จากการสแกน ว่าเป็นชนิดใด
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //ทำการแสดงผลลัพธ์จากการแสกนใน txtResult
                Log.v("result2 :" , contents);
                formBody = new FormBody.Builder()
                        .add("cus_imei", app_config.imei)
                        .add("cus_tel", app_config.user_tel)
                        .add("cus_name", app_config.user_name)
                        .build();

                String url = contents;
                new AsyncHttpTask().execute(url);
            }
        }
    }

    public void dialogShow(Boolean set){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.set_user_detail);
        final EditText user = (EditText) dialog.findViewById(R.id.user_name);
        final EditText tel = (EditText) dialog.findViewById(R.id.tel);
        TextView u_imei = (TextView) dialog.findViewById(R.id.imei);


        if(set){
            sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            String user_name = sp.getString("user_name", "");
            String user_tel = sp.getString("user_tel", "");
            Log.v("user_name1", user_name);
            user.setText(user_name);
            tel.setText(user_tel);
            u_imei.setText("your imei :" + app_config.imei);
        }
            Button set_btn = (Button) dialog.findViewById(R.id.set_btn);
            set_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String user_str = user.getText().toString();
                    String user_tel = tel.getText().toString();
                    boolean pass1 = false;
                    boolean pass2 = false;
                    if (user_str.matches("")) {
                        user.setError("กรุณากรอกข้อมูล");
                        pass1 = false;
                    } else {
                        pass1 = true;
                    }
                    if (user_tel.matches("")) {
                        tel.setError("กรุณากรอกข้อมูล");
                        pass2 = false;
                    } else {
                        pass2 = true;
                    }
                    if (pass1 && pass2) {
                        sp = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
                        editor = sp.edit();
                        editor.putString("user_name", user_str);
                        editor.putString("user_tel", user_tel);
                        editor.commit();
                        app_config.user_name = user_str;
                        app_config.user_tel = user_tel;
                        dialog.dismiss();
                        user_name_display.setText(user_str);
                        user_tel_display.setText(user_tel);
                        Log.v("sub", app_config.user_name + " - " + app_config.user_tel);

                        APP_config app_config = new APP_config();
                        formBody = new FormBody.Builder()
                                .add("cus_imei", app_config.imei)
                                .add("cus_tel", app_config.user_tel)
                                .add("cus_name", app_config.user_name)
                                .build();

                        String url = app_config.PostUser;
                        new AsyncHttpTask().execute(url);
                    }
                }
            });

        dialog.show();
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            String result = null;
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HTTPReq PostData = new HTTPReq();
            result = PostData.HTTPPOST(url, formBody);
            Log.v("result", url.toString());
            Log.v("result", result);
            if(result == "Fail"){
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {
                Toast.makeText(getApplication(),"เสร็จสิ้น",Toast.LENGTH_LONG).show();
            } else {
                String url = app_config.PostUser;
                new AsyncHttpTask().execute(url);
            }
        }

        public void setProgressBarIndeterminateVisibility(boolean progressBarIndeterminateVisibility) {
            //this.progressBarIndeterminateVisibility = progressBarIndeterminateVisibility;
        }

    }
}
