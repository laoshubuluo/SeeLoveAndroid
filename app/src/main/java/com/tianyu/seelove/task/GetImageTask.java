package com.tianyu.seelove.task;

import android.graphics.Bitmap;

import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.utils.BitmapUtils;

public class GetImageTask extends
        BaseTask<String, Void, Bitmap> {
    @Override
    protected Bitmap _doInBackground(String... params) {
        return BitmapUtils.GetLocalOrNetBitmap(params[0]);
    }
}
