package org.opencheck.mvp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import org.opencheck.mvp.requests.CloudbedsRequests;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

@RestController
class MvpOpenCheckController {

	//-------Necessary allways to Cloudbeds's clients-------------//
	final static String clientId = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
	final static String clientSecret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
	final static String propertyId = "212599";
	final static String redirectUri = "https://81ee-31-4-128-46.eu.ngrok.io";
	String code = "yH1QV9s8YugwTFLGWf9FtzEnDn4MdJwyYuRMewVLL7o";
	String state = "fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f62b44fa165b49";
	String refreshToken = "ZwmXt5wpqiqWXD-9JNXZby5aJ8vwokk4cus_etW_Onk";
	CloudbedsRequests rq = new CloudbedsRequests(propertyId, clientId, clientSecret, redirectUri, code, state);
	//-------------------------------------------------------------------------------------------------------//

	// Useful information for the client
	String reservationId = "6171559861813";
	//-------------------------------------------------------------------------------------------------------//

	@RequestMapping("/config_machine?client_id={client_id}&client_secret={client_secret}&property_id={property_id}&redirect_uri={redirect_uri}&code={code}&state={state}")
	public String index(String client_id, String client_secret, String property_id, String redirect_uri, String code, String state) {
		this.rq = new CloudbedsRequests(property_id, client_id, client_secret, redirect_uri, code, state);
		return "Machine configuration: DONE!";
	}

	@RequestMapping("/get_token")
	public String getAccessToken() {
		return rq.getAccessToken().toString();
	}

	@RequestMapping("/get_guest_details&guest_id={guestId}")
	public String getHotelDetails(@PathVariable String guestId){

		this.rq.setRefreshToken(this.refreshToken);
		this.rq.getAccessToken();

		return this.rq.getGuest(guestId, null).toString();

	}

	@RequestMapping("/get_reservation&guest_last_name={guestLastName}&date_in={dateIn}&date_out={dateOut}")
	public String getReservation(@PathVariable String guestLastName, @PathVariable String dateIn, @PathVariable String dateOut) {
		this.rq.setRefreshToken(this.refreshToken);
		this.rq.getAccessToken();

		Map<String, String> criteria = new HashMap<>();
		criteria.put("resultsTo", dateIn);
		JSONObject reservations = this.rq.getReservations(criteria);

		String reservationId = filterReservationByGuestName(reservations, guestLastName, dateIn, dateOut);
		JSONObject reservationInfo = this.rq.getReservation(reservationId);

		return uiUsefulInfo(reservationId, reservationInfo);
	}

	@RequestMapping("/get_reservation&reservation_id={reservationId}")
	public String getReservation(@PathVariable String reservationId) {
		this.rq.setRefreshToken(this.refreshToken);
		this.rq.getAccessToken();
		JSONObject reservationInfo = this.rq.getReservation(reservationId);

		return uiUsefulInfo(reservationId, reservationInfo);
	}

	@RequestMapping("/do_checkin&reservation_id={reservationId}")
	public String doCheckin(@PathVariable String reservationId) {
		this.rq.setRefreshToken(this.refreshToken);
		this.rq.setAccessToken("eyJraWQiOiJMWFBQMGVrZ2t2SHJpMTNBNk1hc1J2RThXM1JBTGJJdWtZY3VmeEoyVThBIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmNLRkZ3Wkd0MHZkdllENkxqS0l3VFhlVVl6V0NCcFp3RGFlc3M3N1U3NTAub2FyaXlnbXJ6d09GcjJEME41ZDYiLCJpc3MiOiJodHRwczovL2lkcC5jbG91ZGJlZHMuY29tL29hdXRoMi9hdXNkNWcydTY5QmxKNFdBYzVkNiIsImF1ZCI6Imh0dHBzOi8vaG90ZWxzLmNsb3VkYmVkcy5jb20vYXBpIiwiaWF0IjoxNjU1OTg1NjkyLCJleHAiOjE2NTU5ODkyOTIsImNpZCI6ImxpdmUxXzIxMjU5OV9MQlJRZDBsaFRGSWtKSDVHQ3B3UFVPRWciLCJ1aWQiOiIwMHU1MHY0ZTNmT1Y1VlJuVzVkNyIsInNjcCI6WyJvZmZsaW5lX2FjY2VzcyIsInJlYWQ6Z3Vlc3QiLCJ3cml0ZTpyZXNlcnZhdGlvbiIsIndyaXRlOmd1ZXN0IiwicmVhZDpyZXNlcnZhdGlvbiJdLCJhdXRoX3RpbWUiOjE2NTU5ODIzNDUsInN1YiI6InJhbWlyZXpzYW5jaGV6am9zZW1AZ21haWwuY29tIiwidHlwZSI6InByb3BlcnR5In0.jaigMjxiafJR5oZ73BacFhiY5cJ2JE3GCN6U7NaJN_VGSPq6HcvZW0BNaaIXGoUusGD0ieW8u-Tg2SY0MdZBJl4XqN5ClGeouzJX1CzFYwQXGOZkuC7kdGkneHcqfWL34x7vPFyRE7DKLb-nraUtlz2D-FiYidoF6Is-RIIROYDnMGrX14pNpPx3wdicFGeSoxxB93tPhQjx45hBtvzRsQLQlnVo-JYvKvgZqRqKGCVRj4RL62bhZC6WuOweLqHaYxyBHhFWjlMPW5R2onvOGPFGdF1SzI45PmMVOHwr7MJCF9L-VCotVKEJ9kOs4-s7rbmth_Zzhi-hCzGrYRHOfg");
//		this.rq.getAccessToken();

		JSONObject reservation = this.rq.getReservation(reservationId);
		String reservationString = uiUsefulInfo(reservationId, reservation);
		reservationString = reservationString.replace("\"paidStatus\":\"false\"", "\"paidStatus\":\"true\"");

		if (reservationString.contains("\"paidStatus\":\"true\"")){
			Map<String, String> criteria = new HashMap<>();
			criteria.put("status", "checked_in");

			this.rq.setAccessToken("AT.e2MRpOS7qGryLyo_gMVp-PSjtJvSAkstQGQ6L6xyHnE.oariygmrzwOFr2D0N5d6");
			JSONObject reservationInfo = this.rq.putReservation(reservationId, criteria);

			return reservationInfo.toString();
		}
		else if(reservationString.contains("\"paidStatus\":\"false\"")){
			return "{\"success\":false,\"message\":\"Unpaid Reservation\"}";
		}
		else {
			return reservationString;
		}
	}

