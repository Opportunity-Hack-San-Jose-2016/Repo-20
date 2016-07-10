package com.opportunityhack.teamhacks.helpbot_android;

/**
 * Created by hjagtap on 7/9/16.
 */
public class RowItem {

    private String refugeeID;
    private String zip;

    public RowItem(String refugeeID, String zip) {
        this.setZip(zip);
        this.setRefugeeID(refugeeID);
    }

    public String getRefugeeID() {
        return refugeeID;
    }

    public void setRefugeeID(String refugeeID) {
        this.refugeeID = refugeeID;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
