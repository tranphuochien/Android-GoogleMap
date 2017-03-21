package com.example.op.tutorial678;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


import java.util.ArrayList;


/**
 * Created by OP on 3/9/2017.
 */

class ContactAdapter extends ArrayAdapter<Contact>{
    private final Context context;
    private static int position;

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
    }
    private static class ViewHolder {
        TextView tvName;
        TextView tvNumberPhone;
        CircleImageView circleImageView;
        Button detailBut;
    }
    public void selectedItem(int position)
    {
        ContactAdapter.position = position; //position must be a global variable
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder vh; // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            vh = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
            vh.tvNumberPhone = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            vh.circleImageView = (CircleImageView) convertView.findViewById(R.id.imageViewAvatar);
            vh.detailBut = (Button) convertView.findViewById(R.id.detailBut);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(vh);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor((TextView)view,"#EF4836", "#446cb3" ,2000,100);
            }
        });

        vh.tvNumberPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextColor((TextView)view,"#EF4836", "#6C7A89" ,2000,100);
            }
        });

        vh.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] idAvatars = getItem(position).idAvatars;
                int idx = getItem(position).idxAvatar;
                int idAvatar;
                int n = idAvatars.length;

                if(idx == (n-1)) {
                    getItem(position).setIdxAvatar(0);
                    idAvatar = idAvatars[0];
                } else {
                    idx += 1;
                    getItem(position).setIdxAvatar(idx);
                    idAvatar = idAvatars[idx];
                }
                ((CircleImageView)view).setImageResource(idAvatar);
            }
        });

        vh.detailBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Profile.class );

                intent.putExtra("phoneNumber", getItem(position).phoneNumber);
                intent.putExtra("name", getItem(position).name);
                intent.putExtra("email", getItem(position).email);
                intent.putExtra("idAvatar", getItem(position).idAvatars[getItem(position).idxAvatar]);
                context.startActivity(intent);
            }
        });

        vh.tvName.setText(contact.name);
        vh.tvName.setTextColor(Color.parseColor("#446cb3"));
        vh.tvNumberPhone.setText(contact.phoneNumber);
        vh.tvName.setTextColor(Color.parseColor("#6C7A89"));
        vh.circleImageView.setImageResource(getItem(position).idAvatars[getItem(position).idxAvatar]);


        return convertView;
    }

    private static void changeTextColor(final TextView textView, String sColor, String eColor,
                                        final long animDuration, final long animUnit){
        if (textView == null) return;

        int startColor = Color.parseColor(sColor);
        int endColor = Color.parseColor(eColor);

        final int startRed = Color.red(startColor);
        final int startBlue = Color.blue(startColor);
        final int startGreen = Color.green(startColor);

        final int endRed = Color.red(endColor);
        final int endBlue = Color.blue(endColor);
        final int endGreen = Color.green(endColor);

        new CountDownTimer(animDuration, animUnit){
            //animDuration is the time in ms over which to run the animation
            //animUnit is the time unit in ms, update color after each animUnit
            @Override
            public void onTick(long l) {
                int red = (int) (endRed + (l * (startRed - endRed) / animDuration));
                int blue = (int) (endBlue + (l * (startBlue - endBlue) / animDuration));
                int green = (int) (endGreen + (l * (startGreen - endGreen) / animDuration));

                //Log.d("Changing color", "Changing color to RGB" + red + ", " + green + ", " + blue);
                textView.setTextColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onFinish() {
                textView.setTextColor(Color.rgb(endRed, endGreen, endBlue));
            }
        }.start();
    }


}
