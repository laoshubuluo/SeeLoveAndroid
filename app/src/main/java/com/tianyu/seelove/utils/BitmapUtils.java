package com.tianyu.seelove.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.manager.DirectoryManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;

/**
 * @author shisheng.zhao
 * @Description: 位图工具类
 * @date 2015-09-11 下午12:11:24
 */
public class BitmapUtils {

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 存储Bitmap
     *
     * @param bitmap
     * @return 存储路径
     */
    public static String saveBitmapToFile(Bitmap bitmap) {
        if (null == bitmap) {
            return "";
        }
        String picPath = null;
        FileOutputStream fout = null;
        try {
            File file = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE),
                    StringUtils.generateGUID()
                            + ".jpg");
            picPath = file.getAbsolutePath();
            fout = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 90, fout);
            bitmap.recycle();
            System.gc();
            return picPath;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String saveBitmapToFile(Bitmap bitmap, File file) {
        String picPath = null;
        FileOutputStream fout = null;
        try {
            picPath = file.getAbsolutePath();
            fout = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 80, fout);
            return picPath;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从uri中获取指定大小的缩放后的图片
     *
     * @param resolver
     * @param uri      目标图片地址
     * @param width    需要获取的文件的宽度
     * @param height   需要获取的文件的宽度
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap decodeStream(ContentResolver resolver, Uri uri,
                                      int width, int height) throws FileNotFoundException {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (width > 0 || height > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(
                    new BufferedInputStream(resolver.openInputStream(uri),
                            8 * 1024), null, options);
            int w = options.outWidth;
            int h = options.outHeight;
            while (true) {
                if ((width > 0 && w / 2 < width && h < 4096)
                        || (height > 0 && h / 2 < height && w < 4096)) {
                    break;
                }
                w /= 2;
                h /= 2;
                scale *= 2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        Bitmap bitmaps = BitmapFactory.decodeStream(new BufferedInputStream(
                resolver.openInputStream(uri), 8 * 1024), null, options);
        return new SoftReference<Bitmap>(bitmaps).get();
    }

    public static Bitmap getResourceBitmap(Resources resolver, int id,
                                           float maxWidth, float maxHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(resolver, id, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = (int) Math.min(maxWidth, maxHeight);
        newOpts.inSampleSize = computeSampleSize(newOpts, w,
                (int) (1 * 1024 * 1024));
        bitmap = BitmapFactory.decodeResource(resolver, id, newOpts);
        Log.e("SampleSize==========", "be:" + newOpts.inSampleSize);
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        return new SoftReference<Bitmap>(bitmap).get();// 压缩好比例大小后再进行质量压缩
    }


    /**
     * Bitmap缩放
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap
                .createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return new SoftReference<Bitmap>(newBitmap).get();
    }

    /**
     * 从文件获取Bitmap
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getImageFromFile(String srcPath) {
        return getImageFromFile(srcPath, 400f, 800f);
    }

    public static Bitmap getImageFromFileWithHighResolution(String srcPath,
                                                            float maxWidth, float maxHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = (int) Math.min(maxWidth, maxHeight);
        newOpts.inSampleSize = computeSampleSize(newOpts, w,
                (int) (1 * 1024 * 1024));
        Log.e("SampleSize==========", "be:" + newOpts.inSampleSize);
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        FileInputStream is = null;

        try {
            is = new FileInputStream(srcPath);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null,
                    newOpts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int degree = readPictureDegree(srcPath);
        if (degree != 0) {// 旋转照片角度
            bitmap = rotateBitmap(bitmap, degree);
        }
        return new SoftReference<Bitmap>(bitmap).get();// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return new SoftReference<Bitmap>(bitmap).get();
    }

    public static Bitmap getImageFromFile(String srcPath, float maxWidth,
                                          float maxHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = (int) Math.min(maxWidth, maxHeight);
        newOpts.inSampleSize = computeSampleSize(newOpts, w,
                (int) (1 * 1024 * 1024));
        // Log.e("SampleSize==========", "be:" + be * 2);
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return new SoftReference<Bitmap>(bitmap).get();// 压缩好比例大小后再进行质量压缩
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static byte[] bitmap2Bytes(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 32) {    //循环判断如果压缩后图片是否大于32kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        return baos.toByteArray();
    }

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        float scaleWidth = 1;
        float scaleHeight = 1;
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        if (bmpHeight < 200 && bmpWidth < 200) {
            return bitmap;
        }
        /* 设置图片缩小的比例 */
        double scale = 0.4;
        /* 计算出这次要缩小的比例 */
        scaleWidth = (float) (scaleWidth * scale);
        scaleHeight = (float) (scaleHeight * scale);
        /* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
                bmpHeight, matrix, true);
        return resizeBmp;
    }

    public static Bitmap scaleMapBitmap(Bitmap bitmap) {
        float scaleWidth = 1;
        float scaleHeight = 1;
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        if (bmpHeight < 100 && bmpWidth < 100) {
            return bitmap;
        }
        /* 设置图片缩小的比例 */
        double scale = 1;
        /* 计算出这次要缩小的比例 */
        scaleWidth = (float) (scaleWidth * scale);
        scaleHeight = (float) (scaleHeight * scale);
        /* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
                bmpHeight, matrix, true);
        return resizeBmp;
    }

    public static Bitmap comp(Bitmap image, float hh, float ww) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 60, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, 40, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
//        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 220f;//这里设置高度为800f
//        float ww = 135f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Amen");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, String.format(context.getString(R.string.save_image_sucess), appDir.toString()), Toast.LENGTH_LONG).show();
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * 网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * 本地路径:url="file://mnt/sdcard/photo/image.png";
     * 支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), Constant.IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, Constant.IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[Constant.IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * @param bitmap 原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }
}
