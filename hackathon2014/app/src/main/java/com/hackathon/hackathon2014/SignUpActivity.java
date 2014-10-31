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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by smileyOpal on 10/31/14.
 */
public class SignUpActivity extends Activity {
    private final String USERNAME = "SIGNUP_USERNAME";
    private final String PASSWORD = "SIGNUP_PASSWORD";
    private final String DISPLAY_NAME = "SIGNUP_DISPLAY_NAME";

    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_OPEN_CAMERA = 2;

    private ImageView _imageView;
    private Button _signupButton;

    private String _imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setImageIconAction();
        setSignUpButton();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView = (ImageView) findViewById(R.id.imageIcon);

        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            } else if (requestCode == RESULT_OPEN_CAMERA) {
                galleryAddPic();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    private void setImageIconAction() {
        _imageView = (ImageView) findViewById(R.id.imageIcon);
        _imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildImageOptionsDialog();
            }
        });
    }

    private void setSignUpButton() {
        _signupButton = (Button) findViewById(R.id.signupbutton);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText _name = (EditText) findViewById(R.id.name);
                EditText _username = (EditText) findViewById(R.id.username);
                EditText _password = (EditText) findViewById(R.id.password);
                EditText _cPassword = (EditText) findViewById(R.id.confirmpassword);
                EditText _email = (EditText) findViewById(R.id.email);

                if (hasEmptyValue(_name) || hasEmptyValue(_username) || hasEmptyValue(_password)
                        || hasEmptyValue(_cPassword) || hasEmptyValue(_email)) {
                    createErrorDialog("Please fill in all information!");
                } else if (isNotMatch(_password, _cPassword)) {
                    createErrorDialog("Password mismatch!");
                }
                else {
//                    submitSignUpData();
                }
            }
        });
    }

    private void createErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR");
        builder.setMessage(message);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    private boolean isNotMatch(EditText password, EditText cPassword) {
        return !getEditTextValue(password).equals(getEditTextValue(cPassword));
    }

    private String getEditTextValue(EditText editText) {
        return editText.getText().toString();
    }

    private boolean hasEmptyValue(EditText name) {
        return (null == name) || (null == name.getText()) || "".equals(getEditTextValue(name));
    }

    private void buildImageOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("image options")
                .setItems(R.array.signup_image_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Resources res = getResources();
                        if (i == 0) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, RESULT_LOAD_IMAGE);
                        } else if (i == 1) {
                            _imagePath = Environment.getExternalStorageDirectory()
                                    + "/images/socialRadar/"
                                    + String.valueOf(System.currentTimeMillis())
                                    + "uploadImage"
                                    + ".jpg";

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, _imagePath);

                            startActivityForResult(intent, RESULT_OPEN_CAMERA);
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    //not working yet
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(_imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
