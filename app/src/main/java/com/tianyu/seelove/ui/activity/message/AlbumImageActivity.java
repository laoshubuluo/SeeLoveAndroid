package com.tianyu.seelove.ui.activity.message;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.AlbumImageAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.ImageFloder;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.pop.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class AlbumImageActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {
    int totalCount = 0;
    private ProgressDialog mProgressDialog;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private GridView mGirdView;
    private AlbumImageAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    private int mScreenHeight;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private ImageView btn_back, btn_send;
    private TextView titleView;
    private long target;
    private UserDao userDao;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.select_photos_no),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> rmImgs = new ArrayList<String>();
        mImgs = Arrays.asList(mImgDir.list());
        for (int i = (mImgs.size() - 1); i > 0; i--) {
            if (mImgs.get(i).endsWith(".jpg") || mImgs.get(i).endsWith(".png")
                    || mImgs.get(i).endsWith(".jpeg")) {
                rmImgs.add(mImgs.get(i));
                LogUtil.i("AlbumImage===" + mImgs.get(i));
            } else {
            }
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new AlbumImageAdapter(getApplicationContext(), rmImgs,
                R.layout.grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount + getString(R.string.pre));
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));
        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumimage);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        AlbumImageAdapter.mSelectedImage.clear();
        userDao = new UserDaoImpl();
        target = getIntent().getExtras().getLong("target");
        initView();
        // 在獲取圖片之前進行全盤檢索,防止剛加入的圖片沒有被查找到
//        allScan();
        getImages();
        initEvent();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getString(R.string.no_sdcard), Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.loading));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumImageActivity.this
                        .getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    /**
     * 初始化View
     */
    private void initView() {
        btn_back = (ImageView) findViewById(R.id.leftBtn);
        btn_send = (ImageView) findViewById(R.id.rightBtn);
        titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(getString(R.string.select_photos));
        btn_send.setVisibility(View.VISIBLE);
        btn_back.setVisibility(View.VISIBLE);
        btn_send.setBackgroundResource(R.mipmap.share_btn);
        btn_send.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn: {
                finish();
                break;
            }
            case R.id.rightBtn: {
                if (AlbumImageAdapter.mSelectedImage.size() > 0) {
                    if (0l == target) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("selectImg", AlbumImageAdapter.mSelectedImage);
                        setResult(ActivityResultConstant.SELECT_PICTURE, intent);
                        finish();
                    } else {
                        btn_send.setClickable(false);
                        UserDao userDao = new UserDaoImpl();
                        SLUser user = userDao.getUserByUserId(target);
                        for (String imagePath : AlbumImageAdapter.mSelectedImage) {
                            if (!StringUtils.isNotBlank(imagePath)) {
                                break;
                            }
                            String pathString = imagePath;
                            try {
                                pathString = BitmapUtils.saveBitmapToFile(BitmapUtils
                                        .getImageFromFileWithHighResolution(
                                                imagePath, 800, 800));
                            } catch (OutOfMemoryError ex) {
                                try {
                                    pathString = BitmapUtils.saveBitmapToFile(BitmapUtils
                                            .getImageFromFileWithHighResolution(
                                                    imagePath, 400, 400));
                                } catch (OutOfMemoryError error) {
                                    ex.printStackTrace();
                                }
                            }
                            final long lastId = System.nanoTime();
                            SLImageMessage imageMessage = new SLImageMessage();
                            imageMessage.setMessageId(String.valueOf(lastId));
                            imageMessage.setMessageContent(pathString);
                            imageMessage.setUserFrom(AppUtils.getInstance().getUserId());
                            imageMessage.setUserTo(target);
                            imageMessage.setIsRead(SLMessage.msgRead);
                            imageMessage.setTimestamp(new Date().getTime());
                            imageMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
                            InsertMessageTask insertMessageTask = new InsertMessageTask();
                            insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                                        @Override
                                        public void handle(Boolean result) {
                                            // 发送融云广播
                                            Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                                            send_Intent.putExtra("messageId", String.valueOf(lastId));
                                            send_Intent.putExtra("chatType", "single");
                                            getApplicationContext().sendOrderedBroadcast(send_Intent, null);
                                            // 本地会话广播
                                            Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                                            intent.putExtra("messageID", String.valueOf(lastId));
                                            getApplicationContext().sendOrderedBroadcast(intent, null);
                                            // 图片消息发送的时候默认发送一个进度,让图片加灰处理
                                            Intent process_Intent = new Intent(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
                                            process_Intent.putExtra("ProcessCount", "0");
                                            process_Intent.putExtra("MessageID", String.valueOf(lastId));
                                            getApplicationContext().sendOrderedBroadcast(process_Intent, null);
                                        }
                                    });
                            insertMessageTask.execute(imageMessage);
                            SLSession session = new SLSession();
                            session.setLastMessageId(String.valueOf(lastId));
                            session.setPriority(imageMessage.getTimestamp());
                            session.setTargetId(target);
                            session.setSessionContent(imageMessage.getMessageContent());
                            session.setMessageType(imageMessage.getMessageType());
                            session.setSessionType(SessionType.CHAT);
                            session.setSessionName(user.getNickName());
                            SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                            sessionDaoImpl.addSession(session);
                            Intent session_intent = new Intent(Actions.ACTION_SESSION);
                            session_intent.putExtra("targetId", session.getTargetId());
                            sendOrderedBroadcast(session_intent, null);
                        }
                    }
                }
                finish();
                break;
            }
        }
    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindo
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        List<String> rmImgs = new ArrayList<String>();
        mImgs = Arrays.asList(mImgDir.list());
        for (int i = (mImgs.size() - 1); i > 0; i--) {
            if (mImgs.get(i).endsWith(".jpg") || mImgs.get(i).endsWith(".png")
                    || mImgs.get(i).endsWith(".jpeg")) {
                rmImgs.add(mImgs.get(i));
                LogUtil.i("AlbumImage===" + mImgs.get(i));
            } else {
            }
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new AlbumImageAdapter(getApplicationContext(), rmImgs,
                R.layout.grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + getString(R.string.pre));
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    public void allScan() {
        if (Build.VERSION.SDK_INT >= 19) { // 判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(AlbumImageActivity.this, paths, null, null);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }
}
