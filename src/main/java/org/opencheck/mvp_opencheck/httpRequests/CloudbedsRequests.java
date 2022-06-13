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
import java.util.HashMap;
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

    Map<String, String> parameters = new HashMap<>();

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
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.parameters.put("Authorization", "Bearer " + this.accessToken);

    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //
    public @NotNull Map<String, String> jsonStringToHashMap(@NotNull String httpResponse) throws JSONException, JsonProcessingException {
        Map<String, String> jsonMap;

        String[] httpResponseArray = httpResponse.split("\n");
        String strResponse = httpResponseArray[httpResponseArray.length - 1];

        JSONObject outerObject = new JSONObject('{' + strResponse + '}');
        JSONObject innerObject = outerObject.getJSONObject("Response");

        ObjectMapper mapper = new ObjectMapper();
        jsonMap = mapper.readValue(innerObject.toString(), Map.class);


        return jsonMap;
    }

    public String getFirstAccessToken() {
        String pmsRespond;
        Map<String, String> mapPmsRespond;

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", this.clientId);
            parameters.put("client_secret", this.clientSecret);
            parameters.put("redirect_uri", this.redirectUri);
            parameters.put("code", this.code);
            parameters.put("grant_type", "authorization_code");

            pmsRespond = Request.httpRequest(url, "POST", parameters);
            mapPmsRespond = jsonStringToHashMap(pmsRespond);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        this.accessToken = mapPmsRespond.get("access_token");
        this.parameters.put("Authorization", "Bearer " + this.accessToken);

        this.refreshToken = mapPmsRespond.get("refresh_token");

        return this.accessToken;
    }

    public String getAccessToken(){
        String pmsRespond;
        Map<String, String> mapPmsRespond;

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", this.clientId);
            parameters.put("client_secret", this.clientSecret);
            parameters.put("redirect_uri", this.redirectUri);
            parameters.put("refresh_token", this.refreshToken);
            parameters.put("grant_type", "refresh_token");

            pmsRespond = Request.httpRequest(url, "POST", parameters);
            mapPmsRespond = jsonStringToHashMap(pmsRespond);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        this.accessToken = mapPmsRespond.get("access_token");
        this.parameters.put("Authorization", "Bearer " + this.accessToken);

        this.refreshToken = mapPmsRespond.get("refresh_token");

        return this.accessToken;
    }

    public Map<String, String> getUserInfo(){
        String pmsRespond;
        Map<String, String> mapPmsRespond;

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/userinfo\n");

            this.parameters.put("property_id", this.propertyId);

            pmsRespond = this.httpRequest(url, "GET", this.parameters);
            mapPmsRespond = jsonStringToHashMap(pmsRespond);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return mapPmsRespond;
    }

    public boolean checkAccessToken(){
        String pmsRespond;
        Map<String, String> mapPmsRespond;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token_check");

            pmsRespond = this.httpRequest(url, "POST", this.parameters);
            mapPmsRespond = jsonStringToHashMap(pmsRespond);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public Map<String, String> getHotelDetails(){
        String pmsRespond;
        Map<String, String> mapPmsResponse;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getHotelDetails");

            this.parameters.put("scope", "read:hotel");
            this.parameters.put("property_id", this.propertyId);

            pmsRespond = Request.httpRequest(url, "GET", parameters);
            mapPmsResponse = jsonStringToHashMap(pmsRespond);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return mapPmsResponse;
    }

    public Map<String, String> getReservations(String status, String checkInfo) {
        String pmsRespond;
        Map<String, String> jsonPmsRespond;

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getReservations");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.parameters.put("status", "in_house");

            pmsRespond = Request.httpRequest(url, "GET", this.parameters);
            jsonPmsRespond = jsonStringToHashMap(pmsRespond);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonPmsRespond;
    }

    public boolean putReservations(String reservationId, String status) {
        String pmsRespond;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/putReservation");

            this.parameters.put("reservationID", reservationId);
            this.parameters.put("status", status);

            pmsRespond = Request.httpRequest(url, "PUT", this.parameters);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsRespond.contains("success");
    }


}
