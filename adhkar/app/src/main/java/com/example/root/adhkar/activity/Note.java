package com.example.root.adhkar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.adhkar.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by root on 14/07/15.
 */
public class Note extends FragmentActivity {
    Activity aux = this;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutbackr);
        int[] tab = new int[3];
        tab[0] = R.drawable.tem1;
        tab[1] = R.drawable.tem2;
        tab[2] = R.drawable.tem3;
        Random r = new Random();

        int xi = r.nextInt(tab.length);


        layout.setBackgroundResource(tab[xi]);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(Note.this, "Share Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Note.this, "Share Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Note.this, exception.getMessage(), Toast.LENGTH_LONG).show();


                Log.e("TAG", exception.getMessage());

                //      exception.printStackTrace();

            }

        });

        TextView view = ((TextView) this.findViewById(R.id.doaa));
        final String[] tabDoaa = getResources().getStringArray(R.array.listadhkarnotification);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            i = Integer.parseInt((String) b.get("numd"));

        }

        view.setText(tabDoaa[i]);


        // final ImageView img = (ImageView) findViewById(R.id.imageView);
        ImageButton sharefb = (ImageButton) findViewById(R.id.sharefb);


        sharefb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Note.this, SharingActivity.class);
                intent.putExtra("testd", tabDoaa[i]);

                startActivity(intent);


            }
        });
        final ImageButton share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareingIntent = new Intent(Intent.ACTION_SEND);
                String shareBody =tabDoaa[i];
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.share)));


                share();

                //           initShareIntent("",tabDoaa[i]);
            }
        });


    }

    private void postFB(Bitmap bm) {
        SharePhoto photo = new SharePhoto.Builder().setBitmap(bm).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        ShareDialog dialog = new ShareDialog(this);
        if (dialog.canShow(SharePhotoContent.class)) {
            dialog.show(content);
        } else {
            Log.d("Activity", "you cannot share photos :(");
        }

    }


    void share() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.tem3);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_TEXT, "hello #test");

        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private void initShareIntent(String type, String _text) {
        File filePath = getFileStreamPath("icon.png");  //optional //internal storage
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, _text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));  //optional//use this when you want to send an image
        shareIntent.setType("image/png");
        //Uri uri = Uri.parse("android.resource://"getresour"/drawable/image_name");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
   /* private void shareOnFacebook(String content, String url)
    {
        try
        {
            // TODO: This part does not work properly based on my test
            Intent fbIntent = new Intent(Intent.ACTION_SEND);
            fbIntent.setType("text/plain");
            fbIntent.putExtra(Intent.EXTRA_TEXT, content);
            fbIntent.putExtra(Intent.EXTRA_STREAM, url);
            fbIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            fbIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            fbIntent.setComponent(new ComponentName("com.facebook.katana",
                    "com.facebook.composer.shareintent.ImplicitShareIntentHandler"));

            startActivity(fbIntent);
            return;
        }
        catch (Exception e)
        {
            // User doesn't have Facebook app installed. Try sharing through browser.
        }

        // If we failed (not native FB app installed), try share through SEND
        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
        SupportUtils.doShowUri(this.getActivity(), sharerUrl);
    }
    public void shareImage(Bitmap image){
        ShareDialog facebookDialog = new ShareDialog(this);
        SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        facebookDialog.show(content);

    }*/
}
