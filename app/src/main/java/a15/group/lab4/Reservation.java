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
    private String customerId;
    private String offerId;
    private String restaurantId;
    private String reservationId;
    private String offerName;
    private String restaurantName;
    private String restaurantPhoto;
    private String date;
    private String time;
    private String comment; //@TODO implement comment feature
    private int status;

    public Reservation(){
        //this.ID = null;
        //this.restaurantID = null;
        this.customerId = null;
        this.offerId = null;
        this.time = null;
        this.comment = null;
        this.status = ARRIVED;
        this.date = null;
        this.restaurantId = null;
    }



    public Reservation (String customerId, String offerId, String restaurantId, String reservationId,
                        String offerName, String restaurantName, String restaurantPhoto, String date, String time, String comment, int status) {

        this.customerId = customerId;
        this.offerId = offerId;
        this.restaurantId = restaurantId;
        this.reservationId = reservationId;
        this.offerName = offerName;
        this.restaurantName = restaurantName;
        this.restaurantPhoto = restaurantPhoto;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.status = ARRIVED;
    }

    //getter
    public String getReservationId(){return this.reservationId;}
    public String getCustomerId(){ return this.customerId; }
    public String getTime(){ return this.time; }
    public String getDate() { return this.date; }
    public String getComment(){ return this.comment; }
    public int getStatus(){ return this.status; }
    public String getOfferId(){ return this.offerId; }
    public String getRestaurantId() { return this.restaurantId; }
    public String getOfferName() { return this.offerName;}
    public String getRestaurantName() {return this.restaurantName; }
    public String getRestaurantPhoto() { return this.restaurantPhoto; }

    //setter
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public void setReservationId(String reservationId) {this.reservationId = reservationId;}

    public void setTime(String time){
        this.time = time;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public void setComment(String comment){ this.comment = comment; }

    public void setStatus(int status){ this.status = status; }

    public void setOfferId(String offerId){ this.offerId = offerId; }

    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public void setOfferName(String offerName) { this.offerName = offerName; }

    public void setRestaurantName(String restaurantName ) { this.restaurantName = restaurantName;}

    public void setRestaurantPhoto(String restaurantPhoto) {this.restaurantPhoto = restaurantPhoto;}

}
