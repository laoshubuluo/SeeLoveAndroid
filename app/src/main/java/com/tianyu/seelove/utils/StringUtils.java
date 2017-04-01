package com.tianyu.seelove.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author shisheng.zhao
 * @Description: 封装一些常用的字符串处理方法
 * @date 2017-04-01 10:37
 */
public class StringUtils {
    public static boolean isNotBlank(String src) {
        return !isNullOrBlank(src);
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String ListToCommaString(List<String> list) {
        Collections.sort(list);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    public static String NullToString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    public static String digest(String srcStr) {
        MessageDigest md = null;
        String encode = null;
        byte[] bytes = null;
        try {
            md = MessageDigest.getInstance("MD5");
            bytes = md.digest(srcStr.getBytes());
            return bytesToHex(bytes, 0, bytes.length).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();
    }

    private static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

    /**
     * 判断一个字符串是否是手机号码
     * @param str
     * @return
     */
    public static boolean isPhoneNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]{8,11}");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断一个字符串是否为空
     * @param src 需要判断的字符串
     * @return
     */
    public static boolean isNullOrBlank(String src) {
        if (null == src || src.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为群租
     * @param groupId
     * @return
     */
    public static boolean isGroupChat(String groupId) {
        if (null == groupId || "null".equals(groupId) || groupId.trim().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 将集合元素组装成字符串
     * @param collection 需要组装的元素
     * @param seprator   分隔符
     * @return 组装后的字符串
     */
    public static String joinIntegerArray(Collection<Integer> collection,
                                          String seprator) {
        if (collection == null) {
            return "";
        }
        Iterator<Integer> iterator = collection.iterator();
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        while (iterator.hasNext()) {
            if (start) {
                start = !start;
                sb.append(iterator.next());
                continue;
            }
            sb.append(seprator).append(iterator.next());
        }
        return sb.toString();
    }

    /**
     * 将集合元素组装成字符串
     * @param collection 需要组装的元素
     * @param seprator   分隔符
     * @return 组装后的字符串
     */
    public static String join(Collection<String> collection, String seprator) {
        if (collection == null) {
            return "";
        }
        Iterator<String> iterator = collection.iterator();
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        while (iterator.hasNext()) {
            if (start) {
                start = !start;
                sb.append(iterator.next());
                continue;
            }
            sb.append(seprator).append(iterator.next());

        }
        return sb.toString();
    }

    public static boolean getDiffrent(List<String> list1, List<String> list2) {
        return list1.retainAll(list2);
    }

    public static String generateGUID() {
        String source = "abcdefghijklmnopqrstuvwxyz1234567890";
        String target = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            target += source.charAt(random.nextInt(source.length()));
        }
        return target;
    }
}
