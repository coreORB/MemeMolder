package pl.coreorb.mememolder.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import pl.coreorb.mememolder.R;

/**
 * POJO for meme.
 */
public class Meme implements Parcelable {

    private
    @DrawableRes
    int photo;
    private MemeCaption topCaption;
    private MemeCaption bottomCaption;

    public Meme() {
        photo = R.drawable.fragment_editor_no_photo;
        topCaption = new MemeCaption();
        bottomCaption = new MemeCaption();
    }

    public Meme(Parcel in) {
        photo = in.readInt();
        topCaption = in.readParcelable(MemeCaption.class.getClassLoader());
        bottomCaption = in.readParcelable(MemeCaption.class.getClassLoader());
    }

    public Meme(Meme other) {
        photo = other.photo;
        topCaption = new MemeCaption(other.topCaption);
        bottomCaption = new MemeCaption(other.bottomCaption);
    }

    public boolean isEmpty() {
        if (photo == R.drawable.fragment_editor_no_photo
                && topCaption.getText().equals("")
                && bottomCaption.getText().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public
    @DrawableRes
    int getPhoto() {
        return photo;
    }

    public void setPhoto(@DrawableRes int photo) {
        this.photo = photo;
    }

    public MemeCaption getTopCaption() {
        return topCaption;
    }

    public void setTopCaption(MemeCaption topCaption) {
        this.topCaption = topCaption;
    }

    public MemeCaption getBottomCaption() {
        return bottomCaption;
    }

    public void setBottomCaption(MemeCaption bottomCaption) {
        this.bottomCaption = bottomCaption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Meme{" +
                "photo=" + photo +
                ", topCaption=" + topCaption +
                ", bottomCaption=" + bottomCaption +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photo);
        dest.writeParcelable(topCaption, 0);
        dest.writeParcelable(bottomCaption, 0);
    }

    public static final Creator<Meme> CREATOR = new Creator<Meme>() {

        @Override
        public Meme createFromParcel(Parcel source) {
            return new Meme(source);
        }

        @Override
        public Meme[] newArray(int size) {
            return new Meme[size];
        }
    };


}
