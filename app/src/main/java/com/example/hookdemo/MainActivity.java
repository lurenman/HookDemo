package com.example.hookdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hookdemo.common.JNIHelper;
import com.example.hookdemo.util.MD5Utils;
import com.example.hookdemo.util.MacAddressUtils;
import com.example.hookdemo.util.RootUtils;
import com.example.hookdemo.util.SandhookUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("hookdemo");
    }

    private Button btn_native_test;
    private Button btn_native_add;
    private Button btn_native_RegisterNativeTest;
    private Button btn_native_InlineHook;
    private Button btn_root_check;
    private Button btn_native_xhook;
    private Button btn_native_InlineAsm;
    private Button btn_native_mac_address;
    private Button btn_sandhook;
    private Button btn_sandhook_native;

    native String stringFromJNI();

    private static final String TAG = "L_TEST";
    private Context mContext;
    private Button btn_click;
    private Button btn_md5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        btn_click = (Button) findViewById(R.id.btn_click);
        btn_md5 = (Button) findViewById(R.id.btn_md5);
        btn_native_test = (Button) findViewById(R.id.btn_native_test);
        btn_native_add = (Button) findViewById(R.id.btn_native_add);
        btn_root_check = (Button) findViewById(R.id.btn_root_check);
        btn_native_RegisterNativeTest = (Button) findViewById(R.id.btn_native_RegisterNativeTest);
        btn_native_InlineHook = (Button) findViewById(R.id.btn_native_InlineHook);
        btn_native_xhook = (Button) findViewById(R.id.btn_native_xhook);
        btn_native_InlineAsm = (Button) findViewById(R.id.btn_native_InlineAsm);
        btn_native_mac_address = (Button) findViewById(R.id.btn_native_mac_address);
        btn_sandhook = (Button) findViewById(R.id.btn_sandhook);
        btn_sandhook_native = (Button) findViewById(R.id.btn_sandhook_native);
        initEvent();
    }

    private void initEvent() {
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: test");
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        });
        btn_md5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plainText = "hello c++";
                String md5Str = MD5Utils.stringToMD5(plainText);
                Toast.makeText(mContext, md5Str, Toast.LENGTH_SHORT).show();
            }
        });
        btn_native_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = stringFromJNI();
                Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
            }
        });
        btn_native_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = JNIHelper.add(1, 2);
                Toast.makeText(mContext, "add result:" + result, Toast.LENGTH_SHORT).show();
            }
        });
        btn_root_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean deviceRooted = RootUtils.isDeviceRooted();
                Toast.makeText(mContext, "isRoot:" + deviceRooted, Toast.LENGTH_SHORT).show();
            }
        });
        btn_native_RegisterNativeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JNIHelper.RegisterNativeTest("hello");
            }
        });
        btn_native_InlineHook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JNIHelper.InlineHook();
            }
        });
        btn_native_xhook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JNIHelper.XHook();
            }
        });
        btn_native_InlineAsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JNIHelper.InlineAsm();
            }
        });
        btn_native_mac_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String macAddress = MacAddressUtils.getMacAddress();
                Toast.makeText(mContext, "macAddress:" + macAddress, Toast.LENGTH_SHORT).show();
            }
        });
        btn_sandhook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SandhookUtils.hook(mContext);
            }
        });
        btn_sandhook_native.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JNIHelper.Sandhook();
            }
        });
    }

}