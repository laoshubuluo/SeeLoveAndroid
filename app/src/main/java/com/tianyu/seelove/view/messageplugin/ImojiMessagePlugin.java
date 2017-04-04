package com.tianyu.seelove.view.messageplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout.LayoutParams;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FaceAdapter;
import com.tianyu.seelove.adapter.FaceViewPagerAdapter;
import com.tianyu.seelove.model.entity.message.ChatEmoji;
import com.tianyu.seelove.utils.FaceConversionUtils;

/**
 * @author shisheng.zhao
 * @Description: 表情Plugin
 * @date 2015-09-01 上午18:14:37
 */
public class ImojiMessagePlugin extends MessagePlugin {
    private ViewPager emojiPager;

    public ImojiMessagePlugin(final PluginManager manager) {
        super(manager);
        ViewPager viewPager = new ViewPager(manager.getContext());
        List<View> pageViews = new ArrayList<View>();
        List<List<ChatEmoji>> emojis = FaceConversionUtils.getInstace().emojiLists;
        for (int i = 0; i < emojis.size(); i++) {
            GridView view = new GridView(manager.getContext());
            final FaceAdapter adapter = new FaceAdapter(manager.getContext(), emojis.get(i));
            view.setAdapter(adapter);
            view.setNumColumns(7);
            view.setSelector(R.color.translate);
//            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(3, 0, 3, 0);
            view.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    ChatEmoji emoji = (ChatEmoji) adapter.getItem(arg2);
                    if (emoji.getId() == R.drawable.face_del_icon) {
                        int selection = manager.getEditText().getSelectionStart();
                        String text = manager.getEditText().getText().toString();
                        if (selection > 0) {
                            String text2 = text.substring(selection - 1);
                            boolean isMessyCode = isMessyCode(text2);
                            if (isMessyCode) {
                                try {
                                    manager.getEditText().getText().delete(selection - 2, selection);
                                } catch (Exception e) {
                                    manager.getEditText().getText().delete(selection - 1, selection);
                                }
                                return;
                            }
                            manager.getEditText().getText().delete(selection - 1, selection);
                        }
                    }
                    if (!TextUtils.isEmpty(emoji.getCharacter())) {
                        SpannableString spannableString = FaceConversionUtils.getInstace()
                                .addFace(manager.getContext(), emoji.getId(), FaceConversionUtils.getInstace().getEmoji(emoji.getCharacter()));
                        manager.getEditText().append(spannableString);
                    }
                }
            });
//            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }
        viewPager.setAdapter(new FaceViewPagerAdapter(pageViews));
        viewPager.setVisibility(View.GONE);
        emojiPager = viewPager;
        manager.getPluginbox().addView(viewPager);
    }

    @Override
    protected int getEntranceBtnImg() {
        return R.mipmap.chat_tool_emoj;
    }

    @Override
    public void onEntranceClick() {
        manager.getEntranceBox().setVisibility(View.GONE);
        emojiPager.setVisibility(View.VISIBLE);
    }

    @Override
    protected void reset() {
        manager.getEntranceBox().setVisibility(View.VISIBLE);
        emojiPager.setVisibility(View.GONE);
    }

    @Override
    protected String getEntanceText() {

        return "表情";
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 将字符串转成unicode
     *
     * @param str 待转字符串
     * @return unicode字符串
     */
    public static String convert(String str) {
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
}
