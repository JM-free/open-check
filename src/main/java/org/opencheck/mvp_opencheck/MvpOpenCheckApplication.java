package org.opencheck.mvp_opencheck;

import org.opencheck.mvp_opencheck.httpRequests.CloudbedsRequests;

import java.util.HashMap;
import java.util.Map;

public class MvpOpenCheckApplication {

	public static void main(String[] args) {

		String clientId = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
		String clientSecret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
		String propertyId = "212599";
		String redirectUri = "https://81ee-31-4-128-46.eu.ngrok.io";
		String code = "-MrRQx3QejusB1w0TVGy8S2tGcLju3Ysem0RCV2oKWk";
		String state = "fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f62a8937cf0b19";

		String refreshToken = "fzfOngPJkMuwz45Jz6Lhtbd_ab5LSqv08Lm-xFJovcQ";

		CloudbedsRequests rq = new CloudbedsRequests(propertyId, clientId, clientSecret, redirectUri, code, state);
		rq.setRefreshToken(refreshToken);
		String ans = rq.getAccessToken();
		System.out.println(ans);
		ans = String.valueOf(rq.getUsrInfo());
		System.out.println(ans);

		Map<String, String> parameters = new HashMap<>();
		parameters.put("status", "confirmed");
		ans = String.valueOf(rq.getReservations(parameters));
		System.out.println(ans);
	}
}
