package com.example.nc.androidphotos.Model;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Neil on 12/11/17.
 */

public class Album implements Image, Serializable{

    private List<Photo> album;
    private String albumName;

    private User user;


    private boolean isSearchResult;

    /**
     * Constructor for the album class
     * @param album_name
     */
    public Album(String album_name) {
        this.albumName = album_name;
        this.album = new ArrayList<Photo>();
    }

    /**
     *
     * @return the name of the album
     */
    public String getTitle() {
        return this.albumName;
    }

    /**
     *
     * @param newName the new name of the album
     */
    public void setAlbumName(String newName) {
        this.albumName = newName;
    }

    /**
     *
     * @param photo
     * @return the index of the photo required
     */
    public int getIndex(Photo photo) {
        int i = 0;
        for(Photo x: this.album) {
            if(x.equals(photo)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getImageUri() {
        return (album.size() > 0) ? album.get(0).getImageUri(): null;
    }

    /**
     *
     * @param index
     * @return photo at the index requested
     */
    public Photo getPhoto(int index) {
        return this.album.get(index);
    }

    /**
     *
     * @return the entire album list of photos
     */
    public List<Photo> getAlbum(){
        return this.album;
    }

    /**
     *
     * @param photo to be added to the album
     */
    public void addPhoto(Photo photo) {
        this.album.add(photo);
    }

    /**
     *
     * @param photo to be deleted from the album
     */
    public boolean removePhoto(Photo photo) {
        if(this.album.contains(photo)) {
            this.album.remove(photo);
            return true;
        }
        return false;
    }
}
