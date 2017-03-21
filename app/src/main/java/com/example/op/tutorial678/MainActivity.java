package com.example.op.tutorial678;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTutorial6(View view) {
        Intent intent = new Intent(MainActivity.this, Tutorial6.class );
        MainActivity.this.startActivity(intent);
    }

    public void onClickTutorial7(View view) {
    }

    public void onClickTutorial8(View view) {
        Intent intent = new Intent(MainActivity.this, Tutorial8.class );
        MainActivity.this.startActivity(intent);
    }

    public void onClickTutorial9(View view) {
        Intent intent = new Intent(MainActivity.this, Tutorial9.class );
        MainActivity.this.startActivity(intent);
    }

    public void onClickTutorial11(View view) {
        Intent intent = new Intent(MainActivity.this, Tutorial11.class );
        MainActivity.this.startActivity(intent);
    }

    public void onClickMyMap(View view) {
        Intent intent = new Intent(MainActivity.this, MyMap.class );
        MainActivity.this.startActivity(intent);
    }

    public void onClickMyCustomMarkerMap(View view) {
        Intent intent = new Intent(MainActivity.this, CustomMarker.class );
        MainActivity.this.startActivity(intent);
    }
}
