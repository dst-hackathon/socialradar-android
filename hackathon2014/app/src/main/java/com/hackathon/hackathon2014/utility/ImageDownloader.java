package com.hackathon.hackathon2014.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Sam on 1/11/2557.
 */
public class ImageDownloader extends AsyncTask< String, Void, Bitmap> {
    private ImageView bmImage;

    public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap mIcon = null;

        try {
            InputStream in  = new URL( url ).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch ( Exception e) {
            Log.d( "ERROR!" , e.getMessage());
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
