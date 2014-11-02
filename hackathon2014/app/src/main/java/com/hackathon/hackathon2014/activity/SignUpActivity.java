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
import com.hackathon.hackathon2014.utility.AlertMessageDialogue;
import com.hackathon.hackathon2014.utility.ImageDownloader;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smileyOpal on 10/31/14.
 */
public class SignUpActivity extends Activity {
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_OPEN_CAMERA = 2;
    private final String MODE_REGISTER = "MODE_REGISTER";
    private final String MODE_EDIT = "MODE_EDIT";

    private final String BASE_SERVICE_URL = "http://api.radar.codedeck.com";
    private final String SIGNUP_SERVICE_URL = BASE_SERVICE_URL + "/signup";
    private final String POST_AVATAR_SERVICE_URL = BASE_SERVICE_URL + "/users/{id}/avatar";
    private final String GET_AVATAR_SERVICE_URL = BASE_SERVICE_URL + "/users{id}/avatar";

    private ImageView _imageView;
    private Button _signupButton;

    private String _imagePath;
    private String mode;

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
            _signupButton.setText("Update");
            mode = MODE_EDIT;
            loadSignUpData();
        } else {
            screentitle.setText("Setup New Account");
            _signupButton.setText("Create");
            mode = MODE_REGISTER;
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

    private void setImageIcon(String imagePath) {
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

    ////////////////////////////////

    private void registerNewAccount(EditText name, EditText username, EditText password, EditText email) {
        RegisterInfo registerInfo = setupRegisterInfo(name, username, password, email);
        submitRequest(registerInfo);
    }

    private void submitRequest(RegisterInfo registerInfo) {
        RestProvider.requestSignUp(registerInfo, new PostRequestHandler<String>() {
            @Override
            public void handle(String result) {
                if ("success".equals(result)) {
                    if (MODE_REGISTER.equals(mode)) {
                        startActivity(new Intent(SignUpActivity.this, QuestionActivity.class));
                    }
                } else {
                    displayToast(result + " Please try again!");
                }
            }
        });
    }

    private void loadSignUpData() {
        loginWithSignUpAccount();

        RegisterInfo registerInfo = LoginUser.getLoginUser();
        new ImageDownloader(_imageView).execute("http://api.radar.codedeck.com/users/" + registerInfo.getId() + "/avatar");
    }

    private void loginWithSignUpAccount() {
        RegisterInfo registerInfo = LoginUser.getLoginUser();
        RestProvider.loginUser(registerInfo.getEmail(), registerInfo.getPassword(), new PostRequestHandler<RegisterInfo>() {
            @Override
            public void handle(RegisterInfo registerInfo) {
                if ((registerInfo != null) &&
                        (registerInfo.getSuccess() != null) && ("success".equalsIgnoreCase(registerInfo.getSuccess()))) {
                    registerInfo.setEmail(registerInfo.getEmail());
                    registerInfo.setPassword(registerInfo.getPassword());
                    LoginUser.setLoginUser(registerInfo);
                }
            }
        });
        setupLoadedAccount(registerInfo);
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

    private void setupLoadedAccount(RegisterInfo registerInfo){
        EditText _name = (EditText) findViewById(R.id.name);
        EditText _username = (EditText) findViewById(R.id.username);
        EditText _password = (EditText) findViewById(R.id.password);
        EditText _cPassword = (EditText) findViewById(R.id.confirmpassword);
        EditText _email = (EditText) findViewById(R.id.email);

        _name.setText("");
        _username.setText("");
        _password.setText(registerInfo.getPassword());
        _cPassword.setText(registerInfo.getPassword());
        _email.setText(registerInfo.getEmail());
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

        if (null != _imagePath) {
            registerInfo.setFile(new File(_imagePath));
        }
        return registerInfo;
    }
}
