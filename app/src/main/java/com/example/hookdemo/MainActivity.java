package com.example.hookdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hookdemo.common.JNIHelper;
import com.example.hookdemo.util.MD5Utils;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("hookdemo");
    }

    private Button btn_native_test;
    private Button btn_native_add;

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
    }

}