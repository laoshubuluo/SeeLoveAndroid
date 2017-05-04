package com.tianyu.seelove.ui.activity.system;

import java.util.ArrayList;
import java.util.HashMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.SignleAlbumGridViewAdapter;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * @author shisheng.zhao
 * @Description: 選擇頭像
 * @date 2017-04-20 16:59
 */
public class SignleAlbumActivity extends BaseActivity {
    private GridView gridView;
    private ArrayList<String> dataList = new ArrayList<String>();
    private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private ProgressBar progressBar;
    private SignleAlbumGridViewAdapter gridImageAdapter;
    private LinearLayout selectedImageLayout;
    private Button okButton;
    private HorizontalScrollView scrollview;
    private ImageView btn_back;
    private TextView title;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>) bundle
                .getSerializable("dataList");
        init();
        initListener();
    }

    private void init() {
        findViewById(R.id.bottom_layout).setVisibility(View.GONE);
        btn_back = (ImageView) findViewById(R.id.leftBtn);
        title = (TextView) findViewById(R.id.titleView);
        title.setText(getString(R.string.select_photos));
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new SignleAlbumGridViewAdapter(this, dataList,
                selectedDataList);
        gridView.setAdapter(gridImageAdapter);
        refreshData();
        selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
        okButton = (Button) findViewById(R.id.ok_button);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
        initSelectImage();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
                break;
        }
    }

    private void initSelectImage() {
        if (selectedDataList == null)
            return;
        for (final String path : selectedDataList) {
            ImageView imageView = (ImageView) LayoutInflater.from(
                    SignleAlbumActivity.this).inflate(
                    R.layout.choose_imageview, selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            hashMap.put(path, imageView);
            ImageLoader.getInstance().displayImage(ImageLoaderUtil.getAcceptableUri(path),
                    imageView, new DisplayImageOptions.Builder().cacheInMemory(true).
                            cacheOnDisk(true).showImageOnLoading(R.mipmap.defaultimg).build());
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removePath(path);
                    gridImageAdapter.notifyDataSetChanged();
                }
            });
        }
        okButton.setText("完成(" + selectedDataList.size() + "/4)");
    }

    private void initListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridImageAdapter
                .setOnItemClickListener(new SignleAlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, final String path, boolean isChecked) {
                        if (selectedDataList.size() >= 1) {
                            toggleButton.setChecked(false);
                            if (!removePath(path)) {
                                Toast.makeText(SignleAlbumActivity.this,
                                        getString(R.string.select_photos_pre_one), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (isChecked) {
                            if (!hashMap.containsKey(path)) {
                                ImageView imageView = (ImageView) LayoutInflater
                                        .from(SignleAlbumActivity.this)
                                        .inflate(R.layout.choose_imageview,
                                                selectedImageLayout, false);
                                selectedImageLayout.addView(imageView);
                                imageView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int off = selectedImageLayout
                                                .getMeasuredWidth()
                                                - scrollview.getWidth();
                                        if (off > 0) {
                                            scrollview.smoothScrollTo(off, 0);
                                        }
                                    }
                                }, 100);
                                hashMap.put(path, imageView);
                                selectedDataList.add(path);
                                /*ImageManager2.from(SignleAlbumActivity.this)
                                        .displayImage(imageView, path,
												R.drawable.camera_default, DimensionUtils
												.convertDipToPixels(
														SignleAlbumActivity.this
																.getResources(),
														100),
										DimensionUtils
												.convertDipToPixels(
														SignleAlbumActivity.this
																.getResources(),
														100));*/
                                ImageLoader.getInstance().displayImage(ImageLoaderUtil.getAcceptableUri(path),
                                        imageView, new DisplayImageOptions.Builder().cacheInMemory(true).
                                                cacheOnDisk(true).showImageOnLoading(R.mipmap.defaultimg).build());
                                imageView
                                        .setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                toggleButton.setChecked(false);
                                                removePath(path);
                                            }
                                        });
                                okButton.setText(getString(R.string.select_pre)
                                        + selectedDataList.size() + getString(R.string.select_add));
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                // intent.putArrayListExtra("dataList",
                                // dataList);
                                bundle.putStringArrayList("dataList",
                                        selectedDataList);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            removePath(path);
                        }
                    }
                });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", selectedDataList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private boolean removePath(String path) {
        if (hashMap.containsKey(path)) {
            selectedImageLayout.removeView(hashMap.get(path));
            hashMap.remove(path);
            removeOneData(selectedDataList, path);
            okButton.setText(getString(R.string.select_pre) + selectedDataList.size() + getString(R.string.select_add));
            return true;
        } else {
            return false;
        }
    }

    private void removeOneData(ArrayList<String> arrayList, String s) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(s)) {
                arrayList.remove(i);
                return;
            }
        }
    }

    private void refreshData() {
        AsyncTask<Void, Void, ArrayList<String>> refreshTask = new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                ArrayList<String> mediaFiles = getAllMediaFiles();
                return mediaFiles;
            }

            protected void onPostExecute(ArrayList<String> tmpList) {
                if (SignleAlbumActivity.this == null
                        || SignleAlbumActivity.this.isFinishing()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                dataList.clear();
                dataList.addAll(tmpList);
                gridImageAdapter.notifyDataSetChanged();
                return;
            }

            ;
        };
        AsyncTaskUtils.execute(refreshTask);
    }

    private ArrayList<String> getAllMediaFiles() {
        ArrayList<String> listDir = new ArrayList<String>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = SignleAlbumActivity.this
                .getContentResolver();
        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
//            Bitmap bitmap = BitmapUtils.getImageFromFile(path);
//            if (null != bitmap) {
//                if (bitmap.getHeight() < 100 && bitmap.getWidth() < 100) {
//                    break;
//                }
//                if (StringUtils.isNotBlank(path)) {
//                    listDir.add(path);
//                }
//            }
            if (StringUtils.isNotBlank(path)) {
                listDir.add(path);
            }
        }
        mCursor.close();
        return listDir;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
