package a15.group.lab4;

/**
 * Created by eugeniosorbellini on 24/05/16.
 */
public class User {
    private String type;
    private String name;
    private String surname;
    private String phone;

    public User(){
        this.type = "";
        this.name = "";
        this.surname = "";
        this.phone = "";
    }


    public void setType(String type){
    this.type = type;
}
    public void setName(String name) {this.name = name; }
    public void setSurname(String surname) {this.surname = surname; }
    public void setPhone(String phone) {this.phone = phone; }

    public String getType(){
        return this.type;
    }
    public String getName() {return  this.name; }
    public String getSurname() {return  this.surname; }
    public String getPhone() {return  this.phone; }


}