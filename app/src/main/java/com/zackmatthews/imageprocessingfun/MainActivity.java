package com.zackmatthews.imageprocessingfun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_PICKER_ACTIVITY=91;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PHOTO_PICKER_ACTIVITY);
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_PICKER_ACTIVITY){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(selectedImage);

                new AnalyzeColorTask(selectedImage, findViewById(R.id.bg)).execute();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setProgressBarVisible(boolean isVisible){
        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }



    public class AnalyzeColorTask extends AsyncTask<Void, Void, Void> {

        private Bitmap bmp;
        private View bgView;
        private int avgColor = -1;
        public AnalyzeColorTask(Bitmap bmp, View bgView){
            this.bmp = bmp;
            this.bgView = bgView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarVisible(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bgView.setBackgroundColor(avgColor);
            setProgressBarVisible(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bmp = Bitmap.createScaledBitmap(bmp, 352, 240, true); //Scale to cif
            avgColor = ImageProcessor.getInstance().getAverageColor(bmp);
            return null;
        }
    }
}
