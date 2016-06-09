package pl.coreorb.mememolder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.coreorb.mememolder.MemeGenerator;
import pl.coreorb.mememolder.R;
import pl.coreorb.mememolder.data.Meme;

/**
 * Fragment displaying created meme with share option.
 */
public class ResultFragment extends Fragment {

    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.share)
    Button share;

    public ResultFragment() {
        //required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void createMeme(Meme meme) {
        Bitmap memeBitmap = MemeGenerator.createMeme(getContext(), meme);
        boolean memeSaved = MemeGenerator.saveTempBitmap(getContext().getApplicationContext(), memeBitmap);
        if (memeSaved) {
            photo.setImageDrawable(new BitmapDrawable(getResources(), memeBitmap));
        } else {
            Toast.makeText(getContext(), R.string.error_save_meme, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @OnClick(R.id.share)
    public void shareMeme() {
        //get uri of temp file
        File tempBitmap = new File(MemeGenerator.getTempFilePath(getContext()));
        Uri uri = FileProvider.getUriForFile(getContext(), "pl.coreorb.mememolder.fileprovider", tempBitmap);

        //create intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.fragment_result_share_intent_text));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, getText(R.string.fragment_result_share_intent_title)));
    }
}
