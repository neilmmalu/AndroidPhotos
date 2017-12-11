package com.example.nc.androidphotos.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neil on 12/11/17.
 */

public class User implements Serializable{

    public static final String sessionDataFile = "sessionData.dat";

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


}
