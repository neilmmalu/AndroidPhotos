package com.example.nc.androidphotos;

import com.example.nc.androidphotos.Model.User;
import android.app.Application;

/**
 * Created by Neil on 12/11/17.
 */

public class AndroidPhoto extends Application{
    private User theUser;

    public User getUser() {
        return theUser;
    }

    public void setUser(User user) {
        this.theUser = user;
    }
}
