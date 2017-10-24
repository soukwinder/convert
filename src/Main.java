import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Main {
    public static JSONArray list = new JSONArray();
    public static JSONObject oblat = new JSONObject();

    private static final String USER_AGENT = "Mozilla/5.0";



    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject objjson = new JSONObject();

        try {

            Object obj = parser.parse(new FileReader("/Users/singh/Downloads/converter/inkomen.json"));

            JSONArray jsonObject = (JSONArray) obj;
            System.out.println(jsonObject);

//            String name = (String) jsonObject.get("name");
//            System.out.println(name);
//
//            long age = (Long) jsonObject.get("age");
//            System.out.println(age);
//
//            // loop array

            for (int i = 0; i < jsonObject.size(); i++) {
                String jstring = jsonObject.get(i).toString();
                JSONObject json = (JSONObject) parser.parse(jstring);
                NumberFormat nf = NumberFormat.getInstance();
                String wijk = json.get("wijk").toString();
                String colour = "";
                int radius = 500;
               // System.out.println(json.get("inkomen").toString());
                double int_inkomen = nf.parse(json.get("inkomen").toString()).doubleValue() / 10;

                String wijk_split = wijk.replaceAll("\\s+","%20");
                System.out.println(wijk_split);

                if(int_inkomen <= 15){
                    colour = "red";
                } else if(int_inkomen > 15 && int_inkomen < 20){
                    colour = "orange";
                } else if(int_inkomen >= 20 && int_inkomen < 25){
                    colour = "yellow";
                }else if(int_inkomen >= 25){
                    colour = "green";
                }


                sendGet(wijk_split, colour, radius);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //sendGet("Pendrecht");
    }

    private static void sendGet(String adress, String colour, int radius) throws Exception {

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+adress+",+Netherlands&key=AIzaSyD0l8AD86NucXhybctdjkzL32XE7PDsYIA";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response.toString());
        String results = json.get("results").toString();
        JSONArray json_results = (JSONArray) parser.parse(results);
        JSONObject test = (JSONObject) parser.parse(json_results.get(0).toString());

        JSONArray array = new JSONArray();
        array.add(test.get("geometry"));
        JSONObject geo = (JSONObject) parser.parse(array.get(0).toString());
        array.add(geo.get("viewport"));
        JSONObject south = (JSONObject) parser.parse(array.get(1).toString());
        array.add(south.get("southwest"));
        JSONObject latlon = (JSONObject) parser.parse(array.get(2).toString());



    }
}
