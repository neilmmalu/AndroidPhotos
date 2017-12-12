package com.example.nc.androidphotos.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nc.androidphotos.AndroidPhoto;
import com.example.nc.androidphotos.Adapter.*;
import com.example.nc.androidphotos.Model.*;
import com.example.nc.androidphotos.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

/**
 * Created by Neil on 12/12/17.
 */

public class PhotoActivity extends Activity{

    private ImageView image;
    private User user;
    private Photo photo;
    private Album album;
    private ListView listView;
    private List<Tag> tags;
    private ArrayAdapter adapter;

    int position;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_photo);

        this.photo = null;

        this.album = null;

        this.user = ((AndroidPhoto) this.getApplication()).getUser();

        String albumName = getIntent().getStringExtra("albumName");

        for(Album a : this.user.getAlbums())
        {
            if (a.getTitle().equals(albumName))
            {
                this.album = a;
                break;
            }
        }

        this.position = 0;
        String photoUri = getIntent().getStringExtra("photoUri");
        for(Photo p: this.album.getAlbum()) {
            if(p.getImageUri().equals(photoUri)) {
                this.photo = p;
                break;
            }
            this.position++;
        }

        this.image = (ImageView) findViewById(R.id.imageView);
        this.image.setImageURI(Uri.parse(this.photo.getImageUri()));

        listView = (ListView) findViewById(R.id.tagListView);
        listView.setChoiceMode(CHOICE_MODE_SINGLE);

        this.tags = new ArrayList<Tag>();
        this.tags.addAll(this.photo.getTags());

        adapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, this.tags);

        listView.setAdapter(adapter);

        updateListView();
        updateCaption();

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

    private void updateImageView() {
        this.image.setImageURI(Uri.parse(this.photo.getImageUri()));
    }

    private void updateCaption() {
        TextView captionBoy = (TextView) findViewById(R.id.tvCaption);
        captionBoy.setText(this.photo.getCaption());
    }

    private void updateListView() {
        this.tags.clear();
        this.tags.addAll(this.photo.getTags());
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    public void next(View view) {
        if(this.position == this.album.getAlbum().size() - 1) {
            this.position = 0;
        } else {
            this.position++;
        }
        this.photo = this.album.getAlbum().get(this.position);
        updateCaption();
        updateListView();
        updateImageView();
    }

    public void prev(View view) {
        if(this.position == 0) {
            this.position = this.album.getAlbum().size() - 1;
        } else {
            this.position--;
        }
        this.photo = this.album.getAlbum().get(this.position);
        updateCaption();
        updateListView();
        updateImageView();
    }

    public void addTag(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Input tag pair");

        // Set an EditText view to get user input
        final EditText value = new EditText(this);
        value.setHint("Value");
        final RadioGroup radgroup = new RadioGroup(this);
        final RadioButton radPerson = new RadioButton(this);
        radPerson.setText("Person");
        final RadioButton radLocation = new RadioButton(this);
        radLocation.setText("Location");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        radgroup.addView(radPerson);
        radgroup.addView(radLocation);
        layout.addView(radgroup);
        layout.addView(value);
        alert.setView(layout);

        radPerson.setChecked(true);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(value.getText().toString().length() == 0) {
                    //yell at user
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Both fields are required", duration);
                    toast.show();
                    return;
                }
                String tagType;
                if(radPerson.isChecked()) {
                    tagType = "Person";
                }
                else {
                    tagType = "Location";
                }
                photo.addTag(tagType, value.getText().toString());
                updateListView();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void removeTag(View view) {
        if(this.photo.getTags().size() == 0) {
            //yell at user
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "There are no tags to delete", duration);
            toast.show();
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        alertDialogBuilder.setTitle("Please select a tag to delete:")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Tag selectedTag = tags.get(which);
                        photo.deleteTag(selectedTag);
                        updateListView();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
