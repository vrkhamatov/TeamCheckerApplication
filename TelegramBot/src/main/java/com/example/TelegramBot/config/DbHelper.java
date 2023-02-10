package com.example.telegrambot.config;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DbHelper {

    public static String parseResult(String username, StringBuilder response){
        String correctEnterMessage = "Вы успешно зашли в комнату: " + username + "\nСостав комнаты:\n";

        assert response != null;
        response = new StringBuilder(response.toString().replace(",", "\n\r"));
        response = new StringBuilder(response.substring(1, response.length() - 1));
        response = new StringBuilder(response.toString().replace(" ", ""));

        return correctEnterMessage + response;

    }


    public static String enterPost(String username, String teamCode) throws IOException {
        URL obj = new URL("http://localhost:8488/team/enter");
        JSONObject jObj = new JSONObject();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");


        jObj.put("username", username);
        jObj.put("teamCode", teamCode);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        byte[] out = jObj.toString().getBytes();
        os.write(out);
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuilder response = null;

            while ((inputLine = in.readLine()) != null) {
                if (response != null)
                    response.append(inputLine);
                else
                    response = new StringBuilder(inputLine);
            }
            in.close();




            System.out.println(response);
            return parseResult(username,response);
        } else {
            System.out.println("POST request did not work.");
            return "Войти в комнату не удалось";
        }
    }

    public static String createPost(String username) throws IOException {
        URL obj = new URL("http://localhost:8488/team/create");
        JSONObject jObj = new JSONObject();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");


        jObj.put("username", username);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        byte[] out = jObj.toString().getBytes();
        os.write(out);
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuilder response = null;

            while ((inputLine = in.readLine()) != null) {
                if (response != null)
                    response.append(inputLine);
                else
                    response = new StringBuilder(inputLine);
            }
            in.close();

            System.out.println(response);

            String mes = "Вы успешно создали комнату с номером: " + response.toString() + "\n Сообщите его другим, чтобы они могли присоединиться в вашу комнату";

            return mes;
        } else {
            System.out.println("POST request did not work.");
            return "Войти в комнату не удалось";
        }
    }

    public static String exitFromTeam(String username) throws IOException {
        URL obj = new URL("http://localhost:8488/exit");
        JSONObject jObj = new JSONObject();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");


        jObj.put("username", username);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        byte[] out = jObj.toString().getBytes();
        os.write(out);
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuilder response = null;

            while ((inputLine = in.readLine()) != null) {
                if (response != null)
                    response.append(inputLine);
                else
                    response = new StringBuilder(inputLine);
            }
            in.close();

            System.out.println(response);

            String mes = "Вы успешно вышли из группы";

            return mes;
        } else {
            System.out.println("POST request did not work.");
            return "Вы не состоите в группе";
        }
    }

}


