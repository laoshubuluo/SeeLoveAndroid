package com.tianyu.seelove.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

import com.tianyu.seelove.R;
import com.tianyu.seelove.dao.SeeLoveDao;
import com.tianyu.seelove.model.entity.message.ChatEmoji;

/**
 * 表情转化工具类
 *
 * @author shisheng.zhao
 * @date 2015-09-01 18:00:22
 */
public class FaceConversionUtils {
    // 每一页表情的个数
    private int pageSize = 20;
    private static FaceConversionUtils mFaceConversionUtil;
    // 保存于内存中的表情HashMap
    private HashMap<String, String> emojiMap = new HashMap<String, String>();
    // 保存于内存中的表情集合
    private List<ChatEmoji> emojis = new ArrayList<ChatEmoji>();
    // 表情分页的结果集合
    public List<List<ChatEmoji>> emojiLists = new ArrayList<List<ChatEmoji>>();

    private static String validInputPattern = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\u3000-\\u301e\\ufe10-\\ufe19\\ufe30-\\ufe44\\ufe50-\\ufe6b\\uff01-\\uffeea-zA-Z0-9\\\\s]";

    private FaceConversionUtils() {
    }

    public static FaceConversionUtils getInstace() {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceConversionUtils();
        }
        return mFaceConversionUtil;
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str) {
        return getExpressionString(context, str, true);
    }

    public SpannableString getExpressionString(Context context, String str,
                                               boolean session) {
        SpannableString spannableString = null;
        try {
            spannableString = new SpannableString(str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getExpressionString", "str is null");
        }
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern

//        for(int i=0;i<str.length();i++){
//            String input=str.substring(i,i+1);
//            if(!isText(input)){
//                String output=string2Unicode(input);
//            }
//
//        }


        Pattern sinaPatten = Pattern.compile(validInputPattern);

        // Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            if (session) {
                dealExpression(context, spannableString, sinaPatten, 0, 20);
            } else {
                dealExpression(context, spannableString, sinaPatten, 0);
            }
        } catch (Exception e) {
            Log.e("dealExpression",
                    e.getMessage() == null ? "" : e.getMessage());
        }
        return spannableString;
    }


    private boolean isText(Pattern textPattern, String input) {
        //Pattern textPattern=Pattern.compile(validInputPattern);
        Matcher matcher = textPattern.matcher(input);
        return matcher.find();
    }

    private String string2Unicode(String string) {


        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
// 取出每一个字符
            char c = string.charAt(i);
// 转换为unicode 添另\\u 后面的字符转换成16进制编码
            unicode.append("0x" + Integer.toHexString(c) + " ");
        }
        return unicode.toString();
    }

    private String string2Utf8(String string) {

        try {
            byte[] bytes = string.getBytes("UTF-8");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append("0x");
                sb.append(Integer.toHexString(bytes[i] & 0xFF).toUpperCase() + " ");

            }

            return sb.toString().trim();

        } catch (Exception ex) {
            return string;
        }
    }


    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imgId);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                DimensionUtils.convertDipToPixels(context.getResources(), 25),
                DimensionUtils.convertDipToPixels(context.getResources(), 25),
                true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        //dealExpression(context, spannableString, patten, start, 25);
        dealEmojiExpression(context, spannableString, patten, start, 25);
    }

    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start,
                                int width) throws Exception {


        dealEmojiExpression(context, spannableString, patten, start, width);
        return;
//
//
//        Matcher matcher = patten.matcher(spannableString);
//        while (matcher.find()) {
//            String key = matcher.group();
//            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
//            if (matcher.start() < start) {
//                continue;
//            }
//            String value = emojiMap.get(key);
//            if (TextUtils.isEmpty(value)) {
//                continue;
//            }
//            int resId = context.getResources().getIdentifier(value, "mipmap",
//                    context.getPackageName());
//            if (resId != 0) {
//                Bitmap bitmap = BitmapFactory.decodeResource(
//                        context.getResources(), resId);
//                bitmap = Bitmap.createScaledBitmap(
//                        bitmap,
//                        DimensionUtils.convertDipToPixels(
//                                context.getResources(), width),
//                        DimensionUtils.convertDipToPixels(
//                                context.getResources(), width), true);
//                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
//                ImageSpan imageSpan = new ImageSpan(context, bitmap);
//                // 计算该图片名字的长度，也就是要替换的字符串的长度
//                int end = matcher.start() + key.length();
//                // 将该图片替换字符串中规定的位置中
//                spannableString.setSpan(imageSpan, matcher.start(), end,
//                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                if (end < spannableString.length()) {
//                    // 如果整个字符串还未验证完，则继续。。
//                    dealExpression(context, spannableString, patten, end, width);
//                }
//                break;
//            }
//        }
    }


    private void dealEmojiExpression(Context context,
                                     SpannableString spannableString, Pattern patten, int start,
                                     int width) throws Exception {

        int strLength = spannableString.length();

        for (int i = 0; i < strLength; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(spannableString.subSequence(i, i + 1));
            String selectedStr = sb.toString();

            if (isText(patten, selectedStr))
                continue;

            String key = string2Utf8(selectedStr);
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                if (i == strLength - 1)
                    continue;

                sb.append(spannableString.subSequence(i + 1, i + 2));
                selectedStr = sb.toString();
                key = string2Utf8(selectedStr);
                value = emojiMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    if (i >= strLength - 3) {
                        continue;
                    }

                    sb.append(spannableString.subSequence(i + 2, i + 4));
                    selectedStr = sb.toString();
                    key = string2Utf8(selectedStr);
                    value = emojiMap.get(key);
                    if (TextUtils.isEmpty(value)) {
                        continue;
                    } else {
                        replaceEmojiText(context, spannableString, i, 4, value, width);
                        i += 3;
                    }
                } else {
                    replaceEmojiText(context, spannableString, i, 2, value, width);
                    i += 1;
                }
            } else {
                replaceEmojiText(context, spannableString, i, 1, value, width);
            }

        }
    }

    private void replaceEmojiText(Context context,
                                  SpannableString spannableString, int start, int length,
                                  String key, int imageWidth) throws Exception {
        int resId = context.getResources().getIdentifier(key, "mipmap",
                context.getPackageName());
        if (resId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), resId);
            bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    DimensionUtils.convertDipToPixels(
                            context.getResources(), imageWidth),
                    DimensionUtils.convertDipToPixels(
                            context.getResources(), imageWidth), true);
            // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            // 计算该图片名字的长度，也就是要替换的字符串的长度
            int end = start + length;
            // 将该图片替换字符串中规定的位置中
            spannableString.setSpan(imageSpan, start, end,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }


    }

    public void getFileText(Context context) {
        ParseData(getEmojiFile(context), context);
    }

    /**
     * 初始化表情列表
     */
    public void getEmojiList(Context context) {
        List<ChatEmoji> chatEmojis = new SeeLoveDao(context).getDBEmojiList();
        LogUtil.e("chatEmojis" + chatEmojis.size());
        ChatEmoji emojEentry;
        try {
            for (ChatEmoji chatEmoji : chatEmojis) {
                String fileName = chatEmoji.getFaceName();
                int index = fileName.lastIndexOf(".");
                fileName = fileName.substring(0, index);
                String mapKey = chatEmoji.getMapKey().toLowerCase().replace("0x", "\\u").trim().replace(" ", "");
                emojiMap.put(chatEmoji.getCharacter(), fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "mipmap", context.getPackageName());
                if (resID != 0) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(chatEmoji.getCharacter());
                    emojEentry.setFaceName(fileName);
                    emojis.add(emojEentry);
                }
            }
            int pageCount = (int) Math.ceil(emojis.size() / pageSize + 0.1);
            for (int i = 0; i < pageCount; i++) {
                emojiLists.add(getData(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getEmojiFile(Context context) {
        try {
            InputStream in = context.getResources().getAssets().open("emoji");
            return FileUtil.readAllLines(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析字符
     *
     * @param data
     */
    private void ParseData(List<String> data, Context context) {
        if (data == null) {
            return;
        }
        ChatEmoji emojEentry;
        try {
            for (String str : data) {
                String fileName = str;
                emojiMap.put("[" + str + "]", fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "mipmap", context.getPackageName());
                if (resID != 0) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter("[" + str + "]");
                    emojEentry.setFaceName(fileName);
                    emojis.add(emojEentry);
                }
            }
            int pageCount = (int) Math.ceil(emojis.size() / pageSize + 0.1);
            for (int i = 0; i < pageCount; i++) {
                emojiLists.add(getData(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private List<ChatEmoji> getData(int page) {
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex > emojis.size()) {
            endIndex = emojis.size();
        }
        // 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        list.addAll(emojis.subList(startIndex, endIndex));
        if (list.size() < pageSize) {
            for (int i = list.size(); i < pageSize; i++) {
                ChatEmoji object = new ChatEmoji();
                list.add(object);
            }
        }
        if (list.size() == pageSize) {
            ChatEmoji object = new ChatEmoji();
            object.setId(R.drawable.face_del_icon);
            list.add(object);
        }
        return list;
    }

    public String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>> 8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

    /**
     * unicode 转字符串
     *
     * @param unicode
     * @return
     */
    /**
     * 将unicode 字符串
     *
     * @param str 待转字符串
     * @return 普通字符串
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public String getEmoji(String dbstr) {
        String emoji = dbstr;
        emoji = emoji.replaceAll("0x", "");
        emoji = emoji.replaceAll(" ", "");
        BigInteger bigInteger1 = new BigInteger(emoji, 16);
        byte[] by1 = bigInteger1.toByteArray();
        byte[] by2 = null;
        if (by1[0] == 0) {
            by2 = new byte[by1.length - 1];
        }
        for (int i = by1.length - by2.length; i <= by2.length; i++) {
            by2[i - 1] = by1[i];
            System.out.println("******* b******* " + by1[i]);
        }
        String result = "";
        try {
            result = new String(by2, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
