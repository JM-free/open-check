package org.opencheck.mvp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencheck.mvp.requests.CloudbedsRequests;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
class MvpOpenCheckController {

	//-------Necessary allways to Cloudbeds's clients-------------//
	static final String CLIENT_ID = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
	static final String CLIENT_SECRET = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
	static final String PROPERTY_ID = "212599";
	static final String REDIRECT_URI = "https://81ee-31-4-128-46.eu.ngrok.io";
	CloudbedsRequests rq = new CloudbedsRequests(PROPERTY_ID, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
	//-------------------------------------------------------------------------------------------------------//

	// Useful information for the client
	String reservationId = "3468462393326";
	//-------------------------------------------------------------------------------------------------------//

	@RequestMapping("/get_token")
	public String getAccessToken() {
		return rq.getAccessToken().toString();
	}

	@CrossOrigin("*")
	@GetMapping("/get_reservation")
	public String getReservation(@RequestParam(defaultValue = "invalid") String reservationId) {

		JSONObject reservationInfo = new JSONObject();
		if (reservationId.equals("invalid")) {
			reservationInfo.put("success", false);
			reservationInfo.put("message", "Invalid reservation id");

			return reservationInfo.toString();
		} else {
			this.rq.getAccessToken();
			reservationInfo = this.rq.getReservation(reservationId);

			return uiUsefulInfo(reservationInfo);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/get_guest")
	public String getGuest(@RequestParam(defaultValue = "invalid") String guestId, @RequestParam(defaultValue = "invalid") String reservationId) {

		JSONObject guestInfo = new JSONObject();
		this.rq.getAccessToken();

		if (reservationId.equals("invalid")) {
			return this.rq.getGuest(guestId, null).toString();
		} else if (guestId.equals("invalid")) {
			return this.rq.getGuest(null, reservationId).toString();
		} else {
			guestInfo.put("success", false);
			guestInfo.put("message", "Guest not found");
			return guestInfo.toString();
		}
	}

	/***
	 * This method is used to filter all reservations by guest name, date in and date out
	 * @param guestLastName
	 * @param dateIn
	 * @param dateOut
	 * @return String with the reservations info
	 */
	@CrossOrigin("*")
	@GetMapping("/get_reservations")
	public String getReservation(@RequestParam String guestLastName, @RequestParam String dateIn, @RequestParam String dateOut) {
		this.rq.getAccessToken();

		Map<String, String> criteria = new HashMap<>();
		criteria.put("resultsTo", dateIn);
		JSONObject reservations = this.rq.getReservations(criteria);

		String reservationId = filterReservationByGuestName(reservations, guestLastName, dateIn, dateOut);
		JSONObject reservationInfo = this.rq.getReservation(reservationId);

		return uiUsefulInfo(reservationInfo);
	}

//	/***
//	 * This method is used to add a reservation from the client side
//	 * @param startDate
//	 * @param endDate
//	 * @param guestFirstName
//	 * @param guestLastName
//	 * @param guestCountry
//	 * @param guestZipCode
//	 * @param guestEmail
//	 * @param roomRoomTypeId
//	 * @param roomQuantity
//	 * @param adultsroomTypeID
//	 * @param adultsQuantity
//	 * @param childrenroomTypeID
//	 * @param childrenQuantity
//	 * @param paymentMethod #Allowed values: "cash", "credit", "ebanking", "pay_pal"
//	 * @param paymentAmoun
//	 * @param paymentFieldName # This field is not clear
//	 * @param paymentFieldValue # This field is not clear
//	 * @return
//	 */
//	@CrossOrigin("*")
//	@PostMapping("/post_reservation")
//	public Reservation postReservation(@RequestBody Reservation newReservation){
//		return "invalid";
//	}


	//	@RequestMapping("/do_checkin&reservation_id={reservationId}")
	//	public String doCheckin(@PathVariable String reservationId) {
	//		this.rq.setRefreshToken(this.refreshToken);
	//		this.rq.getAccessToken();
	//
	//		JSONObject reservation = this.rq.getReservation(reservationId);
	//		String reservationString = uiUsefulInfo(reservationId, reservation);
	//		reservationString = reservationString.replace("\"paidStatus\":\"false\"", "\"paidStatus\":\"true\"");
	//
	//		if (reservationString.contains("\"paidStatus\":\"true\"")){
	//			Map<String, String> criteria = new HashMap<>();
	//			criteria.put("status", "checked_in");
	//
	//			JSONObject reservationInfo = this.rq.putReservation(reservationId, criteria);
	//
	//			return reservationInfo.toString();
	//		}
	//		else if(reservationString.contains("\"paidStatus\":\"false\"")){
	//			return "{\"success\":false,\"message\":\"Unpaid Reservation\"}";
	//		}
	//		else {
	//			return reservationString;
	//		}
	//	}

	private String uiUsefulInfo(@NotNull JSONObject reservationInfo) {

		JSONObject usefullInfo = new JSONObject();
		if (reservationInfo.isEmpty()) {
			usefullInfo.put("success", false);
			usefullInfo.put("message", "Reservation not found");

			return usefullInfo.toString();
		}
		else if (reservationInfo.get("success").equals(true)) {

			// A valid reservation was found
			JSONObject reservationData = reservationInfo.getJSONObject("data");
			usefullInfo.put("success", "true");

			// Get the date in and date out of the reservation
			usefullInfo.put("startDate", reservationData.get("startDate"));
			usefullInfo.put("endDate", reservationData.get("endDate"));

			// Get the guest information
			usefullInfo.put("guestName", reservationData.get("guestName"));
			usefullInfo.put("guestEmail", reservationData.get("guestEmail"));

			// Get the room information
			JSONObject status;
			if (!reservationData.get("assigned").toString().equals("[]")) {
				status = reservationData.getJSONArray("assigned").getJSONObject(0);
			} else {
				status = reservationData.getJSONArray("unassigned").getJSONObject(0);
			}

			usefullInfo.put("roomType", status.getString("roomTypeName"));
			usefullInfo.put("adults", status.getString("adults"));
			usefullInfo.put("children", status.getString("children"));

			//Get the payments information
			usefullInfo.put("paid", reservationData.getJSONObject("balanceDetailed").get("paid"));
			usefullInfo.put("balance", reservationData.get("balance"));

			if (reservationData.get("balance").toString().equals("0")) {
				usefullInfo.put("paidStatus", "true");
			} else {
				usefullInfo.put("paidStatus", "false");
			}

			return usefullInfo.toString();
		} else {
			return reservationInfo.toString();
		}
	}

	private String filterReservationByGuestName(@NotNull JSONObject reservationsInfo, String LastName, String dataIn, String dataOut) {

		JSONArray reservations = reservationsInfo.getJSONArray("data");

		String reservationId = "0";
		int reservationsLength = reservations.length();
		for (int i = 0; i < reservationsLength; i++) {

			JSONObject reservation = reservations.getJSONObject(i);
			if (reservation.getString("guestName").contains(LastName) &&
					reservation.getString("startDate").equals(dataIn) &&
					reservation.getString("endDate").equals(dataOut)) {

				reservationId = reservation.getString("reservationID");
				return reservationId;
			}
		}
		return reservationId;
	}
}

@SpringBootApplication
public class MvpOpenCheckApplication {
	public  static void main(String[] args){

		SpringApplication.run(MvpOpenCheckApplication.class, args);

	}
}
