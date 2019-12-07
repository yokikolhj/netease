package com.shirly.neteasemaster.function;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/7 16:30
 * @description 中文分词器，按第一个的字去字典中查找，有就继续查找第二个字，没有就返回
 */
public class Tokenizer {
    private Map<Character, Object> dictionary;

    public Tokenizer(String dictionaryFilePath) throws IOException {
        dictionary = new TreeMap<>(); // 红黑树的实现，其key存放的是字典中的第一个字符
        // 从文件加载字典到TreeMap，
        this.loadDictionary(dictionaryFilePath);
    }

    /**
     * 从文件加载字典到TreeMap
     * @param dictionaryFilePath 字典路径
     */
    private void loadDictionary(String dictionaryFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFilePath)));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            char c;
            Map<Character, Object> child = this.dictionary;

            // 组成以这个字符开头的词的树
            for (int i = 0; i < line.length(); i++) {
                c = line.charAt(i);
                Map<Character, Object> cMap = (Map<Character, Object>) child.get(c);
                if (cMap == null) {
                    cMap = new HashMap<Character, Object>();
                    child.put(c, cMap);
                }
                child = cMap;
            }
        }
    }

    /**
     * 分离文本
     * @param text 文本内容
     * @return
     */
    private List<String> participle(String text) {
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.length() == 0) {
            return null;
        }
        List<String> tokens = new ArrayList<>();
        char c;
        for (int i = 0; i < text.length();) {
            StringBuilder token = new StringBuilder();
            Map<Character, Object> child = this.dictionary;
            boolean matchToken = false;
            for (int j = i; j <text.length(); j++) {
                c = text.charAt(j);
                token.append(c);
                Map<Character, Object> cMap = (Map<Character, Object>) child.get(c);
                if (cMap == null) {
                    break;
                }
                if (cMap.isEmpty()){
                    matchToken = true;
                    i = j + 1;
                    break;
                }
                child = cMap;
            }

            if (matchToken) {
                tokens.add(token.toString());
            } else {
                tokens.add("" + text.charAt(i));
                i++;
            }
        }
        return tokens;
    }

    public static void main(String[] args) throws IOException {
        String dictionaryFilePath = ResourceUtils.getFile("classpath:dictionary.txt").getPath();
        System.out.println(dictionaryFilePath);
        Tokenizer tokenizer = new Tokenizer(dictionaryFilePath);
        System.out.println(tokenizer.dictionary);
        List<String> tokens = tokenizer.participle("张三说的确实在理，我爱中国");
        for (String s : tokens) {
            System.out.println(s);
        }
    }
}
