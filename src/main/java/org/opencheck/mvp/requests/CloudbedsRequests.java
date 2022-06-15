package org.opencheck.mvp.requests;

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

    Map<String, String> parameters;

    //Constructor
    public CloudbedsRequests(String propertyId, String clientId, String clientSecret,
                             String redirectUri, String code, String state) {
        super();
        this.clientId = clientId;
        this.propertyId = propertyId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.state = state;
        this.code = code;

        this.parameters = new HashMap<>();
        this.parameters.put("propertyID", this.propertyId);
    }

    //Getter-Setter section
    public String requestAccessTokenByScopes(@NotNull List<String> scopes){

        try {

            StringBuilder bld = new StringBuilder();
            for (String temp: scopes) {
                bld.append(temp).append(" ");
            }
            String urlScopes = bld.toString();

            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/oauth?scope=" + urlScopes);

            Map<String, String> requestParameters = new HashMap<>();
            requestParameters.put("client_id", this.clientId);
            requestParameters.put("redirect_uri", this.redirectUri);
            requestParameters.put("response_type", "code");

            JSONObject pmsResponse = Request.httpRequest(url, "GET", null, null);
            this.code = (String) pmsResponse.get("code");
            this.state = (String) pmsResponse.get("state");
            
        } catch (MalformedURLException |  JSONException e) {
            throw new RuntimeException(e);
        }

        return this.code;
    }

    public String getAccessToken() {
        final String REFRESH_TOKEN = "refresh_token";

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/access_token");

            Map<String, String> requestParameters = new HashMap<>();
            requestParameters.put("client_id", this.clientId);
            requestParameters.put("client_secret", this.clientSecret);
            requestParameters.put("redirect_uri", this.redirectUri);

            if (this.refreshToken != null){
                requestParameters.put(REFRESH_TOKEN, this.refreshToken);
                requestParameters.put("grant_type", REFRESH_TOKEN);
            }else {
                requestParameters.put("code", this.code);
                requestParameters.put("grant_type", "authorization_code");
            }


            JSONObject pmsResponse = Request.httpRequest(url, "POST", requestParameters, null);
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

    // Cloudbeds's Generic Methods
    public JSONObject getUsrInfo(){
        JSONObject pmsResponse;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/userinfo");
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pmsResponse;
    }

    public JSONObject getHotelDetails(){
        JSONObject pmsResponse;
        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getHotelDetails");
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pmsResponse;
    }

    // Cloudbeds's Reservation Methods

    /**
     * Returns information on a booking specified by the reservationID parameter
     *
     * @param reservationId Reservation Unique Identifier.
     * @return A {@code JSONObject} with the status of the request and the details for the reservation queried.
     */
    public JSONObject getReservation(String reservationId){
        JSONObject pmsResponse;

        try {
            String urlRequest = new StringBuilder().append("https://hotels.cloudbeds.com/api/v1.1/getReservation?reservationID=").append(reservationId).toString();
            URL url = new URL(urlRequest);
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

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

    /**
     * Returns a list of rooms/reservations assigned for a selected date.
     *
     * @param date A {@code LocalDate} with the date selected to get the assignments. If no date is passed, it will return the results for the current day.
     * @return A {@code @JSONObject} with the list of rooms/reservations assigned with the {@code guestName}, {@code reservationID} and details about the room.
     */
    public JSONObject getReservationAssignments(String date){
        JSONObject pmsResponse;
        this.parameters.put("date", date);

        try {
            URL url = new URL("https://hotels.cloudbeds.com/api/v1.1/getReservationAssignments");
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse;
    }

    public JSONObject getReservationInvoiceInformation(String reservationId){
        JSONObject pmsResponse;
        try {
            String urlStr = new StringBuilder().append("https://hotels.cloudbeds.com/api/v1.1/getReservationInvoiceInformation?reservationID=").append(reservationId).toString();
            URL url = new URL(urlStr);
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse;

    }

    public JSONObject postReservationNote(String reservationId, String note){
        JSONObject pmsResponse;
        this.parameters.put("reservationID", reservationId);
        this.parameters.put("reservationNote", note);

        try {
            String urlStr = "https://hotels.cloudbeds.com/api/v1.1/postReservationNote";
            URL url = new URL(urlStr);
            pmsResponse = Request.httpRequest(url, "POST", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse;
    }

    /**
     * Attaches a document to a reservation.
     *
     * @param reservationId Reservation Unique Identifier
     * @param document Form-based File Upload. Allowed file types: *.pdf, *.rtf, *.doc, *.docx, *.txt, *.jpg, *.jpeg, *.png, *.gif, *.csv, *.txt, *.xls, *.xlsx. Allowed max file size: 10MB
     * @return success or not
     */
    public boolean postReservationDocument(String reservationId, String document) throws JSONException {
        JSONObject pmsResponse;
        this.parameters.put("reservationID", reservationId);
        this.parameters.put("file", document);

        try {
            String urlStr = "https://hotels.cloudbeds.com/api/v1.1/postReservationDocument";
            URL url = new URL(urlStr);
            pmsResponse = Request.httpRequest(url, "POST", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse.get("success").toString().contains("true");
    }

    // Cloudbeds's Guest Methods
    public JSONObject getGuest(String guestId, String reservationId) {
        JSONObject pmsResponse;
        String urlRequest = null;

        if (reservationId == null) {
            urlRequest = new StringBuilder().append("https://hotels.cloudbeds.com/api/v1.1/getGuest").append("?guestID=").append(guestId).toString();
            this.parameters.put("guestID", guestId);
        } else if (guestId == null) {
            urlRequest = new StringBuilder().append("https://hotels.cloudbeds.com/api/v1.1/getGuest").append("?reservationID=").append(reservationId).toString();
        }

        try {
            URL url = new URL(urlRequest);
            pmsResponse = Request.httpRequest(url, "GET", this.parameters, this.header);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pmsResponse;
    }
}