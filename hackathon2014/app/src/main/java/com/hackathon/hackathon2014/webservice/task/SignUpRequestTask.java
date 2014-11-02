package com.hackathon.hackathon2014.webservice.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
//import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by smileyOpal on 11/1/14.
 */
public class SignUpRequestTask extends AsyncTask<RegisterInfo, Void, String> {

    private final String url = "http://api.radar.codedeck.com/signup";
    private RestTemplate restTemplate;
    private PostRequestHandler<String> handler;

    public SignUpRequestTask(RestTemplate restTemplate, PostRequestHandler<String> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(RegisterInfo... registerInfos) {
        String boundary = "--eriksboundary--";
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, boundary, Charset.defaultCharset());
        try {
            RegisterInfo registerInfo = registerInfos[0];
            entity.addPart("email", new StringBody(registerInfo.getEmail()));
            entity.addPart("password", new StringBody(registerInfo.getPassword()));

            if (null != registerInfo.getFile()) {
                entity.addPart("file", new FileBody(registerInfo.getFile()));

//                Bitmap bitmap = BitmapFactory.decodeFile(registerInfo.getFile().getAbsolutePath());
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
//                byte[] data = bos.toByteArray();
//                ByteArrayBody bab = new ByteArrayBody(data, registerInfo.getFile().getName());
//                entity.addPart("file", bab);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpParams params = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(params, 5000);
//        HttpConnectionParams.setSoTimeout(params, 5000);

        HttpClient client = new DefaultHttpClient(params);
        HttpContext httpContext = new BasicHttpContext();

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        post.addHeader("Accept", "application/json");
        post.setEntity(entity);

        try {
            HttpResponse response = client.execute(post, httpContext);
            String responseStr = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                JSONObject jsonObject = new JSONObject(responseStr);
                String result = (String) jsonObject.get("success");
                if ("".equals(result)){
                    return "success";
                }
                return "fail";
            }
            else if (statusCode == HttpStatus.SC_BAD_REQUEST){
                JSONObject jsonObject = new JSONObject(responseStr);
                return (String) jsonObject.get("error");
            }

            return "Unable to signup";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @Override
    protected void onPostExecute(String result) {
        handler.handle(result);
    }
}
