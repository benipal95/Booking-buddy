package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jjtowers on 11/30/2017.
 */

public class AirlineCodeParser
{
    Context mContext;
    //ArrayList<HashMap<String, String>> dataJSON;
    public AirlineCodeParser(Context theContext) {
        mContext= theContext;
    }

    public HashMap<String, String> parseAirlineJSON() throws IOException {
        HashMap<String, String> dataJSON = new HashMap();

        String json = null;
        try {
            InputStream is = mContext.getAssets().open("airlines.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        try {
            JSONArray top_data = new JSONArray(json);
            //Log.d("AIRLINE_CODE_PARSE", "Length: " + data.length());
            JSONArray data = top_data.getJSONArray(0);
            for(int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
                String key = d.toString();
                key = key.substring(2, key.indexOf('\"', 3));
                Log.d("AIRLINE_CODE_PARSE", key);
                // add values to Strings for hashmap
                String test = d.toString();
                if(test.contains("\"IATA\"")) {
                    String iata = d.getString("IATA");
                    String name = d.getString("name");
                    Log.d("AIRLINE_CODE_PARSE", iata + ", " + name);


                    // add strings to hashmap
                    //HashMap<String, String> hashData = new HashMap<>();
                    //hashData.put("iata", iata);
                    dataJSON.put(iata, name);
                }

                //add hashmap back to the HashMap ArrayList
                //dataJSON.add(hashData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return dataJSON;

    }

    public ArrayList<String> parseAirlineJSON_AsList() throws IOException {
        ArrayList<String> dataJSON = new ArrayList<>();

        String json = null;
        try {
            InputStream is = mContext.getAssets().open("airlines.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        try {
            JSONArray top_data = new JSONArray(json);
            //Log.d("AIRLINE_CODE_PARSE", "Length: " + data.length());
            JSONArray data = top_data.getJSONArray(0);
            for(int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
                String key = d.toString();
                key = key.substring(2, key.indexOf('\"', 3));
                Log.d("AIRLINE_CODE_PARSE", key);
                // add values to Strings for hashmap
                String test = d.toString();
                //if(test.contains("\"IATA\"")) {
                //    String iata = d.getString("IATA");
                    String name = d.getString("name");
                //    Log.d("AIRLINE_CODE_PARSE", iata + ", " + name);


                    // add strings to hashmap
                    //HashMap<String, String> hashData = new HashMap<>();
                    //hashData.put("iata", iata);
                    dataJSON.add(i, name);
                //}

                //add hashmap back to the HashMap ArrayList
                //dataJSON.add(hashData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataJSON;

    }
}
