package com.example.nc.androidphotos.Model;

import java.io.Serializable;

/**
 * Created by Neil on 12/11/17.
 */

public class Tag implements Serializable{
    private String tagType;
    private String tagValue;

    /**
     *
     * @param tagType the type of the tag
     * @param tagValue the value of the tag
     */
    public Tag(String tagType, String tagValue) {
        this.tagType = tagType;
        this.tagValue = tagValue;
    }

    /**
     * Sets the tagType
     * @param tagType returns the tagType
     */
    public void setType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * Sets the tagValue
     * @param tagValue returns the tagValue
     */
    public void setValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     *
     * @return the tagType
     */
    public String getType(){
        return tagType;
    }

    /**
     *
     * @return the tagValue
     */
    public String getValue(){
        return tagValue;
    }

    /**
     * @return returns tag in a nice format
     */
    public String toString(){
        return getType() + " : " + getValue();
    }

    /**
     * Overrides the equals method
     */
    public boolean equals(Object o){
        if(o == null || !(o instanceof Tag)){
            return false;
        }
        Tag temp =(Tag)o;

        return temp.getType().equals(tagType) &&  temp.getValue().equals(tagValue);
    }
}
