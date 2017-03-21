package com.example.op.tutorial678;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tutorial6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial6);

        ArrayList<Contact> contacts = getListContacts();
        final ContactAdapter contactAdapter = new ContactAdapter(this, contacts);

        ListView listView =(ListView) findViewById(R.id.listViewContacts);
        listView.setAdapter(contactAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Toast.makeText(Tutorial6.this, "row click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<Contact> getListContacts() {
        ArrayList<Contact> list = new ArrayList<>();
        int []idAvatars = {R.drawable.avatar, R.drawable.avatar2, R.drawable.avatar3};
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "1", "+84938743096"));
        list.add(new Contact(idAvatars, "tphien@gmail.com", "2", "+841267861996"));
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "3", "+84938743096"));
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "4", "+84938743096"));
        list.add(new Contact(idAvatars, "tphien@gmail.com", "5", "+841267861996"));
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "6", "+84938743096"));
        list.add(new Contact(idAvatars, "tphien@gmail.com", "7", "+841267861996"));
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "8", "+84938743096"));
        list.add(new Contact(idAvatars, "tphien@gmail.com", "9", "+841267861996"));
        list.add(new Contact(idAvatars, "phlinh@gmail.com", "10", "+84938743096"));

        return list;
    }
}
