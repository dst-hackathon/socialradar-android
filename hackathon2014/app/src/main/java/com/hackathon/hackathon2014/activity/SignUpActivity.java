package com.hackathon.hackathon2014.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.hackathon.hackathon2014.LoginUser;
import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.utility.ImageDownloader;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smileyOpal on 10/31/14.
 */
public class SignUpActivity extends Activity {
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_OPEN_CAMERA = 2;

    private final String BASE_SERVICE_URL = "http://api.radar.codedesk.com";
    private final String SIGNUP_SERVICE_URL = "/signup";
    private final String POST_AVATAR_SERVICE_URL = "/users/{id}/avatar";
    private final String GET_AVATAR_SERVICE_URL = "/users{id}/avatar";

    private ImageView _imageView;

    private String _imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setImageIconAction();
        setSignUpButtonAction();
        updateDisplayMode();
    }

    private void updateDisplayMode() {
        TextView screentitle = (TextView) findViewById(R.id.screentitle);

        if (LoginUser.isLogin()) {
            screentitle.setText("Edit Account");
            loadSignUpData();
        } else {
            screentitle.setText("Setup New Account");
            clearControls();
        }
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

        if (resultCode == RESULT_OK) {
            if ((requestCode == RESULT_LOAD_IMAGE) && (null != data)) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                _imagePath = cursor.getString(columnIndex);
                cursor.close();
                setImageIcon(_imagePath);

            } else if (requestCode == RESULT_OPEN_CAMERA) {
                galleryAddPic();
                setImageIcon(_imagePath);
            }
        }
    }

    ////////////////////////////////

    private void setImageIcon(String imagePath){
        ImageView imageView = (ImageView) findViewById(R.id.imageIcon);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
    }

    private void setImageIconAction() {
        _imageView = (ImageView) findViewById(R.id.imageIcon);
        _imageView.setDrawingCacheEnabled(true);
        _imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildImageOptionsDialog();
            }
        });
    }

    private void setSignUpButtonAction() {
        Button _signupButton = (Button) findViewById(R.id.signupbutton);

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
                    displayToast("Please fill in all information!");
                } else if (isNotMatch(_password, _cPassword)) {
                    displayToast("Password mismatch!");
                } else {
                    registerNewAccount(_name, _username, _password, _email);
                }
            }
        });
    }

    ////////////////////////////////

    private void registerNewAccount(EditText name, EditText username, EditText password, EditText email) {
        RegisterInfo registerInfo = setupRegisterInfo(name, username, password, email);
        submitRequest(registerInfo);
    }

    private void submitRequest(RegisterInfo registerInfo) {
        RestTemplate restTemplate = new RestTemplate();

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(mediaTypes);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null, httpHeaders);

        RegisterInfo res = restTemplate.postForObject(SIGNUP_SERVICE_URL, registerInfo, RegisterInfo.class, httpEntity);
    }

    private void loadSignUpData() {
        //get userid
        //get register information from user id
        new ImageDownloader( _imageView ).execute( "http://api.radar.codedeck.com/users/" + 2 +  "/avatar" );
    }

    /////////////////////////////////

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
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(photoFile));
                                    startActivityForResult(intent, RESULT_OPEN_CAMERA);
                                }
                            }
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        _imagePath = "file:" + image.getAbsolutePath();
        _imagePath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(_imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void clearControls() {
        EditText _name = (EditText) findViewById(R.id.name);
        EditText _username = (EditText) findViewById(R.id.username);
        EditText _password = (EditText) findViewById(R.id.password);
        EditText _cPassword = (EditText) findViewById(R.id.confirmpassword);
        EditText _email = (EditText) findViewById(R.id.email);

        _name.setText("");
        _username.setText("");
        _password.setText("");
        _cPassword.setText("");
        _email.setText("");
    }

    private boolean isNotMatch(EditText password, EditText cPassword) {
        return !getEditTextValue(password).equals(getEditTextValue(cPassword));
    }

    private boolean hasEmptyValue(EditText name) {
        return (null == name) || (null == name.getText()) || "".equals(getEditTextValue(name));
    }

    private String getEditTextValue(EditText editText) {
        return editText.getText().toString();
    }

    private void displayToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }

    private RegisterInfo setupRegisterInfo(EditText name, EditText username, EditText password, EditText email) {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setDisplayName(getEditTextValue(name));
        registerInfo.setUsername(getEditTextValue(username));
        registerInfo.setPassword(getEditTextValue(password));
        registerInfo.setEmail(getEditTextValue(email));

        Bitmap bitmap = Bitmap.createBitmap(_imageView.getDrawingCache());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        registerInfo.setFile(stream.toByteArray());
        return registerInfo;
    }
}
