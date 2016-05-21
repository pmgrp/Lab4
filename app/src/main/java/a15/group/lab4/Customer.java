package a15.group.lab4;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eugeniosorbellini on 01/04/16.
 */
public class Customer implements Parcelable{
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;

    //constructor
    public Customer(){
        this.name = null;
        this.surname = null;
        this.phone = null;
        this.email = null;
        this.password = null;
    }

    //getter
    public String getName(){ return this.name; }
    public String getSurname(){ return this.surname; }
    public String getPhone(){ return this.phone; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }

    //setter
    public void setName(String name){ this.name = name; }
    public void setSurname(String surname){ this.surname = surname; }
    public void setPhone(String phone){ this.phone = phone; }
    public void setEmail(String email){ this.email = email; }
    public void setPassword(String password){ this.password = password; }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.name);
        dest.writeString(this.surname);
    }

    //Creator
    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    //De-parcel object
    public Customer(Parcel in){
        this.name = in.readString();
        this.surname = in.readString();
    }

}





