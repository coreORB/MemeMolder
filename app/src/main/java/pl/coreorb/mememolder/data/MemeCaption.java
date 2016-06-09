package pl.coreorb.mememolder.data;

/**
 * Created by ZaYeR on 2016-06-08.
 */

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

/**
 * POJO for meme's captions.
 */
public class MemeCaption implements Parcelable {

    private String text;
    private
    @ColorInt
    int colorText;
    private
    @ColorInt
    int colorOutline;

    public MemeCaption() {
        text = "";
        colorText = Color.WHITE;
        colorOutline = Color.BLACK;
    }

    public MemeCaption(Parcel in) {
        text = in.readString();
        colorText = in.readInt();
        colorOutline = in.readInt();
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public
    @ColorInt
    int getColorText() {
        return colorText;
    }

    public void setColorText(@ColorInt int colorText) {
        this.colorText = colorText;
    }

    public
    @ColorInt
    int getColorOutline() {
        return colorOutline;
    }

    public void setColorOutline(@ColorInt int colorOutline) {
        this.colorOutline = colorOutline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "MemeCaption{" +
                ", text='" + text + '\'' +
                ", colorText=" + colorText +
                ", colorOutline=" + colorOutline +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(colorText);
        dest.writeInt(colorOutline);
    }

    public static final Creator<MemeCaption> CREATOR = new Creator<MemeCaption>() {

        @Override
        public MemeCaption createFromParcel(Parcel source) {
            return new MemeCaption(source);
        }

        @Override
        public MemeCaption[] newArray(int size) {
            return new MemeCaption[size];
        }
    };


}
