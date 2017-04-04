package com.tianyu.seelove.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author shisheng.zhao
 * @Description: TextView工具类:用于处理textView显示中一些处理逻辑
 * @date 2015-12-03 下午11:16:13
 */
public class TextUtils {

    /**
     * textview中的字符全角化:即将所有的数字、字母及标点全部转为全角字符，
     * 使它们与汉字同占两个字节，这样就可以避免由于占位导致的排版混乱问题了
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     * 利用正则表达式将所有特殊字符过滤，或利用replaceAll（）将中文标号替换为英文标号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return ToDBC(m.replaceAll("").trim());
    }
}
