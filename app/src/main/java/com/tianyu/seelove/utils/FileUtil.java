package com.tianyu.seelove.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/8/1
 * introduce : 文件读取工具
 */
public class FileUtil {
    private FileUtil() {
    }

    private static MediaScannerConnection mediaScannerConnection = null;

    /**
     * 将Uri转换为文件
     *
     * @param uri     需要转换的Uri
     * @param context 上下文
     * @return
     */
    public static File convertUriToFile(Uri uri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor actualimagecursor = ((Activity) context).managedQuery(uri, proj,
                null, null, null);
        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        File file = new File(img_path);
        return file;
    }

    /**
     * 重新加载媒体文件 当新的媒体文件加入到系统的时候，系统不会主动加载该文件到对应的媒体程序 需要调用该方法更新系统的媒体数据库
     *
     * @param f
     * @param context
     */
    public static void reloadMedia(final File f, final Context context,
                                   final String contentType) {
        mediaScannerConnection = new MediaScannerConnection(context,
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    public void onScanCompleted(String path, Uri uri) {
                        mediaScannerConnection.disconnect();
                    }

                    public void onMediaScannerConnected() {
                        if (f.isDirectory()) {
                            File[] files = f.listFiles();
                            if (files != null) {
                                for (int i = 0; i < files.length; i++) {
                                    if (files[i].isDirectory()) {
                                        reloadMedia(files[i], context,
                                                contentType);
                                    } else {
                                        mediaScannerConnection.scanFile(
                                                files[i].getAbsolutePath(),
                                                contentType);
                                    }
                                }
                            }
                        } else {
                            mediaScannerConnection.scanFile(
                                    f.getAbsolutePath(), contentType);
                        }
                    }
                });
        mediaScannerConnection.connect();
    }


    public static boolean saveToFile(String path, Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        boolean flag = false;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            flag = true;
        } catch (FileNotFoundException e) {
            LogUtil.e("未找到文件" + path, e);
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.e("写入文件发生错误" + path, e);
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {

                    fout.close();
                }
            } catch (IOException e) {
                LogUtil.e("关闭输入流发生错误", e);
                e.printStackTrace();

            }
        }
        return flag;
    }

    /**
     * 将二进制数组保存到文件
     *
     * @param path 文件的路径
     * @param data 数据
     * @return
     */
    public static boolean saveToFile(String path, byte[] data) {
        boolean flag = false;
        FileOutputStream out = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                out = new FileOutputStream(new File(path));
                out.write(data, 0, data.length);
                out.flush();
                flag = true;
            } catch (FileNotFoundException e) {
                LogUtil.e("未找到文件" + path, e);
            } catch (IOException e) {
                LogUtil.e("写入文件发生错误" + path, e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        LogUtil.e("关闭输入流发生错误", e);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 从一个流中读取多行数据
     *
     * @param inputStream
     * @return
     */
    public static List<String> readAllLines(InputStream inputStream) {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                lines.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LogUtil.e(e.getMessage(), e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.e(e.getMessage(), e);
                }
            }
        }
        return lines;
    }

    /**
     * 判断sdcard下指定路径的文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isExist(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }


    /*
     * 写数据到SD中的文件
     */
    public static boolean writeFile(String path, String writeStr) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(path, false), "UTF-8");
            osw.write(writeStr);
            osw.flush();
            osw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
 * 读SD中的文件
 */
    public static InputStream readFileInputStream(String fileName) {
        InputStream fin = null;
        try {
            fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
        } catch (Throwable e) {
            LogUtil.e("file read error：", e);
        } finally {
            try {
                if (null != fin)
                    fin.close();
            } catch (Throwable e) {
                LogUtil.e("io close error", e);
            }
        }
        return fin;
    }

    /*
     * 拷贝Assets文件到存储
     */
    public static void copyAssetsToStorage(Context context, String fileNameInAssets, String filePathInSD) {
        File pathFile = new File(filePathInSD);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(filePathInSD + "/" + fileNameInAssets);
            myInput = context.getAssets().open(fileNameInAssets);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            LogUtil.i("file copy to storage success:" + fileNameInAssets);
        } catch (Throwable e) {
            LogUtil.e("file copy to storage error:" + fileNameInAssets, e);
        } finally {
            try {
                if (null != myInput)
                    myInput.close();
                if (null != myOutput)
                    myOutput.close();
            } catch (Throwable e) {
                LogUtil.e("io close error", e);
            }
        }
    }

    /*
     * 复制文件夹
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().equals("xx") || file[i].getName().endsWith(".zip")) {
                LogUtil.i("skip file：" + file[i].getName());
            } else {
                if (file[i].isFile()) {
                    // 源文件
                    File sourceFile = file[i];
                    // 目标文件
                    File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                    copyFile(sourceFile, targetFile);
                }
                if (file[i].isDirectory()) {
                    // 准备复制的源文件夹
                    String dir1 = sourceDir + File.separator + file[i].getName();
                    // 准备复制的目标文件夹
                    String dir2 = targetDir + File.separator + file[i].getName();
                    copyDirectiory(dir1, dir2);
                }
            }
        }
    }

    /*
     * 复制文件
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        FileInputStream in = null;
        FileOutputStream os = null;
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            in = new FileInputStream(sourceFile);
            os = new FileOutputStream(targetFile);
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(in);
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(os);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) > 0) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            os.flush();
        } finally {
            try {
                if (null != inBuff)
                    inBuff.close();
                if (null != outBuff)
                    outBuff.close();
                if (null != in)
                    in.close();
                if (null != os)
                    os.close();
            } catch (Throwable e) {
                LogUtil.e("io close error", e);
            }
        }
    }

    /*
     * 删除文件夹
     */
    public static void delFolder(String folderPath) throws Exception {
        delAllFile(folderPath); // 删除完里面所有内容
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete(); // 删除空文件夹
    }

    /*
     * 删除指定文件夹下所有文件
     */
    public static boolean delAllFile(String path) throws Exception {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
    public static byte[] stream2Byte(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = is.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, len);
        }
        byte[] buffer = baos.toByteArray();
        return buffer;
    }
}
