package com.example.op.tutorial678;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Tutorial8 extends AppCompatActivity {
    ListTrailerAdapter adapter;
    ArrayList<Trailer> trailerArrayList;
    ListView listViewTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial8);

        initControl();
        loadData();
    }

    private void loadData() {
        trailerArrayList.add(new Trailer("Big hero 6", "https://www.youtube.com/watch?v=z3biFxZIJOQ",
                BitmapFactory.decodeResource(getResources(), R.drawable.bighero6)));
        trailerArrayList.add(new Trailer("Avatar", "https://www.youtube.com/watch?v=cRdxXPV9GNQ",
                BitmapFactory.decodeResource(getResources(), R.drawable.avatar_film)));
        trailerArrayList.add(new Trailer("Return XXX", "https://www.youtube.com/watch?v=uisBaTkQAEs",
                BitmapFactory.decodeResource(getResources(), R.drawable.xxx_return)));
        trailerArrayList.add(new Trailer("Fast and furious 8", "https://www.youtube.com/watch?v=uisBaTkQAEs",
                BitmapFactory.decodeResource(getResources(), R.drawable.ff8)));
        adapter.addAll(trailerArrayList);

    }

    private void initControl() {
        listViewTrailer = (ListView) findViewById(R.id.list_view_trailer);
        adapter = new ListTrailerAdapter(Tutorial8.this);
        trailerArrayList = new ArrayList<>();
        listViewTrailer.setAdapter(adapter);

        listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trailer trailer = (Trailer) listViewTrailer.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });
    }
}
