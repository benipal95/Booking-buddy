package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lorenzopacis on 11/20/2017.
 */

public class IATACodeParser {
    Context mContext;
    ArrayList<HashMap<String, String>> dataJSON;
    public IATACodeParser(Context theContext) {
        mContext= theContext;
    }

    public ArrayList<HashMap<String, String>> parseIATAJson() throws IOException {
        dataJSON = new ArrayList<>();

            String json = null;
            try {
                InputStream is = mContext.getAssets().open("airports.json");
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
            JSONArray data = new JSONArray(json);
            for(int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
                // add values to Strings for hashmap
                String iata =  d.getString("iata");
                String name = d.getString("name");

                // add strings to hashmap
                HashMap<String, String> hashData = new HashMap<>();
                hashData.put("iata", iata);
                hashData.put("name", name);

                //add hashmap back to the HashMap ArrayList
                dataJSON.add(hashData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataJSON;

    }


}
