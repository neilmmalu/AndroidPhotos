package com.example.nc.androidphotos.Activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.app.Activity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nc.androidphotos.Model.*;
import com.example.nc.androidphotos.Adapter.*;
import com.example.nc.androidphotos.R;
import com.example.nc.androidphotos.AndroidPhoto;

import java.util.ArrayList;

/**
 * Created by Neil on 12/12/17.
 */

public class AlbumActivity extends Activity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private User user;
    private Photo photo;
    private Album album;

    private static final int PICK_IMAGE = 100;

    private static final int DISCARD_SEARCH_RESULTS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_album);

        //init
        this.photo = null;

        //get a copy of User
        this.user = ((AndroidPhoto) this.getApplication()).getUser();

        //get a copy of the Selected Album
        String albumName = getIntent().getStringExtra("albumName");

        for(Album a: this.user.getAlbums()) {
            if(a.getTitle().equals(albumName)) {
                this.album = a;
                break;
            }
        }
        if(this.album == null) {
            //Shouldn't happpen........
            System.out.println("Could not find album...");
            //complain
        }

        //Update the gridview
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_album_layout, getData());
        gridView.setAdapter(gridAdapter);

        //Set up on click listener for gridview items
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                photo = (Photo) parent.getItemAtPosition(position);
                v.setSelected(!v.isSelected());

                /*//Create intent
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);*/

            }
        });

        if (this.album.isSearchResult) {
            Button addPhoto, movePhoto, deletePhoto, recaptionPhoto;

            addPhoto = (Button) findViewById(R.id.addPhotoButton);
            movePhoto = (Button) findViewById(R.id.movePhotoButton);
            deletePhoto = (Button) findViewById(R.id.deletePhotoButton);
            recaptionPhoto = (Button) findViewById(R.id.recaptionPhotoButton);

            addPhoto.setEnabled(false);
            addPhoto.setVisibility(View.GONE);
            movePhoto.setEnabled(false);
            movePhoto.setVisibility(View.GONE);
            deletePhoto.setEnabled(false);
            deletePhoto.setVisibility(View.GONE);
            recaptionPhoto.setEnabled(false);
            recaptionPhoto.setVisibility(View.GONE);
        }
    }

    private ArrayList<Photo> getData() {
        return (ArrayList<Photo>) this.album.getAlbum();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        //The current app is no longer active
        //Save the data just in case
        try {
            this.user.write(this.user, User.sessionDataFile, this);
        } catch (Exception e) {
            //handle this
        }
    }

    @Override
    public void onBackPressed() {
        if (this.album.isSearchResult)
        {
            this.user.deleteAlbum(this.album);
        }
        super.onBackPressed();
    }

    public void deletePhoto(View view) {
        if(this.photo == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Photos are selected", duration);
            toast.show();
            return;
        }
        else
        {
            this.album.removePhoto(this.photo);
            gridView.invalidateViews();
        }
    }

    public void recaptionPhoto(View view) {
        if(this.photo == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Photos are selected", duration);
            toast.show();
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter the caption of this photo:");

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

                photo.setCaption(str);

                //Update the gridview!
                gridAdapter.notifyDataSetChanged();
                gridView.setAdapter(gridAdapter);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void addPhoto(View view) {

        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();

            int takeFlags = data.getFlags();
            takeFlags &= Intent.FLAG_GRANT_READ_URI_PERMISSION;
            ContentResolver resolver = getContentResolver();
            resolver.takePersistableUriPermission(imageUri, takeFlags);

            if(!this.album.addPhoto(this.photo)) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "This photo is already in the album.", duration);
                toast.show();
                return;
            }

            gridAdapter.notifyDataSetChanged();
            gridView.setAdapter(gridAdapter);
        }
    }

    public void viewPhoto(View view) {
        if(this.photo == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Photos are selected", duration);
            toast.show();
            return;
        }
        //Create intent
        Intent intent = new Intent(AlbumActivity.this, PhotoActivity.class);
        intent.putExtra("albumName", this.album.getTitle());
        intent.putExtra("photoUri", this.photo.getImageUri());

        //Start details activity
        startActivity(intent);
    }

    public void movePhoto(View view) {
        if(this.photo == null) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "No Photos are selected", duration);
            toast.show();
            return;
        }

        //time to load up this adapter...
        ListAdapter adapter = new ArrayAdapter<Album>(
                getApplicationContext(), android.R.layout.simple_list_item_1, this.user.getAlbums()) {

            ViewHolder holder;
            Drawable icon;

            class ViewHolder {
                TextView title;
            }

            public View getView(int position, View convertView,
                                ViewGroup parent) {
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = inflater.inflate(
                            android.R.layout.simple_list_item_1, null);

                    holder = new ViewHolder();
                    holder.title = (TextView) convertView
                            .findViewById(android.R.id.text1);
                    convertView.setTag(holder);
                } else {
                    // view already defined, retrieve view holder
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.title.setText(user.getAlbums().get(position).getTitle());

                return convertView;
            }
        };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        alertDialogBuilder.setTitle("Please choose an album to move this photo to:")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Album moveToAlbum = user.getAlbums().get(which);
                        if(moveToAlbum == album) {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "The photo is already in this album", duration);
                            toast.show();
                            return;
                        } else {
                            if(!album.addPhoto(photo)) {
                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, "The photo already exists in the selected album.", duration);
                                toast.show();
                            } else {
                                for(Photo p: moveToAlbum.getAlbum()) {
                                    if(p.getImageUri().equals(photo.getImageUri())) {
                                        p.setCaption(photo.getCaption());
                                        p.addAllTags(photo.getTags());
                                    }
                                }

                                album.removePhoto(photo);
                                //Update the gridview!
                                gridAdapter.notifyDataSetChanged();
                                gridView.setAdapter(gridAdapter);

                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, "Photo moved successfully", duration);
                                toast.show();
                            }
                        }
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
