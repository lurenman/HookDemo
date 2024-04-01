package com.example.hookdemo.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author Zhenxi on 2020-12-14
 */
public class RootUtils {

    private static String LOG_TAG = RootUtils.class.getName();

    public static boolean isSuFileExists() {
        File suFile = new File("/system/bin/su");
        boolean exists = suFile.exists();
        Log.e(LOG_TAG, "isSuFileExists:" + exists);
        return exists;
    }

    public static boolean checkGetRootAuth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (Throwable e) {
                Log.e(LOG_TAG, "exec error  " + e);
                e.printStackTrace();
            }

            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                Log.e(LOG_TAG, "checkGetRootAuth  ture");

                return true;
            } else {
                return false;
            }
        } catch (Throwable e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //写文件
    public static Boolean writeFile(String fileName, String message) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //读文件
    public static String readFile(String fileName) {
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            while ((len = fis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            String result = new String(bos.toByteArray());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> executeCommand(String[] shellCmd) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            Log.i(LOG_TAG, "to shell exec which for find su :");
            localProcess = Runtime.getRuntime().exec(shellCmd);
        } catch (Exception e) {
            return null;
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                Log.i(LOG_TAG, "–> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "–> Full response was: " + fullResponse);
        return fullResponse;
    }


    public static synchronized boolean checkBusybox() {
        try {
            Log.i(LOG_TAG, "to exec busybox df");
            String[] strCmd = new String[]{"busybox", "df"};
            ArrayList<String> execResult = executeCommand(strCmd);
            if (execResult != null) {
                Log.e(LOG_TAG, "check Busybox  ture");
                return true;
            } else {
                Log.i(LOG_TAG, "execResult=null");
                return false;
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }

    /**
     * 检测data读取权限
     *
     * @return
     */
    public static synchronized boolean checkAccessRootData() {
        try {
            String fileContent = "test_ok";
            Boolean writeFlag = writeFile("/data/su_test", fileContent);
            if (writeFlag) {
            } else {
            }

            String strRead = readFile("/data/su_test");
            Log.i(LOG_TAG, "strRead=" + strRead);
            if (fileContent.equals(strRead)) {
                Log.e(LOG_TAG, "checkAccessRootData  ture");

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        }
    }


    public static boolean checkDeviceDebuggable() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            Log.i(LOG_TAG, "buildTags=" + buildTags);
            Log.d(LOG_TAG, "checkDeviceDebuggable  ture");
            return true;
        }
        return false;
    }


    /**
     * 特征文件 1
     *
     * @return
     */
    public static boolean checkSuperuserApk() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                Log.e(LOG_TAG, "checkSuperuserApk  ture");
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean isDeviceRooted() {
//        if (checkDeviceDebuggable()) {
//            return true;
//        }//check buildTags
//        if (checkSuperuserApk()) {
//            return true;
//        }//Superuser.apk
//        if (checkBusybox()) {
//            return true;
//        }//find su use 'which'
//        if (checkAccessRootData()) {
//            return true;
//        }//find su use 'which'
        if (isSuFileExists()) {
            return true;
        }
//        if (checkGetRootAuth()) {
//            return true;
//        }//exec su

        return false;
    }
}
