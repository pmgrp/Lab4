package a15.group.lab4;


/**
 * Created by eugeniosorbellini on 01/04/16.
 */
public class Reservation {
    public static final int ARRIVED = 0;
    public static final int CONFIRMED = 1;
    public static final int REJECTED = 2;
    public static final int COMPLETED = 3;
    //private String ID;
    //private String restaurantID;
    private Customer customer;
    private DailyOffer dailyOffer;
    private Restaurant restaurant;
    private String date;
    private String time;
    private String comment; //@TODO implement comment feature
    private int status;

    public Reservation(){
        //this.ID = null;
        //this.restaurantID = null;
        this.customer = null;
        this.dailyOffer = null;
        this.time = null;
        this.comment = null;
        this.status = ARRIVED;
        this.date = null;
        this.restaurant = null;
    }



    public Reservation (String ID, String restaurantID, Customer customer, String date, DailyOffer dailyOffer,
                        String time) {
        /*this.ID = ID;
        this.restaurantID = restaurantID;*/
        this.customer = customer;
        this.dailyOffer = dailyOffer;
        this.date = date;
        this.time = time;
        this.comment = null;
        this.status = ARRIVED;
        this.restaurant = null;
    }

    //getter
    //public String getID(){ return ID; }
    //public String getRestaurantID(){ return restaurantID; }
    public Customer getCustomer(){ return this.customer; }
    public String getTime(){ return this.time; }
    public String getDate() { return this.date; }
    public String getComment(){ return this.comment; }
    public int getStatus(){ return this.status; }
    public DailyOffer getDailyOffer(){ return dailyOffer; }
    public Restaurant getRestaurant() { return restaurant; }

    //setter
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public void setComment(String comment){ this.comment = comment; }

    public void setStatus(int status){ this.status = status; }

    public void setDailyOffer(DailyOffer dailyOffer){ this.dailyOffer = dailyOffer; }

    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

}
