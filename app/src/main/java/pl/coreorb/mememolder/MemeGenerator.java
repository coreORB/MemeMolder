package pl.coreorb.mememolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.coreorb.mememolder.data.Meme;
import pl.coreorb.mememolder.data.MemeCaption;

/**
 * Class for generating memes.
 */
public class MemeGenerator {

    private static final String LOG_TAG = MemeGenerator.class.getSimpleName();

    private static final int MEME_SIZE = 500;
    private static final int TEXT_SIZE = 30;
    private static int TEXT_LENGTH;

    private static final String TEMP_IMAGES_DIR_NAME = "images";
    private static final String TEMP_BITMAP_FILENAME = "tempMeme.jpg";

    public static Bitmap createMeme(Context context, @NonNull Meme meme) {
        TEXT_LENGTH = context.getResources().getInteger(R.integer.meme_caption_max_length);
        Bitmap bitmap = getScaledPhoto(context, meme.getPhoto());

        Canvas canvas = new Canvas(bitmap);
        if (!meme.getTopCaption().getText().equals("")) {
            drawCaptionOnCanvas(canvas, meme.getTopCaption(), TEXT_SIZE);
        }
        if (!meme.getBottomCaption().getText().equals("")) {
            drawCaptionOnCanvas(canvas, meme.getBottomCaption(), canvas.getHeight() - 10);
        }

        return bitmap;
    }

    private static void drawCaptionOnCanvas(Canvas canvas, MemeCaption caption, int positionY) {
        Paint paint = new Paint();
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);

        String text;
        if (caption.getText().length() > TEXT_LENGTH) {
            Log.e(LOG_TAG, "Caption too long. Caption truncated!");
            text = caption.getText().substring(0, TEXT_LENGTH);
        } else {
            text = caption.getText();
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(caption.getColorOutline());
        canvas.drawText(text, canvas.getWidth() / 2, positionY, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(caption.getColorText());
        canvas.drawText(text, canvas.getWidth() / 2, positionY, paint);
    }

    public static Bitmap getScaledPhoto(Context context, @DrawableRes int imageResId) {
        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                imageResId).copy(Bitmap.Config.ARGB_8888, true);

        Bitmap background = Bitmap.createBitmap(MEME_SIZE, MEME_SIZE, Bitmap.Config.ARGB_8888);
        float originalWidth = image.getWidth();
        float originalHeight = image.getHeight();
        Canvas canvas = new Canvas(background);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        float scale = MEME_SIZE / originalWidth;
        float xTranslation = 0.0f;
        float yTranslation = (MEME_SIZE - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        paint.setFilterBitmap(true);
        canvas.drawBitmap(image, transformation, paint);
        return image;
    }

    public static boolean saveTempBitmap(Context appContext, Bitmap bitmap) {
        boolean result = false;
        File dir = new File(appContext.getCacheDir(), TEMP_IMAGES_DIR_NAME);
        dir.mkdirs();
        File file = new File(getTempFilePath(appContext));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap readTempBitmap(Context appContext) {
        try {
            File file = new File(getTempFilePath(appContext));
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTempFilePath(Context context) {
        return context.getCacheDir() + File.separator + TEMP_IMAGES_DIR_NAME + File.separator + TEMP_BITMAP_FILENAME;
    }

}
