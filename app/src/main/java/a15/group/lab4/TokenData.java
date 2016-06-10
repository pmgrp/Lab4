package a15.group.lab4;

/**
 * Created by eugeniosorbellini on 09/06/16.
 */
public class TokenData {
    public String userId;
    public String token;

    public TokenData(){
        this.userId = null;
        this.token = null;
    }

    public TokenData(String userId, String token){
        this.userId = userId;
        this.token = token;
    }

}
