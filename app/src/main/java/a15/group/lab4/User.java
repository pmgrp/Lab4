package a15.group.lab4;

/**
 * Created by eugeniosorbellini on 24/05/16.
 */
public class User {
    private String type;
    private String name;
    private String surname;
    private String phone;
    private String birthday;
    private String email;
    private String password;
    private String photo;

    public User(){
        this.type = "";
        this.name = "";
        this.surname = "";
        this.phone = "";
        this.birthday = "";
        this.email = "";
        this.password = "";
        this.photo = "";
    }


    public void setType(String type){
    this.type = type;
}
    public void setName(String name) {this.name = name; }
    public void setSurname(String surname) {this.surname = surname; }
    public void setPhone(String phone) {this.phone = phone; }
    public void setBirthday(String birthday) {this.birthday = birthday; }
    public void setEmail(String email) {this.email = email; }
    public void setPassword(String password) {this.password = password; }
    public void setPhoto(String photo) {this.photo = photo; }

    public String getType(){
        return this.type;
    }
    public String getName() {return  this.name; }
    public String getSurname() {return  this.surname; }
    public String getPhone() {return  this.phone; }
    public String getBirthday() {return  this.birthday; }
    public String getEmail() {return  this.email; }
    public String getPassword() {return  this.password; }
    public String getPhoto() {return  this.photo; }


}