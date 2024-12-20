package com.fatec.desenvolvimentomobile.rachai.Helpers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
    public static void enviarRequisicaoHttp(String urlString, JSONObject json, String method, HttpResponseCallback callback) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(method);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);

                if (json != null) {
                    try (OutputStream os = urlConnection.getOutputStream()) {
                        byte[] input = json.toString().getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = urlConnection.getResponseCode();
                InputStream is = responseCode < HttpURLConnection.HTTP_BAD_REQUEST
                        ? urlConnection.getInputStream()
                        : urlConnection.getErrorStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                callback.onSuccess(response.toString(), responseCode);

            } catch (Exception e) {
                callback.onError(e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }

    public interface HttpResponseCallback {
        void onSuccess(String response, int statusCode);
        void onError(Exception e);
    }

}

