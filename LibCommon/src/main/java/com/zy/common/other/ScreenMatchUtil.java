package com.zy.common.other;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ScreenMatchUtil {

    // 计算最小宽度限定符
    public static int getSmallestScreenWidthDp(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float widthDp = metrics.widthPixels / metrics.density;
        int smallestScreenWidthDp = context.getResources().getConfiguration().smallestScreenWidthDp;
        // 如果计算出的最小宽度与系统配置的不一致，使用系统配置的值
        return smallestScreenWidthDp > 0 ? smallestScreenWidthDp : (int) widthDp;
    }

    public static void main(double targetSize) {
        // 假设1728p屏幕的基准大小为720dp，您需要根据实际情况调整这个值
        double baseSize = 720.0;
//        double targetSize = 1728.0;

        StringBuilder xmlContent = new StringBuilder();
        xmlContent.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        xmlContent.append("<resources>\n");

        for (int i = 1; i <= 100; i++) { // 假设我们有100个不同的尺寸需要生成
            double sizeInDp = i * 1.0; // 这里i*1.0是示例，您需要根据实际的dimens值来计算
            double scaledSize = (sizeInDp / baseSize) * targetSize;
            xmlContent.append(String.format("<dimen name=\"dimen_%1$d\">%2$.2fdp</dimen>\n", i, scaledSize));
        }
        xmlContent.append("</resources>");

        // 将生成的XML内容写入文件
        writeFile("path/to/your/res/values-sw" + (int)(targetSize / baseSize) + "dp/dimens_fhd.xml", xmlContent.toString());
    }

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

