package pl.coreorb.mememolder.activities;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.coreorb.mememolder.MemeGenerator;
import pl.coreorb.mememolder.R;
import pl.coreorb.mememolder.data.Meme;
import pl.coreorb.mememolder.photodialog.PhotoSelectDialog;
import pl.coreorb.selectiondialogs.data.SelectableColor;
import pl.coreorb.selectiondialogs.dialogs.ColorSelectDialog;

/**
 * Meme editor Fragment.
 */
public class EditorFragment extends Fragment implements ColorSelectDialog.OnColorSelectedListener, PhotoSelectDialog.OnPhotoSelectedListener {

    private static final String LOG_TAG = EditorFragment.class.getSimpleName();

    private static final String STATE_CURRENT_MEME = "STATE_CURRENT_MEME";
    private static final String STATE_LAST_DIALOG_ID = "STATE_LAST_DIALOG_ID";

    private static final String TAG_PHOTO_DIALOG = "TAG_PHOTO_DIALOG";
    private static final String TAG_COLOR_DIALOG = "TAG_COLOR_TOP_TEXT_DIALOG";


    @BindView(R.id.top_text)
    EditText topText;
    @BindView(R.id.top_color_text)
    ImageView topColorText;
    @BindView(R.id.top_color_outline)
    ImageView topColorOutline;
    @BindView(R.id.bottom_text)
    EditText bottomText;
    @BindView(R.id.bottom_color_text)
    ImageView bottomColorText;
    @BindView(R.id.bottom_color_outline)
    ImageView bottomColorOutline;
    @BindView(R.id.photo_button)
    RelativeLayout photoButton;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.no_photo_text)
    TextView noPhotoText;

    private int lastDialogId = 0;
    private Meme currentMeme;

    public EditorFragment() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            currentMeme = new Meme();
            populateCurrentMeme();
        }
    }

    @Override
    public void onResume() {
        ColorSelectDialog colorDialog = (ColorSelectDialog) getFragmentManager().findFragmentByTag(TAG_COLOR_DIALOG);
        if (colorDialog != null) {
            colorDialog.setOnColorSelectedListener(this);
        }

        PhotoSelectDialog photoDialog = (PhotoSelectDialog) getFragmentManager().findFragmentByTag(TAG_PHOTO_DIALOG);
        if (photoDialog != null) {
            photoDialog.setOnPhotoSelectedListener(this);
        }

        super.onResume();
    }

    private void populateCurrentMeme() {
        topText.setText(currentMeme.getTopCaption().getText());
        topColorText.setColorFilter(currentMeme.getTopCaption().getColorText());
        topColorOutline.setColorFilter(currentMeme.getTopCaption().getColorOutline());
        bottomText.setText(currentMeme.getBottomCaption().getText());
        bottomColorText.setColorFilter(currentMeme.getBottomCaption().getColorText());
        bottomColorOutline.setColorFilter(currentMeme.getBottomCaption().getColorOutline());
        if (currentMeme.getPhoto() == R.drawable.fragment_editor_no_photo) {
            noPhotoText.setVisibility(View.VISIBLE);
        } else {
            noPhotoText.setVisibility(View.GONE);
            photo.setImageDrawable(new BitmapDrawable(getResources(), MemeGenerator.getScaledPhoto(getContext(), currentMeme.getPhoto())));
        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        lastDialogId = savedInstanceState.getInt(STATE_LAST_DIALOG_ID);
        currentMeme = savedInstanceState.getParcelable(STATE_CURRENT_MEME);
        populateCurrentMeme();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_LAST_DIALOG_ID, lastDialogId);
        outState.putParcelable(STATE_CURRENT_MEME, currentMeme);
        super.onSaveInstanceState(outState);
    }


    @OnTextChanged(R.id.top_text)
    public void onTopTextChanged() {
        currentMeme.getTopCaption().setText(topText.getText().toString());
    }

    @OnTextChanged(R.id.bottom_text)
    public void onBottomTextChanged() {
        currentMeme.getBottomCaption().setText(bottomText.getText().toString());
    }

    @OnClick({R.id.top_color_text, R.id.top_color_outline, R.id.bottom_color_text, R.id.bottom_color_outline})
    public void showSelectColorDialog(View view) {
        lastDialogId = view.getId();

        new ColorSelectDialog.Builder(getContext())
                .setColors(R.array.color_names, R.array.color_names, R.array.color_values)
                .setOnColorSelectedListener(this)
                .build().show(getFragmentManager(), TAG_COLOR_DIALOG);

    }

    @Override
    public void onColorSelected(SelectableColor selectedItem) {
        switch (lastDialogId) {
            case R.id.top_color_text:
                currentMeme.getTopCaption().setColorText(selectedItem.getColorValue());
                topColorText.setColorFilter(selectedItem.getColorValue());
                break;
            case R.id.top_color_outline:
                currentMeme.getTopCaption().setColorOutline(selectedItem.getColorValue());
                topColorOutline.setColorFilter(selectedItem.getColorValue());
                break;
            case R.id.bottom_color_text:
                currentMeme.getBottomCaption().setColorText(selectedItem.getColorValue());
                bottomColorText.setColorFilter(selectedItem.getColorValue());
                break;
            case R.id.bottom_color_outline:
                currentMeme.getBottomCaption().setColorOutline(selectedItem.getColorValue());
                bottomColorOutline.setColorFilter(selectedItem.getColorValue());
                break;
            default:
                Log.e(LOG_TAG, "onColorSelected(): wrong lastDialogId value(" + lastDialogId + ")");
        }
    }

    @OnClick(R.id.photo_button)
    public void showSelectPhotoDialog() {
        new PhotoSelectDialog.Builder(getContext())
                .setOnPhotoSelectedListener(this)
                .build().show(getFragmentManager(), TAG_PHOTO_DIALOG);

    }

    public void onPhotoSelected(@DrawableRes int photoResId) {
        currentMeme.setPhoto(photoResId);
        photo.setImageDrawable(new BitmapDrawable(getResources(), MemeGenerator.getScaledPhoto(getContext(), photoResId)));

        if (noPhotoText.getVisibility() == View.VISIBLE) {
            noPhotoText.setVisibility(View.GONE);
        }
    }

    public void clearMeme() {
        currentMeme = new Meme();
        populateCurrentMeme();
    }

    public boolean isMemeFilled() {
        if (currentMeme.getPhoto() == R.drawable.fragment_editor_no_photo) {
            Toast.makeText(getContext(), R.string.error_no_photo, Toast.LENGTH_LONG).show();
            return false;
        } else if (currentMeme.getTopCaption().getText().equals("") && currentMeme.getBottomCaption().getText().equals("")) {
            Toast.makeText(getContext(), R.string.error_no_text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public Meme getCurrentMeme() {
        return currentMeme;
    }

}
