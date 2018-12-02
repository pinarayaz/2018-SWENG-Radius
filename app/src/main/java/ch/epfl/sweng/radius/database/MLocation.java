package ch.epfl.sweng.radius.database;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MLocation implements DatabaseObject, Serializable {

    public final double DEFAULT_GROUP_LOCATION_RADIUS = 2000;

    private String userID;
    private String title;
    private String message;
    private double longitude;
    private double latitude;
    private boolean isVisible; // added for invisibility feature
    private String urlProfilePhoto;

    private int locationType; // 0: user location, 1: group location, 2: topic location
    private double radius; // Use it only if the mLocation is a group.
    private String spokenLanguages;
    private String interests;

    public MLocation(String userID){
        this.userID = userID;
        this.latitude = 46.5160698;
        this.longitude = 6.5681216000000004;
        this.title = "";
        this.message = "";
        this.locationType = 0;
        this.radius = 5000;
        this.isVisible = true;
        this.urlProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/radius-1538126456577.appspot.com/o/profilePictures%2Fdefault.png?alt=media&token=ccd39de0-9921-487b-90e7-3501262d7835";
        this.spokenLanguages = "";
        this.interests = "";
    }

    public MLocation(String userID, double longitude, double latitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = "New MLocation";
        this.message = "Here I am";
        this.locationType = 0;
        this.radius = 5000;
        this.isVisible = true;
        this.urlProfilePhoto = "https://firebasestorage.googleapis.com/v0/b/radius-1538126456577.appspot.com/o/profilePictures%2Fdefault.png?alt=media&token=ccd39de0-9921-487b-90e7-3501262d7835";
        this.spokenLanguages = "";
        this.interests = "";
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getID() {
        return userID;
    }

    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        if (interests.length() > 100)
            throw new IllegalArgumentException("Interests input is limited to 100 characters");
        this.interests = interests;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        if (spokenLanguages != null) this.spokenLanguages = spokenLanguages;
    }

    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMessage(String message) {

        if (message.length() > 50) // TODO : config file with all the constants
            throw new IllegalArgumentException("The status is limited to 50 characters");
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setID(String userID) {
        this.userID = userID;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double newRadius) {
        //if (getLocationType() == 1) {
            radius = newRadius;
        //}
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


}
