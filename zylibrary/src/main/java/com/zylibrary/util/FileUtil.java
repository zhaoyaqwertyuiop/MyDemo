package com.zylibrary.util;

import android.content.Context;
import android.util.Log;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * 使用FileInputStream和FileOutputStream进行文件操作。
 * 存放在内部存储数据区(/data/data/..)的文件只能使用openFileOutput和openFileInput进行操作。
 */
public class FileUtil {
    public static final String FILENAME_DATA = "data.txt";

    // 保存文件内容
    public static final void writeFiles(String content, String fileName) {
        File temp = new File(x.app().getFilesDir().toString() + File.separator + fileName + ".temp");
        File file = new File(x.app().getFilesDir().toString() + File.separator + fileName);
        try {
            // 打开文件获取输出流，文件不存在则自动创建
            FileOutputStream fos = x.app().openFileOutput(fileName + ".temp", Context.MODE_PRIVATE);
//            FileOutputStream sdFileFos = new FileOutputStream(new File("sdFilePath")); // 外部存储可以这样获取
            fos.write(content.getBytes());
            fos.close();
            temp.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d("FileUtil", "file:" + file.length() + ", temp:" + temp.length());
            temp.delete();
        }
    }

    // 读取文件内容
    public static final String readFiles(String fileName) {
        String content = null;
        try {
            FileInputStream fis = x.app().openFileInput(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            content = baos.toString();
            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /** 从assets读取文件 */
    public String getFromAssets(String fileName){
        String Result="";
        try {
            InputStreamReader inputReader = new InputStreamReader(x.app().getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while((line = bufReader.readLine()) != null)
                Result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    /** 从raw文件中读取 */
    public String getFromRaw(){
        String Result="";
        try {
//            InputStreamReader inputReader = new InputStreamReader(x.app().getResources().openRawResource(R.raw.test1));
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            String line="";
//            while((line = bufReader.readLine()) != null)
//                Result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}
