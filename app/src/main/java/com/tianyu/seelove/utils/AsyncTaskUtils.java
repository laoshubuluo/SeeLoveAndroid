package com.tianyu.seelove.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.tianyu.seelove.manager.ThreadManager;

public class AsyncTaskUtils {
	@SuppressLint("NewApi")
	public static <Param, Progress, Result> void execute(
			AsyncTask<Param, Progress, Result> task, Param... objects) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			task.execute(objects);
		} else {
			task.executeOnExecutor(ThreadManager.getInstance().getThreadPool(),
					objects);
		}
	}
}
