package com.hackathon.hackathon2014;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;

/**
 * Created by smileyOpal on 10/31/14.
 */
public class SignUpActivity extends Activity {
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_OPEN_CAMERA = 2;
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setImageIconAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImageIconAction() {
        imageView = (ImageView) findViewById(R.id.imageIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("image options")
                        .setItems(R.array.signup_image_option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Resources res = getResources();
                                if (i == 0) {
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                                } else if (i == 1) {
                                    imagePath = Environment.getExternalStorageDirectory()
                                            + "/images/socialRadar/"
                                            + String.valueOf(System.currentTimeMillis())
                                            + "uploadImage"
                                            + ".jpg";

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);

                                    startActivityForResult(intent, RESULT_OPEN_CAMERA);
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = (ImageView) findViewById(R.id.imageIcon);

        if(resultCode == RESULT_OK && null != data) {

            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }
            else if (requestCode == RESULT_OPEN_CAMERA){
                galleryAddPic();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    //not working yet
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
