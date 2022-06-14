package org.opencheck.mvp_opencheck.httpRequests;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CloudbedsRequests extends Request{

    private final String  clientId;
    private final String propertyId;
    private final String clientSecret;
    private final String redirectUri;
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
    public String requestAccessTokenByScopes(@NotNull List<String> scopes){

        try {

            StringBuilder bld = new StringBuilder();
            for (String temp: scopes) {
                bld.append(temp + " ");
            }
            String urlScopes = bld.toString();

            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/oauth?scope=" + urlScopes);

            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", this.clientId);
            parameters.put("redirect_uri", this.redirectUri);
            parameters.put("response_type", "code");

            JSONObject pmsResponse = Request.httpRequest(url, "GET", null, null);
            this.code = (String) pmsResponse.get("code");
            this.state = (String) pmsResponse.get("state");
            
        } catch (MalformedURLException |  JSONException e) {
            throw new RuntimeException(e);
        }

        return this.code;
    }

    public String getAccessToken() {

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", this.clientId);
            parameters.put("client_secret", this.clientSecret);
            parameters.put("redirect_uri", this.redirectUri);

            if (this.refreshToken != null){
                parameters.put(REFRESH_TOKEN, this.refreshToken);
                parameters.put("grant_type", REFRESH_TOKEN);
            }else {
                parameters.put("code", this.code);
                parameters.put("grant_type", "authorization_code");
            }


            JSONObject pmsResponse = Request.httpRequest(url, "POST", parameters, null);
            this.accessToken = (String) pmsResponse.get("access_token");
            this.refreshToken = (String) pmsResponse.get(REFRESH_TOKEN);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        List<String> list = new ArrayList<>();
        list.add("Authorization");
        list.add("Bearer " + this.accessToken);
        this.header = list;

        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;

        List<String> list = new ArrayList<>();
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
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/userinfo");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("property_id", this.propertyId);

            pmsResponse = Request.httpRequest(url, "GET", parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pmsResponse;
    }

    public JSONObject getHotelDetails(){
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

    /**
     * Returns a list of reservations that matched the filters criteria.
     * Please note that some reservations modification may not be reflected in this timestamp.
     *
     * <p> Filters criteria:
     * <ol>
     *     <li>  status - A {@code String}. Filter by current reservation status. Allowed values: "not_confirmed", "confirmed", "canceled", "checked_in", "checked_out", "no_show"
     *     <li>  checkInFrom/checkInTo -  A {@code DataTime}.
     *     <li>  checkOutFrom/checkOutTo - A {@code DataTime}.
     * </ol>
     *
     *<p>Required Scopes:
     * <ul>
     * <li>{@code "scope=read:reservation"}.</li>
     * </ul>
     *
     * @param criteria The {@code Map<String, String>} with the filters criteria.
     * @return a JSON object with the reservation requested.
     *
     * @author José Manuel Ramírez
     * @author Edward L. Campbell
     */
    public JSONObject getReservations(@NotNull Map<String, String> criteria){
        JSONObject pmsResponse;
        criteria.put("propertyID", this.propertyId);

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getReservations");
            pmsResponse = Request.httpRequest(url, "GET", criteria, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse;
    }

    private static final String REFRESH_TOKEN = "refresh_token";
}