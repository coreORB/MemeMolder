package pl.coreorb.mememolder.photodialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import pl.coreorb.mememolder.R;

/**
 * Dialog for selecting photos with Builder pattern.
 */
public class PhotoSelectDialog extends DialogFragment {

    private static final String LOG_TAG = PhotoSelectDialog.class.getSimpleName();

    private ArrayList<Integer> mItems;
    private OnPhotoSelectedListener mListener;

    /**
     * Class that will help you build colors selection dialog.
     */
    public static class Builder {
        private Context context;
        private OnPhotoSelectedListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Sets listener to receive callbacks when photo is selected.
         *
         * @param listener listener which will receive callbacks
         * @return this {@link Builder} to let methods chaining
         */
        public Builder setOnPhotoSelectedListener(OnPhotoSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public PhotoSelectDialog build() {
            PhotoSelectDialog dialog = new PhotoSelectDialog();
            dialog.setOnPhotoSelectedListener(listener);
            return dialog;
        }

    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.photo_dialog_title);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //get photos
        mItems = new ArrayList<>();
        mItems.add(R.drawable.meme_photo_01);
        mItems.add(R.drawable.meme_photo_02);
        mItems.add(R.drawable.meme_photo_03);
        mItems.add(R.drawable.meme_photo_04);
        mItems.add(R.drawable.meme_photo_05);
        mItems.add(R.drawable.meme_photo_06);
        mItems.add(R.drawable.meme_photo_07);
        mItems.add(R.drawable.meme_photo_08);
        mItems.add(R.drawable.meme_photo_09);
        mItems.add(R.drawable.meme_photo_10);
        //create grid view and add it to dialog
        GridView gridView = new GridView(getContext());
        gridView.setNumColumns(3);
        //gridView.setAdapter(new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1));
        gridView.setAdapter(new GridViewAdapter(getContext(), mItems));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onPhotoSelected(mItems.get(position));
                }
                PhotoSelectDialog.this.dismiss();
            }
        });
        builder.setView(gridView);

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListener = null;
    }

    /**
     * Listener to receive callbacks when photo in dialog is selected.
     */
    public interface OnPhotoSelectedListener {
        /**
         * Called when photo in dialog is being selected.
         *
         * @param selectedPhotoResId resource id of selected photot
         */
        void onPhotoSelected(@DrawableRes int selectedPhotoResId);
    }
}