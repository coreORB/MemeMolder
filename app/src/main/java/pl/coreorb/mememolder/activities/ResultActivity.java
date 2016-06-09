package pl.coreorb.mememolder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.File;

import pl.coreorb.mememolder.MemeGenerator;
import pl.coreorb.mememolder.R;
import pl.coreorb.mememolder.data.Meme;

/**
 * Activity displaying created meme with share option.
 */
public class ResultActivity extends AppCompatActivity {

    public static final String ARG_MEME = "ARG_MEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get fragment
        ResultFragment resultFragmentFragment = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        Bundle args = getIntent().getExtras();
        if (args != null && args.containsKey(ARG_MEME)) {
            Meme meme = args.getParcelable(ARG_MEME);
            resultFragmentFragment.createMeme(meme);
        }
    }

    @Override
    public void onBackPressed() {
        //delete temp file on exit
        File tempBitmap = new File(MemeGenerator.getTempFilePath(this));
        if (tempBitmap.exists()) {
            tempBitmap.delete();
        }

        super.onBackPressed();
    }
}