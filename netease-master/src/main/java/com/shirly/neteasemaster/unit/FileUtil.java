package com.shirly.neteasemaster.unit;

import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/13 17:46
 * @description 描述
 */
public class FileUtil {
    // 获取文件内容
    public static String getFileContent( File file, String encoding) {
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = ResourceUtils.getFile("classpath:lua/timeWindowLimit.lua").getPath();
        String result = getFileContent(new File(filePath), "UTF-8");
        System.out.println(result);
    }
}
