package org.opencheck.mvp_opencheck;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opencheck.mvp_opencheck.httpRequests.CloudbedsRequests;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MvpOpenCheckApplication {

	public static void main(String[] args) throws JSONException, IOException, InterruptedException {

		String clientId = "live1_212599_LBRQd0lhTFIkJH5GCpwPUOEg";
		String clientSecret = "gwSvTrFauEbt4OkKy1sVGCnB3hpcjf7I";
		String propertyId = "212599";
		String redirectUri = "https://81ee-31-4-128-46.eu.ngrok.io";

		String accessToken = "eyJraWQiOiJsVXJsdTNvUjVVRTlIb2YyNXRMNHNfTzRXNEF6Wll4akMzSmJjUXZ1VEdZIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkRTdmV2R2pxWnUtd2xTdEJZTlBoWlFoUWRmZ3R6a0FVY0F0R1FyRUctdmcub2FyaTVkdDU2akZhVWhZWWE1ZDYiLCJpc3MiOiJodHRwczovL2lkcC5jbG91ZGJlZHMuY29tL29hdXRoMi9hdXNkNWcydTY5QmxKNFdBYzVkNiIsImF1ZCI6Imh0dHBzOi8vaG90ZWxzLmNsb3VkYmVkcy5jb20vYXBpIiwiaWF0IjoxNjU1MTQ2OTM5LCJleHAiOjE2NTUxNTA1MzksImNpZCI6ImxpdmUxXzIxMjU5OV9MQlJRZDBsaFRGSWtKSDVHQ3B3UFVPRWciLCJ1aWQiOiIwMHU1MHY0ZTNmT1Y1VlJuVzVkNyIsInNjcCI6WyJvZmZsaW5lX2FjY2VzcyIsInJlYWQ6aG90ZWwiXSwiYXV0aF90aW1lIjoxNjU0NzgwMTA0LCJzdWIiOiJyYW1pcmV6c2FuY2hlempvc2VtQGdtYWlsLmNvbSIsInR5cGUiOiJwcm9wZXJ0eSJ9.kbROfT87I1e4o3OXyAw1SAWfw1o483riC71uEnEy2lhS_VCCbiBhKt6ZhNN3BLWt3BmnJYpVyJrJIlimyLTYw9V04PkUgdOnqet64hFS09hppGRPtqM0yVEuhGhmyIhDP-0FyXPhCygLIm0eNK20T-Cu_Gp5yorgD4Y0ZqxegWrttBrDEJHWzXg4O8rW286NRsx_wjwi9Vo3cGOKVgHZm68n6FdFCMU-Ae-xWQyx_iIVxNvjPqOJOf1-8IbGcrfYOpko_CQSlhxDHW-hU3JOsLF6R4dFEflotvQAgWuH4kBoXNNIByZrsDe0W7smWSgCUR6JxAsMm2aI6uqj5pxW_w";
		String refreshToken = "x9kq-gDVrmnscyDjV_dp7mLNOtjAHK9rz9nnb3KY4m8";

		CloudbedsRequests rq = new CloudbedsRequests(propertyId, clientId, clientSecret, redirectUri, "", "");
		rq.setAccessToken(accessToken);
		rq.setRefreshToken(refreshToken);
		String ans = rq.getAccessToken();
		System.out.println(ans);
		ans = String.valueOf(rq.getUsrInfo());
		System.out.println(ans);
	}
}
