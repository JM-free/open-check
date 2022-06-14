package org.opencheck.mvp_opencheck.httpRequests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudbedsRequests extends Request{

    private String clientId;
    private String propertyId;
    private String clientSecret;
    private String redirectUri;
    private String code;

    private String state;

    private String accessToken;
    private String refreshToken;

    List<String> header;

    //Constructor
    public CloudbedsRequests(String propertyId, String clientId, String clientSecret,
                             String redirectUri, String code, String state) {
        this.clientId = clientId;
        this.propertyId = propertyId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.state = state;
        this.code = code;
    }

    //Getter-Setter section
    public String getAccessToken() {

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", this.clientId);
            parameters.put("client_secret", this.clientSecret);
            parameters.put("redirect_uri", this.redirectUri);

            if (this.refreshToken != null){
                parameters.put("refresh_token", this.refreshToken);
                parameters.put("grant_type", "refresh_token");
            }else {
                parameters.put("code", this.code);
                parameters.put("grant_type", "authorization_code");
            }


            JSONObject pmsResponse = Request.httpRequest(url, "POST", parameters, null);
            this.accessToken = (String) pmsResponse.get("access_token");
            this.refreshToken = (String) pmsResponse.get("refresh_token");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        List<String> list = new ArrayList<String>();
        list.add("Authorization");
        list.add("Bearer " + this.accessToken);
        this.header = list;

        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        List<String> list = new ArrayList<String>();
        list.add("Authorization");
        list.add("Bearer " + this.accessToken);
        this.header = list;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // CloudBed's Methods
    public JSONObject getUsrInfo(){
        JSONObject pmsResponse;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getHotelDetails");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("propertyID", this.propertyId);

            pmsResponse = Request.httpRequest(url, "GET", parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pmsResponse;
    }
}