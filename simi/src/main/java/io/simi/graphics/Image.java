package io.simi.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import io.simi.exception.ByteNotFoundException;

/**
 * -------------------------------
 * 			Image
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public class Image implements Serializable{

    private Bitmap bitmap;
    private byte[] bytes;

    public Image(Drawable drawable) {
        drawableToBitmap(drawable);
    }

    public Image(Resources res, int resId) {
        this(res.getDrawable(resId));
    }

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Image(String filePath) throws FileNotFoundException {
        if (new File(filePath).exists()) {
            this.bitmap = BitmapFactory.decodeFile(filePath);
        }else {
            throw new FileNotFoundException("未找到路径为" + filePath + "的图片文件");
        }
    }

    public Image(byte[] data) throws ByteNotFoundException {
        if (data.length > 0) {
            this.bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }else {
            throw new ByteNotFoundException("字节数组为空，无法进行转换，请检查数据");
        }
    }

    public Image transformRadius(float radius) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int color = 0xff888888;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        this.bitmap = mBitmap;
        return this;
    }

    public Image transformRound() {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        return transformRadius(width > height ? width/2 : height/2);
    }

    public Bitmap toBitmap() {
        return bitmap;
    }

    public Drawable toDrawable() {
        return new BitmapDrawable(bitmap);
    }

    public byte[] toPNGByte() {
        return toByte(Bitmap.CompressFormat.PNG);
    }

    public byte[] toJPGByte() {
        return toByte(Bitmap.CompressFormat.JPEG);
    }

    private byte[] toByte(Bitmap.CompressFormat type) {
        if (bytes == null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(type, 100, bos);
            bytes = bos.toByteArray();
        }
        return bytes;
    }

    private void drawableToBitmap(Drawable drawable) {
        final int width = drawable.getIntrinsicWidth();
        final int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        this.bitmap = bitmap;
    }

    public static ShapeDrawable createDrawable(int color, int alpha, float radius) {
        float[] outerR = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAlpha(alpha);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        return shapeDrawable;
    }

    public static ShapeDrawable createDrawable(int color, float radius) {
        return createDrawable(color, 255, radius);
    }

    public static ShapeDrawable createDrawable(int color) {
        return createDrawable(color, 0);
    }
}
