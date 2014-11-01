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
import android.widget.Toast;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.model.RegisterInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smileyOpal on 10/31/14.
 */
public class SignUpActivity extends Activity {
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_OPEN_CAMERA = 2;
    private final String BASE_SERVICE_URL = "http://api.radar.codedesk.com";
    private final String LOGIN_SERVICE_URL = "";
    private final String POST_AVATAR_SERVICE_URL = "/users/{id}/avatar";
    private final String GET_AVATAR_SERVICE_URL = "/users{id}/avatar";

    public final String USERNAME = "SIGNUP_USERNAME";
    private final String PASSWORD = "SIGNUP_PASSWORD";

    public final String DISPLAY_NAME = "SIGNUP_DISPLAY_NAME";
    public final String SIGNUP_MODE = "SIGNUP_MODE";
    public final String MODE_NEW_ACCT = "MODE_NEW_ACCT";

    public final String MODE_EDIT_ACCT = "MODE_EDIT_ACCT";

    private ImageView _imageView;
    private Button _signupButton;

    private String _imagePath;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Bundle extras = getIntent().getExtras();

        if (null != extras && null != extras.getString(SIGNUP_MODE)) {
            mode = extras.getString(SIGNUP_MODE);
        }
        else {
            mode = MODE_NEW_ACCT;
        }

        if(mode.equals(MODE_NEW_ACCT)) {
            clearControls();
        }
        else {
            loadSignUpData();
        }

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
        _imageView.setDrawingCacheEnabled(true);
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
                    displayToast("Please fill in all information!");
                } else if (isNotMatch(_password, _cPassword)) {
                    displayToast("Password mismatch!");
                } else {
                    registerNewAccount(_name, _username, _password, _email);
                }
            }
        });
    }

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
        HttpEntity<String> httpEntity = new HttpEntity<String>(null,httpHeaders);

        RegisterInfo res = restTemplate.postForObject(LOGIN_SERVICE_URL, registerInfo, RegisterInfo.class, httpEntity);
    }

    private RegisterInfo setupRegisterInfo(EditText name, EditText username, EditText password, EditText email) {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setDisplayName(getEditTextValue(name));
        registerInfo.setUsername(getEditTextValue(username));
        registerInfo.setPassword(getEditTextValue(password));
        registerInfo.setEmail(getEditTextValue(email));

        Bitmap bitmap = Bitmap.createBitmap(_imageView.getDrawingCache());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        registerInfo.setImage(stream.toByteArray());
        return registerInfo;
    }

    private void displayToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
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

    private void loadSignUpData() {

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
