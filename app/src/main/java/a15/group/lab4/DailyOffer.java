package a15.group.lab4;

/**
 * Created by eugenio sorbellini on 13/04/16.
 */
public class DailyOffer {
    private String ID;
    private String restaurantID;
    private String restaurantName;
    private double restaurantLatitude;
    private double restaurantLongitude;
    private String name;
    private String description;
    private String photo;
    private String photoThumb;
    private int price;
    private int availability;
    float distance = 0;

    //default constructor
    public DailyOffer() {
        this.ID = null;

        this.restaurantID = null;
        this.restaurantName = null;
        this.restaurantLatitude = 0;
        this.restaurantLongitude = 0;
        this.name = "";
        this.description = "";
        this.photo = "";
        this.photoThumb = "";
        this.price = -1;
        this.availability = -1;

    }

    //getter


    public String getID(){
        return this.ID;
    }

    public String getRestaurantID(){
        return this.restaurantID;
    }
    public String getRestaurantName(){
        return this.restaurantName;
    }
    public double getRestaurantLatitude(){
        return this.restaurantLatitude;
    }
    public double getRestaurantLongitude(){
        return this.restaurantLongitude;
    }


    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getPhoto(){
        return this.photo;
    }
    public String getPhotoThumb(){return  this.photoThumb;}
    public int getPrice(){
        return this.price;
    }
    public int getAvailability(){
        return this.availability;
    }

    //setter

    public void setID(String ID){this.ID = ID;}


    public void setRestaurantID(String restaurantID){
        this.restaurantID = restaurantID;
    }

    public void setRestaurantName(String restaurantName){
        this.restaurantName = restaurantName;
    }
    public void setRestaurantLatitude(double restaurantLatitude){
        this.restaurantLatitude = restaurantLatitude;
    }
    public void setRestaurantLongitude(double restaurantLongitude){
        this.restaurantLongitude = restaurantLongitude;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }
    public void setPhotoThumb(String photoThumb){this.photoThumb = photoThumb;}
    public void setPrice(int price){
        this.price = price;
    }
    public void setAvailability(int availability){
        this.availability = availability;
    }
}

