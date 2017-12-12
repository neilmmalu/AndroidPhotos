package com.example.nc.androidphotos.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.Toast;

import com.example.nc.androidphotos.AndroidPhoto;
import com.example.nc.androidphotos.Model.*;
import com.example.nc.androidphotos.Adapter.*;
import com.example.nc.androidphotos.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Neil on 12/11/17.
 */

public class UserActivity extends AppCompatActivity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private User theUser;
    private Album selectedAlbum;

    private static final String SEARCH_RESULTS = "SEARCH_RESULTS_ALBUM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        selectedAlbum = null;

        //Load the previous session data
        File data = getFileStreamPath(User.sessionDataFile);

        if(data != null && data.exists()) {
            try {
                theUser = (User) Serialization.deserialize(User.sessionDataFile, this);
            } catch (Exception e) {
                //TODO: handle this..
            }

            //This may be used later to verify the photos exist
            //User.verifyPhotos();
        }
        if(theUser == null) {
            theUser = new User("Main");
        }
        ((AndroidPhoto) this.getApplication()).setUser(theUser);

        //Update the gridview
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_album_layout, getData());
        gridView.setAdapter(gridAdapter);

        //Set up on click listener for gridview items
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                selectedAlbum = (Album) parent.getItemAtPosition(position);
                v.setSelected(!v.isSelected());

                /*//Create intent
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);*/

            }
        });
    }

    private ArrayList<Album> getData() {
        return (ArrayList<Album>)theUser.getAlbums();
    }



}
