package org.opencheck.mvp.requests;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

import java.util.Map;
import java.util.List;

import java.net.URL;
import java.net.HttpURLConnection;

class Request {

    Request() {
    }

    /**
     * Return a {{@code @JSONObject}} from the {@code BufferReader} of a URL.
     *
     * @param bufferedReader - The {@code BufferReader} of the URL.
     * @return - The {@code JSONObject} from the {@code BufferReader}.
     * @throws IOException - If an error occurs while reading the {@code BufferReader}.
     * @throws JSONException - If an error occurs while parsing the {{@code @JSONObject}}.
     */
    @NotNull
    private static JSONObject httpResponseBufferToJson(BufferedReader bufferedReader) throws IOException, JSONException {
        StringBuilder bld = new StringBuilder();
        String stringLine;
        while ((stringLine = bufferedReader.readLine()) != null) {
            bld.append(stringLine).append("\n");
        }
        String response = bld.toString();

        JSONTokener tokener = new JSONTokener(response);
        return new JSONObject(tokener);
    }

    /**
     * Return a String with all the {@code HttpURLConnection} headers and {@code HttpURLConnection} parameters.
     *
     * @param parameters - The {@code Map} of the parameters.
     * @return - The String with all the {@code HttpURLConnection} headers and {@code HttpURLConnection} parameters.
     */
    private static @NotNull String getRequestedData(@NotNull Map<String, String> parameters) {

        boolean fristElement = true;
        StringBuilder bld = new StringBuilder();

        for (Map.Entry<String,String> entry : parameters.entrySet()){
            String parameter =  entry.getKey();
            String value = entry.getValue();

            if (fristElement) {
                bld.append(parameter).append("=").append(value);
                fristElement = false;
            }else{
                bld.append("&").append(parameter).append("=").append(value);
            }

        }
        return bld.toString();
    }

    /**
     * Return the {@code JSONObject} from an HTTP Request.
     *
     * @param requestedUrl - The URL of the request.
     * @param requestMethod - The method of the request.
     * @param parameters - The {@code Map} of the parameters.
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

