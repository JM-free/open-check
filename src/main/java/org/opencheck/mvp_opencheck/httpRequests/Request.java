package org.opencheck.mvp_opencheck.httpRequests;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

public class Request {
    /**
     *
     * @param bufferedReader
     * @return
     * @throws IOException
     * @throws JSONException
     */
    @NotNull
    private static JSONObject httpResponseBufferToJson(BufferedReader bufferedReader) throws IOException, JSONException {
        StringBuilder bld = new StringBuilder();
        String stringLine;
        while ((stringLine = bufferedReader.readLine()) != null) {
            bld.append(stringLine + "\n");
        }
        String response = bld.toString();

        JSONTokener tokener = new JSONTokener(response);
        JSONObject json = new JSONObject(tokener);
        return json;
    }

    /**
     *
     * @param parameters
     * @return
     */
    @Nullable
    private static String getRequestedData(@NotNull Map<String, String> parameters) {
        String requestedData = "";
        for (Map.Entry<String,String> entry : parameters.entrySet()){
            String parameter =  entry.getKey();
            String value = entry.getValue();

            if (requestedData.isEmpty()) {
                requestedData += parameter + "=" + value;
            }else{
                requestedData += "&" + parameter + "=" + value;
            }

        }
        return requestedData;
    }

    /**
     *
     * @param requestedUrl
     * @param requestMethod
     * @param parameters
     */
    protected static JSONObject httpRequest(@NotNull URL requestedUrl, String requestMethod, Map<String, String> parameters, List<String> header) {

        JSONObject response = new JSONObject();

        try {
            HttpURLConnection httpUrlConnection = (HttpURLConnection) requestedUrl.openConnection();

            httpUrlConnection.setRequestMethod(requestMethod);
            httpUrlConnection.setDoOutput(true);

            if (requestMethod.contains("GET")) {

                httpUrlConnection.setRequestProperty(header.get(0), header.get(1));
                for (Map.Entry<String,String> entry : parameters.entrySet()){
                    String parameter =  entry.getKey();
                    String value = entry.getValue();
                    httpUrlConnection.setRequestProperty(parameter, value);
                }

            } else if (requestMethod.contains("POST")) {

                httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpUrlConnection.setRequestProperty("charset", "utf-8");

                String requestedData = getRequestedData(parameters);
                httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(requestedData.length()));
                DataOutputStream dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                dataOutputStream.writeBytes(requestedData);
            }

            InputStream inputStream = httpUrlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            response = httpResponseBufferToJson(bufferedReader);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

}

