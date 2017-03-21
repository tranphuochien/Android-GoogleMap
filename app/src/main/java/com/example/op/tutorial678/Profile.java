package com.example.op.tutorial678;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    String name;
    String email;
    String phoneNumber;
    int idAvatar;
    private boolean speech = false;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tts = new TextToSpeech(Profile.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    } else {
                        speech = true;
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phoneNumber");
        idAvatar = intent.getExtras().getInt("idAvatar");

        ((TextView)findViewById(R.id.user_profile_name)).setText(name);
        ((TextView)findViewById(R.id.tvEmail)).setText(email);
        ((TextView)findViewById(R.id.tvNumberPhone)).setText(phoneNumber);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.user_profile_photo);
        circleImageView.setImageResource(idAvatar);
    }
    public void onClickCallBut(View view) {
        Uri number = Uri.parse("tel: " + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL, number);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Profile.this.startActivity(intent);
    }

    public void onClickSmsBut(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", "Hi " + name);
        intent.putExtra("address", phoneNumber);

        Profile.this.startActivity(intent);
    }

    public void onClickEmailBut(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT,"body");

        Profile.this.startActivity(Intent.createChooser(intent, "Send email to " + name));
    }

    public void tvOnClickProfileBio(View view) {
        Toast.makeText(Profile.this, "Please Listen to me !!!", Toast.LENGTH_SHORT).show();
        if (speech) {
            String utteranceId = UUID.randomUUID().toString();
            TextView tv = (TextView)findViewById(R.id.user_profile_short_bio);

            tts.speak(tv.getText().toString(),TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }
}
