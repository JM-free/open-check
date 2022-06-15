package org.opencheck.mvp;

import org.json.JSONException;
import org.opencheck.mvp.requests.CloudbedsRequests;

import java.util.HashMap;
import java.util.Map;

public class MvpOpenCheckApplication {

	public static void main(String[] args) throws JSONException {

		String clientId = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
		String clientSecret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
		String propertyId = "212599";
		String redirectUri = "https://81ee-31-4-128-46.eu.ngrok.io";
		String code = "-MrRQx3QejusB1w0TVGy8S2tGcLju3Ysem0RCV2oKWk";
		String state = "fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f62a8937cf0b19";

		String refreshToken = "fzfOngPJkMuwz45Jz6Lhtbd_ab5LSqv08Lm-xFJovcQ";

		CloudbedsRequests rq = new CloudbedsRequests(propertyId, clientId, clientSecret, redirectUri, code, state);
		rq.setRefreshToken(refreshToken);
		rq.getAccessToken();

		//Tested methods
		String ans= "";

		String reservationId = "8596384364162";
		String guestId = "53448411";

		Map<String, String> filterCriteria = new HashMap<>();
		filterCriteria.put("status", "confirmed");

		// getUsrInfo
		// getReservation
		// getReservations
		// getReservationAssignments
		// getReservationInvoiceInformation
		// getGuest

		System.out.println(ans);

		//UnTested methods
		String note = "Reservation from OpenCheck-API";
		ans = String.valueOf(rq.postReservationNote(reservationId, note));
		System.out.println(ans);

		String filePath = "/home/jsanhcez/Imágenes/Perfiles/img_rectangular.jpg";
		ans = String.valueOf(rq.postReservationDocument(reservationId, filePath)); //getReservationInvoiceInformation
		System.out.println(ans);


	}
}
