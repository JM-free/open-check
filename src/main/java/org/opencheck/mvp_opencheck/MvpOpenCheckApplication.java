package org.opencheck.mvp_opencheck;

import org.json.JSONException;
import org.opencheck.mvp_opencheck.httpRequests.CloudbedsRequests;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
class MvpOpenCheckController {

	@RequestMapping("/")
	public String index() {
		return "Welcome to OpenCheck API!";
	}

	@RequestMapping("/get_new_token&code={code}")
	public void returnAccessToken(@PathVariable String code) {
		String webMsg = "code: " + code;
		CloudbedsRequests cbRequest = new CloudbedsRequests("212599", "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg",
															"gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I",
															"https://de3b-81-42-207-0.eu.ngrok.io",
															code, "");
	}
	@RequestMapping("/get_hotel_details")
	public void getHotelDetails(){
		CloudbedsRequests cbRequest = new CloudbedsRequests("212599", "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg",
				"gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I",
				"https://de3b-81-42-207-0.eu.ngrok.io",
				"4nFbVtgmNnMtALc8svOoXvntX0QIdnc-qu_HIajbVaQ&state=fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f629f71859794a",
				"");
	}

}


@SpringBootApplication
public class MvpOpenCheckApplication {

	public static void main(String[] args) throws JSONException, IOException {

		SpringApplication.run(MvpOpenCheckApplication.class, args);
		String client_id = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
		String property_id = "212599";
		String client_secret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
		String redirect_uri = "https://81ee-31-4-128-46.eu.ngrok.io";

		String code = "4yPEVGoSD_9RRf7Rs8ilVJTA_rtmmXKNLGH9bxOiRrQ";
		String state = "fb08f0d2ff44e959fcd83dc20e58a8c0d729cf6f62a2058dd16c5";

		String accessToken = "eyJraWQiOiJsVXJsdTNvUjVVRTlIb2YyNXRMNHNfTzRXNEF6Wll4akMzSmJjUXZ1VEdZIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmlnOGR2Sl9YYzNVRDU3SWNIUTVGU2hrWlpVVDBzejYwRTQ2cGpoZkVfWlUub2FyaTVkdDU2akZhVWhZWWE1ZDYiLCJpc3MiOiJodHRwczovL2lkcC5jbG91ZGJlZHMuY29tL29hdXRoMi9hdXNkNWcydTY5QmxKNFdBYzVkNiIsImF1ZCI6Imh0dHBzOi8vaG90ZWxzLmNsb3VkYmVkcy5jb20vYXBpIiwiaWF0IjoxNjU0Nzg2NjI1LCJleHAiOjE2NTQ3OTAyMjUsImNpZCI6ImxpdmUxXzIxMjU5OV9MQlJRZDBsaFRGSWtKSDVHQ3B3UFVPRWciLCJ1aWQiOiIwMHU1MHY0ZTNmT1Y1VlJuVzVkNyIsInNjcCI6WyJvZmZsaW5lX2FjY2VzcyIsInJlYWQ6aG90ZWwiXSwiYXV0aF90aW1lIjoxNjU0NzgwMTA0LCJzdWIiOiJyYW1pcmV6c2FuY2hlempvc2VtQGdtYWlsLmNvbSIsInR5cGUiOiJwcm9wZXJ0eSJ9.VprP7JqRDdj7kvUg_IMgaHXdac4ZsK20L5EXkXD5U432uUwK2cWFjNaq3yiMqhONh_vgZHY0rLmBYJdTdwXB6V7Ic8Hq4tR1SGSKbO-1HeAUIkkffH6QfOlkfbpd5vSPijhop20HReFGkh_JisappWOJfZn43GHfsOZzsxD3SAWjFMgO1fVlL7WSyGrHh_NwdPowfsmahoYmXhB0wZLo3noAHQwQLZBLPMyNaSkEOBOwAuDgqwa7ReMpF8e_0pJEuAsoeq7Oa7lSx9V10zsaFwjrfvUmibKYOuzJPEUGBW5s-iQMdmxzeKV44BzPZhweWUloabym5JwH8x2JtIT9VA";
		String refreshToken = "x9kq-gDVrmnscyDjV_dp7mLNOtjAHK9rz9nnb3KY4m8";

		CloudbedsRequests rq = new CloudbedsRequests(property_id,client_id,client_secret,redirect_uri,code,state);

		// Methods that work
		rq.getFirstAccessToken();
		rq.getUserInfo();

		// Methods that don't work
		rq.setRefreshToken(refreshToken); //You need to set a valid refreshToken
		//TODO Check these methods
		rq.getAccessToken();
		rq.checkAccessToken();
		rq.getUserInfo();

		}
	}
