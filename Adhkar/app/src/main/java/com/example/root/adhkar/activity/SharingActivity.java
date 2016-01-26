package com.example.root.adhkar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.root.adhkar.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 23/07/15.
 */
public class SharingActivity extends AppCompatActivity {
    String msgShare = "";
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    protected TextView msgfb;
    protected Button cancelfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        msgfb = (TextView) findViewById(R.id.msgFB);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            msgShare = ((String) b.get("testd"));

        }

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                msgfb.setText(getResources().getString(R.string.seccessshare));
                sharePhotoToFacebook();
            }

            @Override
            public void onCancel() {

                msgfb.setText(getResources().getString(R.string.no_internet_connection));
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                msgfb.setText("sharing error status");

                System.out.println("onError");
            }
        });


    }

    private void sharePhotoToFacebook() {

        int photoid;
        if(msgShare.contains(getResources().getString(R.string.besmellaharab)))
        {
            photoid=R.drawable.aya_icon;
        }else photoid=R.drawable.quran_icon;


        Bitmap image = BitmapFactory.decodeResource(getResources(), photoid);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(msgShare)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}