	private String uiUsefulInfo(String reservationId, @NotNull JSONObject reservationInfo) {

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

		int reservationsCount = reservationsInfo.getJSONArray("data").length();
		JSONArray reservations = reservationsInfo.getJSONArray("data");

		String reservationId = "0";
		for (int i = 0; i < reservationsCount; i++) {
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

	private String uiUsefulInfo(String reservationId, @NotNull JSONObject reservationInfo) {

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

		int reservationsCount = reservationsInfo.getJSONArray("data").length();
		JSONArray reservations = reservationsInfo.getJSONArray("data");

		String reservationId = "0";
		for (int i = 0; i < reservationsCount; i++) {
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

	public void printMessage(@NotNull String message) {

		int length = message.length();
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("=".repeat(length));
		messageBuilder.append("\n");
		messageBuilder.append("+ " + message + " +");
		messageBuilder.append("\n");
		messageBuilder.append("=".repeat(length));
		messageBuilder.append("\n");
		System.out.println(messageBuilder.toString());
	}


	public boolean checkReservation(String reservationID, CloudbedsRequests rq){

		JSONObject reservationInfo = rq.getReservation(reservationID);
		String response = uiUsefulInfo(reservationID, reservationInfo);
		if (response.contains("success")) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkReservationByGuestLastName(String guestLastName, String dateIn, String dateOut, CloudbedsRequests rq){

		Map<String, String> criteria = new HashMap<>();
		criteria.put("resultsTo", dateIn);
		JSONObject reservations = rq.getReservations(criteria);

		String reservationId = filterReservationByGuestName(reservations, guestLastName, dateIn, dateOut);
		JSONObject reservationInfo = rq.getReservation(reservationId);
		String response = uiUsefulInfo(reservationId, reservationInfo);

		if (response.contains("success")) {
			return true;
		} else {
			return false;
		}

	}
	public void main(String[] args){

		SpringApplication.run(MvpOpenCheckApplication.class, args);

		//-------Necessary allways to Cloudbeds's clients-------------//
		final String clientId = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
		final String clientSecret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
		final String propertyId = "212599";
		final String redirectUri = "https://81ee-31-4-128-46.eu.ngrok.io";
		String code = "yH1QV9s8YugwTFLGWf9FtzEnDn4MdJwyYuRMewVLL7o";
		String state = "fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f62b44fa165b49";
		String refreshToken = "ZwmXt5wpqiqWXD-9JNXZby5aJ8vwokk4cus_etW_Onk";
		CloudbedsRequests rq = new CloudbedsRequests(propertyId, clientId, clientSecret, redirectUri, code, state);
		//-------------------------------------------------------------------------------------------------------//

		boolean flag = true;
		while (flag) {
			Scanner sc= new Scanner(System.in);
			this.printMessage("What do you want to do?\n");
			System.out.print("1. Check in a reservation\n" +
							 "2. Check out a reservation\n" +
							 "3. Make a reservation\n" +
							 "4. Exit\n");

			int ans = sc.nextInt();
			switch (ans) {
				case 1:
					this.printMessage("Enter the reservation ID: ");
					String reservationId = sc.next();
					boolean validReservation = validReservation(rq, sc, reservationId);

					if (validReservation){
						this.printMessage("Valid Reservation!!\n");;
						boolean successCheckIn = doCheckIn(rq, reservationId);
						boolean successPayment = doPayment(rq, reservationId);
						if (successCheckIn && successPayment) {
							this.printMessage("Check in and payment success!!\n");
							String messageTicket = makeTickets(rq, reservationId);
							printMessage(messageTicket);
						} else {
							this.printMessage("Check in or payment failed!!\n");
						}

					} else {
						this.printMessage("Reservation not found\n" +
										  "Please select **MAKE A RESERVATION** in the next screen.\n");
					}
					break;

				case 2:
					this.printMessage("Enter the reservation ID: ");
					reservationId = sc.next();

					if (validReservation(rq, sc, reservationId)){
						boolean checkOutDone = doCheckOut(reservationId);
						if (checkOutDone) {
							this.printMessage("Check out success!!\n");
							String messageTicket = makeTickets(rq, reservationId);
							printMessage(messageTicket);

						} else {
							this.printMessage("Check out failed!!\n" +
											  "Try again in the next screen.\n");
						}
					} else{
						System.out.print("Reservation not found\n" +
										  "Try again in the next screen.\n");
					}

					flag = true;
					break;

				case 3:
					Map<String, String> bookingInfo = getBookingInfo(rq, sc);
					reservationId = rq.makeReservation(bookingInfo);
					this.printMessage("Reservation ID: " + reservationId + "\n");
					System.out.print("Please confirm your reservation:\n");
					System.out.println(bookingInfo);
					System.out.print("1. Confirm\n" +
									 "2. Cancel\n");

					int confirmation = sc.nextInt();
					if (confirmation == 1) {
						boolean successCheckIn = doCheckIn(rq, reservationId);
						boolean successPayment = doPayment(rq, reservationId);
						if (successCheckIn && successPayment) {
							this.printMessage("Check in and payment success!!\n");
							String messageTicket = makeTickets(rq, reservationId);
							printMessage(messageTicket);
						} else {
							this.printMessage("Check in or payment failed!!\n");
						}
					} else {
						this.printMessage("Reservation canceled!!\n");
					}

					flag = true;
					break;

				case 4:
					flag = false;
					System.exit(0);
					break;
				default:
					this.printMessage("Invalid option");

					flag = true;
					break;
			}

		}
	}


	// TODO implement the methods below
	private Map<String, String> getBookingInfo(CloudbedsRequests rq, Scanner sc) {
		Map<String, String> bookingInfo = new HashMap<>();

		System.out.print("Enter your name: ");
		String guestName = sc.next();
		bookingInfo.put("guestName", guestName);

		System.out.print("Enter your last name: ");
		String guestLast = sc.next();
		bookingInfo.put("guestLast", guestLast);

		System.out.print("Enter start date (YYYY-MM-DD): ");
		String startDate = sc.next();
		bookingInfo.put("startDate", startDate);
		System.out.print("Enter out date (YYYY-MM-DD): ");
		String outDate = sc.next();
		bookingInfo.put("outDate", outDate);


		System.out.print("Enter room type: ");
		String roomsAvailable = findRoomsAvailable(rq, startDate, outDate);
		System.out.print(roomsAvailable);
		String roomTypeID = sc.next();
		System.out.print("Enter number of rooms: ");
		int quantity = sc.nextInt();
		bookingInfo.put("roomTypeID", roomTypeID);
		bookingInfo.put("quantity", String.valueOf(quantity));


		System.out.print("Enter the number of adults guests: ");
		int adults = sc.nextInt();
		System.out.print("Enter the number of children guests: ");
		int children = sc.nextInt();
		bookingInfo.put("adults", String.valueOf(adults));
		bookingInfo.put("children", String.valueOf(children));

		return bookingInfo;
	}

	private String findRoomsAvailable(CloudbedsRequests rq, String startDate, String outDate) {
		return null;
	}

	private boolean doCheckOut(String reservationId) {
		return true;
	}

	private boolean doPayment(CloudbedsRequests rq, String reservationId) {
		return true;
	}

	private boolean doCheckIn(CloudbedsRequests rq, String reservationId) {
		return false;
	}

	private String makeTickets(CloudbedsRequests rq, String reservationId) {
		return null;
	}

	private boolean validReservation(CloudbedsRequests rq, Scanner sc, String reservationId) {
		boolean validReservation = this.checkReservation(reservationId, rq);
		if (validReservation) {
			this.printMessage("Reservation not found");
		} else {
			this.printMessage("Please. Enter your last name: ");
			String guestLastName = sc.next();
			this.printMessage("Enter the reservation date in: ");
			String dateIn = sc.next();
			this.printMessage("Enter the reservation date out: ");
			String dateOut = sc.next();

			validReservation = checkReservationByGuestLastName(guestLastName, dateIn, dateOut, rq);
		}

		return validReservation;
	}
}
