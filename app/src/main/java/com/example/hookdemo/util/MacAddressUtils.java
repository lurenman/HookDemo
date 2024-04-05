package com.example.hookdemo.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 创建日期：2024-04-05
 * 作者:baiyang
 */
public class MacAddressUtils {
    private static final String TAG = "MacAddressUtils";

    /**
     * setenforce 1|0 命令快速seLinux 模式切换成0 可以访问到
     * 可以执行getenforce命令查看
     * https://www.cnblogs.com/kelelipeng/p/10371593.html
     *
     * @return
     */
    public static String getMacAddress() {
        File file = new File("/sys/class/net/p2p0/address");
        //File file = new File("/sys/class/net/wlan0/address");
        boolean exists = file.exists();
        Log.d(TAG, "getAddress: file exists:" + exists);
        String macAddress = "";
        if (exists) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // 打印MAC地址
                    macAddress = macAddress + line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return macAddress;
    }
}
