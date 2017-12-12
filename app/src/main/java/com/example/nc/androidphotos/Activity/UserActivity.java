package com.example.nc.androidphotos.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
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

public class UserActivity extends Activity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private User user;
    private Album selectedAlbum;
    private String val;

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
                this.user = User.read(User.sessionDataFile, this);
            } catch (Exception e) {
                //TODO: handle this..
            }

            //This may be used later to verify the photos exist
            //User.verifyPhotos();
        }
        if(this.user == null) {
            this.user = new User("Main");
        }
        ((AndroidPhoto) this.getApplication()).setUser(this.user);

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
        return (ArrayList<Album>)this.user.getAlbums();
    }

    @Override
    public void onStart() {
        super.onStart();

        //Update the gridview!
        gridAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        //The current app is no longer active
        //Save the data just in case
        try {
            User.write(this.user, User.sessionDataFile, this);
        } catch (Exception e) {
            //handle this
        }
    }

    public void addAlbum(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter the name of the new album:");

        EditText input = new EditText(this);
        input.setId(R.id.inputDialogText);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int which) {
                EditText input = (EditText) ((Dialog)dialog).findViewById(R.id.inputDialogText);
                Editable value = input.getText();

                String str = value.toString();

                str = str.trim();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                //Do not allow all whitespace
                if (str.replaceAll("\\s", "").isEmpty()) {
                    Toast toast = Toast.makeText(context, "No blank album names", duration);
                    toast.show();
                    return;
                }
                //Do not allow duplicate album names
                if (user.hasAlbumName(str)){
                    Toast toast = Toast.makeText(context, "No duplicate album names", duration);
                    toast.show();
                    return;
                }


                //Add the album to the user!
                user.createAlbum(value.toString());

                //Update the gridview!
                gridAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridAdapter);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void viewAlbum(View view) {
        if(selectedAlbum == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Albums are selected", duration);
            toast.show();
            return;
        }
        //Create intent
        Intent intent = new Intent(UserActivity.this, AlbumActivity.class);
        intent.putExtra("albumName", selectedAlbum.getTitle());

        //Start details activity
        startActivity(intent);
    }

    public void deleteAlbum(View view) {
        if (selectedAlbum == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Albums are selected", duration);
            toast.show();
            return;
        }
        else
        {
            this.user.deleteAlbum(selectedAlbum);
            gridView.invalidateViews();
        }
    }

    public void recaptionAlbum(View view) {
        if(selectedAlbum == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Albums are selected", duration);
            toast.show();
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter the new name of the album:");

        EditText input = new EditText(this);
        input.setId(R.id.inputDialogText);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int which) {
                EditText input = (EditText) ((Dialog)dialog).findViewById(R.id.inputDialogText);
                Editable value = input.getText();

                String str = value.toString();

                str = str.trim();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                //Do not allow all whitespace
                if (str.replaceAll("\\s", "").isEmpty()) {
                    Toast toast = Toast.makeText(context, "No blank album names", duration);
                    toast.show();
                    return;
                }
                //Do not allow duplicate album names
                if (user.hasAlbumName(str)){
                    Toast toast = Toast.makeText(context, "No duplicate album names", duration);
                    toast.show();
                    return;
                }

                selectedAlbum.setAlbumName(str);

                gridAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridAdapter);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void search(View view) {
        val = null;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter the value you wish to search for: ");

        EditText input = new EditText(this);
        input.setId(R.id.inputDialogText);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        alertDialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int which) {
                Album results = new Album(SEARCH_RESULTS);
                results.flip();

                EditText input = (EditText) ((Dialog)dialog).findViewById(R.id.inputDialogText);
                Editable value = input.getText();

                String str = value.toString();

                str = str.trim();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                //Do not allow all whitespace
                if (str.replaceAll("\\s", "").isEmpty()) {
                    Toast toast = Toast.makeText(context, "Cannot search for just whitespace!", duration);
                    toast.show();
                    return;
                }

                val = str.toLowerCase();

                //shouldn't hit this
                if (val == null)
                    return;

                for (Album a : user.getAlbums())
                {
                    for (Photo p : a.getAlbum())
                    {
                        if (p.containsTagValue(val))
                        {
                            results.addPhoto(p.duplicate());
                        }
                    }
                }

                for (Photo p : results.getAlbum())
                {
                    p.setAlbum(results);
                }

                //Create intent
                Intent intent = new Intent(UserActivity.this, AlbumActivity.class);
                intent.putExtra("albumName", results.getTitle());

                user.addAlbum(results);

                //Start details activity
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
