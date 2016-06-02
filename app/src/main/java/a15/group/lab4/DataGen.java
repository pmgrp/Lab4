package a15.group.lab4;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by eugeniosorbellini on 26/04/16.
 */
public class DataGen {

    /*

    static DailyOffer off1,off2,off3,off4,off5,off6;

    public static ArrayList<DailyOffer> makeOffers() {



        off1 = new DailyOffer();
        Uri uri = Uri.parse("android.resource://a15.group.lab4/drawable/pizza");
        String imagePath = uri.toString();
        off1.setID("1");
        off1.setRestaurantID("1");
        off1.setPhoto(imagePath);
        off1.setName("Pizza");
        off1.setPrice(10);
        off1.setRestaurantLatitude(41.90278); //Rome
        off1.setRestaurantLongitude(12.49637);
        off1.setRestaurantName("Da Gennaro");
        off1.setDescription("The good old world famous Italian Pizza");


        off2 = new DailyOffer();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/escargot");
        imagePath = uri.toString();
        off2.setID("2");
        off2.setRestaurantID("2");
        off2.setPhoto(imagePath);
        off2.setName("Escargot");
        off2.setPrice(30);
        off2.setRestaurantLatitude(48.85661); //Paris
        off2.setRestaurantLongitude(2.35222);
        off2.setRestaurantName("Chez Léo");
        off2.setDescription("A French favorite and a culinary adventure");


        off3 = new DailyOffer();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/pasta");
        imagePath = uri.toString();
        off3.setID("3");
        off3.setRestaurantID("1");
        off3.setPhoto(imagePath);
        off3.setName("Pasta");
        off3.setPrice(15);
        off3.setRestaurantLatitude(41.90278); //Rome
        off3.setRestaurantLongitude(12.49637);
        off3.setRestaurantName("Da Gennaro");
        off3.setDescription("World famous Italian pasta with the sauce of your choice from Pesto Genovese, Carbonara, Pesto Calabrese and Ragù");

        off4 = new DailyOffer();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/beef");
        imagePath = uri.toString();
        off4.setID("4");
        off4.setRestaurantID("2");
        off4.setPhoto(imagePath);
        off4.setName("Beef");
        off4.setPrice(20);
        off4.setRestaurantLatitude(48.85661); //Paris
        off4.setRestaurantLongitude(2.35222);
        off4.setRestaurantName("Chez Léo");
        off4.setDescription("Quality Beef with various cooking options");

        off5 = new DailyOffer();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/chicken");
        imagePath = uri.toString();
        off5.setID("5");
        off5.setRestaurantID("3");
        off5.setPhoto(imagePath);
        off5.setName("Chicken");
        off5.setPrice(8);
        off5.setRestaurantLatitude(35.689487); //Tokyo
        off5.setRestaurantLongitude(139.691706);
        off5.setRestaurantName("Keio Sushi");
        off5.setDescription("Japanese style fried chicken");

        off6 = new DailyOffer();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/sushi");
        imagePath = uri.toString();
        off6.setID("6");
        off6.setRestaurantID("3");
        off6.setPhoto(imagePath);
        off6.setName("Sushi");
        off6.setPrice(16);
        off6.setRestaurantLatitude(35.689487); //Tokyo
        off6.setRestaurantLongitude(139.691706);
        off6.setRestaurantName("Keio Sushi");
        off6.setDescription("World famous Japanese sushi");

        ArrayList<DailyOffer> offers = new ArrayList<>();
        offers.add(off1);
        offers.add(off2);
        offers.add(off3);
        offers.add(off4);
        offers.add(off5);
        offers.add(off6);
        return offers;
    }



    public static ArrayList<Reservation> makeReservations(){

        Customer cus1 = new Customer();
        cus1.setName("Marco");
        cus1.setSurname("Bianchi");
        cus1.setPhone("3337659876");

        Reservation res1 = new Reservation();
        res1.setCustomer(cus1);
        res1.setDailyOffer(off1);
        res1.setStatus(Reservation.ARRIVED);
        res1.setTime("12:00");
        res1.setComment("I will need a big table");

        Reservation res2 = new Reservation();
        res2.setCustomer(cus1);
        res2.setDailyOffer(off2);
        res2.setStatus(Reservation.ARRIVED);
        res2.setTime("13:30");
        res2.setComment("I might be late");

        Reservation res3 = new Reservation();
        res3.setCustomer(cus1);
        res3.setDailyOffer(off3);
        res3.setStatus(Reservation.ARRIVED);
        res3.setTime("14:00");
        res3.setComment("");

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(res1);
        reservations.add(res2);
        reservations.add(res3);
        return reservations;

    }

    public static ArrayList<Restaurant> makeRestaurants() {

        Restaurant res1 = new Restaurant();
        Uri uri = Uri.parse("android.resource://a15.group.lab4/drawable/restaurant_rome");
        String imagePath = uri.toString();
        res1.setID("1");
        res1.setRestaurantPhoto(imagePath);
        res1.setRestaurantName("Da Gennaro");
        res1.setRestaurantPhone("06887654");
        res1.setRestaurantAddress("Piazza Della Repubblica 1, Roma");
        res1.setLatitude(41.90278); //Rome
        res1.setLongitude(12.49637);
        res1.setRestaurantEmail("contact@dagennaro.it");
        res1.setLikeCount(2453);
        res1.setRestaurantPiva("1234567865");


        Restaurant res2 = new Restaurant();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/restaurant_paris");
        imagePath = uri.toString();
        res2.setID("2");
        res2.setRestaurantPhoto(imagePath);
        res2.setRestaurantName("Chez Léo");
        res2.setRestaurantPhone("0165784325");
        res2.setRestaurantAddress("Rue de Rivoli 25, Paris");
        res2.setLatitude(48.85661); //Paris
        res2.setLongitude(2.35222);
        res2.setRestaurantEmail("info@chezleo.fr");
        res2.setLikeCount(5567);


        Restaurant res3 = new Restaurant();
        uri = Uri.parse("android.resource://a15.group.lab4/drawable/restaurant_tokyo");
        imagePath = uri.toString();
        res3.setID("3");
        res3.setRestaurantPhoto(imagePath);
        res3.setRestaurantName("Keio Sushi");
        res3.setRestaurantPhone("0332013331");
        res3.setRestaurantAddress("Keio Plaza 4, Tokyo");
        res3.setLatitude(35.689487); //Tokyo
        res3.setLongitude(139.691706);
        res3.setRestaurantEmail("keio@keiosushi.jp");
        res3.setLikeCount(7538);


        ArrayList<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(res1);
        restaurants.add(res2);
        restaurants.add(res3);
        return restaurants;

    }
    */
}
