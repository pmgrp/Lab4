package a15.group.lab4;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by eugeniosorbellini on 01/04/16.
 */
public class Restaurant {

    private String ID;
    private double latitude;
    private double longitude;
    private String restaurantName;
    private String restaurantPhone;
    private String restaurantAddress;
    private String restaurantEmail;
    private String restaurantWebsite;
    private String restaurantPiva;
    private String restaurantPhoto;
    private int likeCount;
    private boolean liked;

    //public ArrayList<Day> openingDays;
    //public ArrayList<String> reservations; //those are ArrayLists of strings because they have only the IDs
    //public ArrayList<String> dailyOffers; //from which you can retrieve the object from DB
    //public ArrayList<String> comments;

    float distance;

    public Restaurant() {
        this.ID = "";
        this.latitude = 0;
        this.longitude = 0;
        this.restaurantName = "";
        this.restaurantPhone = "";
        this.restaurantAddress = "";
        this.restaurantEmail = "";
        this.restaurantWebsite = "";
        this.restaurantPiva = "";
        this.restaurantPhoto = "";
        this.distance = 0;
        //this.openingDays = null;
        //this.reservations = null;
        //this.dailyOffers = null;
        //this.comments = null;
        this.likeCount = 0;
        this.liked = false;
    }


    //getter


    public String getID() {
        return this.ID;
    }

    public int getLikeCount() { return this.likeCount; }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public String getRestaurantPhone() {
        return this.restaurantPhone;
    }

    public String getRestaurantAddress() {
        return this.restaurantAddress;
    }

    public String getRestaurantEmail() {
        return this.restaurantEmail;
    }

    public String getRestaurantWebsite() {
        return this.restaurantWebsite;
    }

    public String getRestaurantPiva() {
        return this.restaurantPiva;
    }

    public String getRestaurantPhoto() {
        return this.restaurantPhoto;
    }

    public boolean getLiked() {return this.liked;}


    //setter
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setLikeCount(int likes)
    {
        this.likeCount = likes;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public void setRestaurantWebsite(String restaurantWebsite) {
        this.restaurantWebsite = restaurantWebsite;
    }

    public void setRestaurantPiva(String restaurantPiva) {
        this.restaurantPiva = restaurantPiva;
    }

    public void setRestaurantPhoto(String restaurantPhoto) {
        this.restaurantPhoto = restaurantPhoto;
    }

    public void setLiked(boolean liked) {this.liked = liked;}

    /*

    public class Day {
        private final String[] namesOfDays = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        int day; //day of week 0 for Monday 6 for Sunday
        boolean breakfast;
        boolean lunch;
        boolean dinner;

        public Day() {
            this.day = 0;
            this.breakfast = false;
            this.lunch = false;
            this.dinner = false;
        }

        //getter
        public String getDayName() {
            return namesOfDays[day];
        }

        public int getDay() {
            return this.day;
        }

        public boolean getBreakfast() {
            return this.breakfast;
        }

        public boolean getLunch() {
            return this.lunch;
        }

        public boolean getDinner() {
            return this.dinner;
        }

        //setter
        public void setDay(int day) {
            this.day = day;
        }

        public void setBreakfast(boolean breakfast) {
            this.breakfast = breakfast;
        }

        public void setLunch(boolean lunch) {
            this.lunch = lunch;
        }

        public void setDinner(boolean dinner) {
            this.dinner = dinner;
        }


    }
*/

}

