package com.example.nc.androidphotos.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import android.content.Context;

/**
 * Created by Neil on 12/11/17.
 */

public class User implements Serializable{

    public static final String sessionDataFile = "PhotoApp.dat";

    private String userName;
    private List<Album> albums;
    private List<Photo> photos;

    /**
     * Constructor for the user class
     * @param name
     */
    public User(String name) {
        this.userName = name;
        albums = new ArrayList<Album>();
    }

    /**
     * Creates a new album and adds it to the album list
     *
     * @param albumName
     */
    public void createAlbum(String albumName) {
        this.albums.add(new Album(albumName));
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    /**
     * Deletes the album requested from the album list
     *
     * @param albumName
     * @return true if deleted, false if it doesn't exist
     */
    public boolean deleteAlbum(Album albumName) {
        return this.albums.remove(albumName);
    }

    /**
     *
     * @return the list of albums the user has
     */
    public List<Album> getAlbums() {
        return this.albums;
    }

    /**
     *
     * @param index
     * @return the album at that index
     */
    public Album getAlbum(int index) {
        if(!this.albums.isEmpty()) {
            return this.albums.get(index);
        }
        else {
            return null;
        }
    }

    /**
     *
     * @param name
     * @return the album corresponding to the name, null if not found
     */
    public Album getAlbum(String name) {
        for(Album x : this.albums) {
            if(x.getTitle().equals(name)){
                return x;
            }
        }
        return null;
    }


    /**
     *
     * @return the username
     */
    public String getUserName() {
        return this.userName;
    }

    public boolean hasAlbumName(String name) {
        if (albums == null || albums.isEmpty())
            return false;

        for (Album a : albums)
        {
            if (a.getTitle().toLowerCase().equals(name.toLowerCase()))
                return true;
        }
        return false;
    }

    /**
     * reads and initializes the application from the previous session
     * @param fileName
     * @param context
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static User read(String fileName, Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        User obj = (User)ois.readObject();
        ois.close();
        return obj;
    }

    /**
     * Writes the current state of this application to be read in the next session
     * @param obj
     * @param fileName
     * @throws IOException
     */
    public static void write(Object obj, String fileName, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
    }
}
