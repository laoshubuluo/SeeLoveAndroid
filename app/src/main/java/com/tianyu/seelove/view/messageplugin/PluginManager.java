package com.tianyu.seelove.view.messageplugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridLayout;

/**
 * @author shisheng.zhao
 * @Description: 管理所有的Plugin
 * @date 2015-09-01 下午18:13:26
 */
public class PluginManager {
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_PERSONAL = 1;
    /**
     * 聊天对象
     */
    private String target;

    private String targetGroup;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 插件父ui对象
     */
    private ViewGroup pluginbox;
    /**
     * 输入框
     */
    private EditText editText;
    /**
     * 插件入口父ui对象
     */
    private ViewPager entranceBox;

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    private LinkedHashMap<String, MessagePlugin> pluginMap = new LinkedHashMap<String, MessagePlugin>();

    private List<ResultChangedNotifier> notifiers = new ArrayList<ResultChangedNotifier>();

    public void addResultChangedNotifier(ResultChangedNotifier notifier) {
        notifiers.add(notifier);
    }

    public PluginManager(Context context, ViewGroup pluginbox,
                         EditText editText) {
        this.context = context;
        this.pluginbox = pluginbox;
        this.editText = editText;
        loadPlugins();
        drawPlugins();
    }

    public PluginManager(Context context, ViewGroup pluginbox,
                         EditText editText, String target) {
        this(context, pluginbox, editText);
    }

    public MessagePlugin getPlugin(String pluginName) {
        if (pluginMap.containsKey(pluginName)) {
            return pluginMap.get(pluginName);
        } else {
            return null;
        }
    }

    public void resetAllPlugins() {
        Iterator<MessagePlugin> pluginIterator = pluginMap.values().iterator();
        while (pluginIterator.hasNext()) {
            MessagePlugin plugin = pluginIterator.next();
            plugin.reset();
        }
    }

    private void drawPlugins() {
        ViewPager viewPager = new ViewPager(context);
        entranceBox = viewPager;
        LayoutParams viewPagerLayout = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(viewPagerLayout);
        Iterator<MessagePlugin> pluginIterator = pluginMap.values().iterator();
        int n = 0;
        GridLayout layout = null;
        final List<GridLayout> grids = new ArrayList<GridLayout>();
        while (pluginIterator.hasNext()) {
            // 为了隐藏表情按钮,哈哈
            if (n == 4 || n == 3) {
                break;
            }
            MessagePlugin plugin = pluginIterator.next();
            if (n++ % 4 == 0) {
                layout = new GridLayout(context);
                layout.setColumnCount(4);
                layout.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                grids.add(layout);
            }
            layout.addView(plugin.getEntrance());
        }
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(grids.get(position), 0);
                return grids.get(position);
            }

            @Override
            public int getCount() {
                return grids.size();
            }
        });
        pluginbox.addView(viewPager);
    }

    public void notifyResultChanged(int requestCode, int resultCode, Intent data) {
        for (ResultChangedNotifier notifier : notifiers) {
            try {
                notifier.notifyResultChanged(requestCode, resultCode, data);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadPlugins() {
        pluginMap.put(ImageMessagePlugin.class.getName(),
                new ImageMessagePlugin(this));
        pluginMap.put(CameraMessagePlugin.class.getName(),
                new CameraMessagePlugin(this));
        pluginMap.put(LocationMessaePlugin.class.getName(),
                new LocationMessaePlugin(this));
        pluginMap.put(ImojiMessagePlugin.class.getName(), new
                ImojiMessagePlugin(this));
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ViewPager getEntranceBox() {
        return entranceBox;
    }

    public ViewGroup getPluginbox() {
        return pluginbox;
    }

    public void setPluginbox(ViewGroup pluginbox) {
        this.pluginbox = pluginbox;
    }

    public EditText getEditText() {
        return editText;
    }

    public interface ResultChangedNotifier {
        public void notifyResultChanged(int requestCode, int resultCode,
                                        Intent data);
    }
}
