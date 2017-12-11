package com.example.nc.androidphotos.Model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neil on 12/11/17.
 */

public class Photo implements Serializable, Image{
    private List<Tag> tags;
    private String caption;
    private Album album;
    private String imageUriString;

    public Photo(String URIstr, Album album) {
        this.imageUriString = URIstr;
        this.tags = new ArrayList<Tag>();
        this.caption = "";
        this.album = album;
    }

    public Photo(String URIstr, Album album, String caption) {
        this(URIstr, album);
        this.caption = caption;
    }

    public Photo duplicate() {
        Photo p = new Photo(this.imageUriString, this.album);
        p.tags = new ArrayList<Tag>(this.tags);
        p.caption = this.caption;
        p.album = null;
        return p;
    }

    public Album getAlbum() {
        return album;
    }

    /**
     * @return the list of tags for the photo
     */
    public List<Tag> getTags(){
        return this.tags;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Photo) || o == null)
            return false;

        Photo p = (Photo)o;

        return p.imageUriString.equals(this.imageUriString);
    }

    public boolean isDuplicate(Photo p) {
        return (this.imageUriString.equals(p.imageUriString) && this.album == p.album);
    }

    public boolean containsTagValue(String value) {
        if (tags.isEmpty())
            return false;

        for (Tag t : tags)
        {
            // uses contains because we want full partial string matches from searches
            if (t.getValue().toLowerCase().contains(value.toLowerCase()))
                return true;
        }

        return false;
    }

    /**
     *
     * @param Type of the tag
     * @param Value of the tag
     *
     */
    public void addTag(String tagType, String tagValue) {
        this.tags.add(new Tag(tagType, tagValue));
    }

    public void addAllTags(List<Tag> Tags) {
        tags.addAll(Tags);
    }

    /**
     *
     * @param index of the tag to be removed
     */
    public void deleteTag(int index) {
        this.tags.remove(index);
    }

    /**
     * Sets the caption for the photo
     * @param The caption for the photo
     */
    public void setCaption(String cap) {
        this.caption = cap;
    }

    /**
     * Returns the caption of the image
     * @return caption for the photo
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * @param The index of the tag required
     * @return The tag at the index requested
     */
    public Tag getTag(int index) {
        return this.tags.get(index);
    }

    /**
     * Returns the caption of the image
     * @return caption for the photo
     */
    public String getTitle() {
        return this.getCaption();
    }

    public String getImageUri() {
        return this.imageUriString;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
