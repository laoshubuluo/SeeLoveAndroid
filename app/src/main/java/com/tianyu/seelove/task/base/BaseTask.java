package com.tianyu.seelove.task.base;

import android.os.AsyncTask;

/**
 * 异步任务基类 扩展了回调接口，将ui和任务进行分离
 * @param <Params>   传入的参数
 * @param <Progress> 进度
 * @param <Result>   结果
 * @author shizheng.zhao
 * @date 2014-1-23
 */
public abstract class BaseTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {
    // 同步控制变量
    protected static final Object SYNC_OBJECT = new Object();

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (onCancelHandler != null) {
            onCancelHandler.handle();
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (onPostExecuteHandler != null) {
            onPostExecuteHandler.handle(result);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (onPreExecuteHandler != null) {
            onPreExecuteHandler.handle();
        }
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (onProgressUpdateHandler != null) {
            onProgressUpdateHandler.handle(values);
        }
    }

    @Override
    protected Result doInBackground(Params... params) {
        return _doInBackground(params);
    }

    private OnCancelHandler onCancelHandler;

    /**
     * 设置取消操作后的回调函数
     * @param handler
     */
    public void setOnCancelHandler(OnCancelHandler handler) {
        this.onCancelHandler = handler;
    }

    /**
     * 当取消操作执行时的回调接口
     * @author shizheng.zhao
     * @date 2014-1-23
     */
    public interface OnCancelHandler {
        public void handle();
    }

    private OnPostExecuteHandler<Result> onPostExecuteHandler;

    /**
     * 注册当执行任务结束之后的回调函数
     * @param handler
     */
    public void setOnPostExecuteHandler(OnPostExecuteHandler<Result> handler) {
        this.onPostExecuteHandler = handler;
    }

    /**
     * 任务执行结束后的回调函数 在该回调函数中，可以操作界面元素
     * @param <Result>
     * @author shizheng.zhao
     * @date 2014-1-23
     */
    public interface OnPostExecuteHandler<Result> {
        public void handle(Result result);
    }

    private OnPreExecuteHandler onPreExecuteHandler;

    /**
     * 注册任务执行前的回调函数
     * @param handler
     */
    public void setOnPreExecuteHandler(OnPreExecuteHandler handler) {
        this.onPreExecuteHandler = handler;
    }

    /**
     * 任务执行前的回调接口 在任务执行前可以执行诸如显示一个加载框等操作
     * @author shizheng.zhao
     * @date 2014-1-23
     */
    public interface OnPreExecuteHandler {
        public void handle();
    }

    private OnProgressUpdateHandler<Progress> onProgressUpdateHandler;

    /**
     * 注册当进度发生变化时的回调函数
     * @param handler
     */
    public void setOnProgressUpdateHandler(
            OnProgressUpdateHandler<Progress> handler) {
        this.onProgressUpdateHandler = handler;
    }

    /**
     * 当进度发生变化时的回调接口
     * @param <Progress> 需要处理的参数类型
     * @author shizheng.zhao
     * @date 2014-1-23
     */
    public interface OnProgressUpdateHandler<Progress> {
        /**
         * 处理进度参数
         * @param values 进度参数
         */
        public void handle(Progress... values);
    }

    /**
     * 公开给继承类的内部操作方法，该操作为子类必须继承的任务,该操作为后台任务，不可操作界面元素
     * @param params 传入参数列表
     * @return 操作之后返回的结果值
     */
    protected abstract Result _doInBackground(Params... params);
}
