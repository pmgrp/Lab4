package a15.group.lab4;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eugeniosorbellini on 10/06/16.
 */
public class NotificationGenerator {

    public static JsonObjectRequest notificationRequest(String message, String to){

        JSONObject jsonBody = new JSONObject();

        try {

            jsonBody = new JSONObject("{\"to\":\"" + to +"\", \"notification\":{\"body\":\"" + message +"\"}}");

        }
        catch (Exception exception){
            Log.d("JSON", exception.getMessage());
        }

        String url = "https://fcm.googleapis.com/fcm/send";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                //hideProgressDialog();
            }
        }) {


            //* Passing some request headers

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=AIzaSyDbAZzBJfHzifu58J7Tn7Mg2V9ANxiHEAg");
                return headers;
            }


        };

        return jsonObjReq;

    }
}
