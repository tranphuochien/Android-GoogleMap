package com.example.op.tutorial678;

/**
 * Created by OP on 3/9/2017.
 */

class Contact {
    String email;
    String name;
    String phoneNumber;

    int[] idAvatars;
    int idxAvatar;

    int [] nameColors;
    int idxColor;

    public Contact(int[] idAvatars, String email, String name, String phoneNumber)
    {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.idAvatars = idAvatars;
        idxAvatar = 0;
    }

    public void setIdxAvatar(int newIdx)
    {
        this.idxAvatar = newIdx;
    }

    public void setIdxColor(int newIdx) { this.idxColor = newIdx;}
}
