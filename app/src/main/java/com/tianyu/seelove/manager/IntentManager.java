package com.tianyu.seelove.manager;

import android.content.Context;
import android.content.Intent;

public class IntentManager {
	/**
	 * @author shisheng.zhao
	 * @date 2014-10-14
	 * @param packContext 上下文对象
	 * @param clazz 需要创建的意图目标对象
	 * @return
	 */
	public static Intent createIntent(Context packContext, Class<?> clazz) {
		return new Intent(packContext, clazz);
	}
}
