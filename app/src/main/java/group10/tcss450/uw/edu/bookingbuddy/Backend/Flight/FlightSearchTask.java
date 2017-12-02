package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

/**
 * Created by jjtowers on 11/17/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightListRecyclerView;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightSearchFragment;
import group10.tcss450.uw.edu.bookingbuddy.R;

import static android.content.ContentValues.TAG;

/**
 * This class handles the actions to be taken when a new FlightSearchTask is executed and will make
 * calls to the API to get the flights that the user is searching for.
 */
public class FlightSearchTask extends AsyncTask<String, Void, String>
{
    private static final String URL_FIRST = "http://api.travelpayouts.com/v2/prices/latest?currency=usd&period_type=year&page=1&limit=10&origin=";
    private static final String URL_CHEAP_START = "http://api.travelpayouts.com/v1/prices/cheap?origin=";
    private static final String URL_CHEAP_DEPART = "&depart_date=";
    private static final String URL_CHEAP_RETURN = "&return_date=";
    private static final String URL_CHEAP_END = "&currency=usd&token=9f0202d35e6767803ce5e453f702e6f6";
    private static final String URL_MID = "&destination=";
    private static final String URL_LAST = "&show_to_affiliates=true&sorting=price&trip_class=0&token=9f0202d35e6767803ce5e453f702e6f6";
    private ArrayList<Integer> sb = new ArrayList<>();
    private ArrayList<Flights> dataJSON;
    private RecyclerView mRecyclerView;
    private TextView mTx_results;
    private Context mContext;
    private String email;
    private int mSortOption;
    HashMap<String, String> mAirlineCodes;
    //Flights mFlights;

    //http://api.travelpayouts.com/v1/prices/cheap?origin=MOW&destination=HKT&depart_date=2017-11&return_date=2017-12&token=PutHereYourToken

    public FlightSearchTask(Context theContext, RecyclerView theRecycler, TextView theTextView, int sortOption, String email)
    {
        super();
        initializeTask(theContext, theRecycler, theTextView, sortOption, email);
    }

    @Override
    protected void onPreExecute()
    {
        AirlineCodeParser air_parse = new AirlineCodeParser(mContext);
        try {
            mAirlineCodes = air_parse.parseAirlineJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method will make a call to the API and create new JSON objects and create a hashmap of their values that will be later
     * used by a recycler view and a graph class to display this information.
     * @param strings
     * @return The result of the API call to the webservice.
     */
    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        HttpURLConnection connection = null;
        dataJSON = new ArrayList<>();
        String theOrigin = strings[0];
        String theDest = strings[1];
        String departureDate = null;
        String returnDate = null;
        if(strings.length > 2)
        {
            departureDate = strings[2];
            returnDate = strings[3];
        }

        try {
            URL urlObj;


            /*mAirlineCodes = new HashMap<>();
            final String[] AIR_IATACODES = new String[AirlineJSON.size()];
            final String[] Airlines = new String[AirlineJSON.size()];
            for(int i = 0; i < AirlineJSON.size(); i++) {
                AIR_IATACODES[i] = AirlineJSON.get(i).get("iata").toString();
                Airlines[i] =  AirlineJSON.get(i).get("name").toString();
                mAirlineCodes.put(AIR_IATACODES[i], Airlines[i]);
            }*/
            if (departureDate == null) {
                urlObj = new URL(URL_CHEAP_START + theOrigin + URL_MID + theDest + URL_CHEAP_END);
            }
            else
            {
                urlObj = new URL(URL_CHEAP_START + theOrigin + URL_MID + theDest + URL_CHEAP_DEPART + departureDate + URL_CHEAP_RETURN + returnDate + URL_CHEAP_END);
            }
            Log.d("TASK_URL", urlObj.toString());

            connection = (HttpURLConnection) urlObj.openConnection();
            InputStream content = connection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
        } catch (Exception e) {
            response = "Unable to connect, Reason: " + e.getMessage();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        if (response != null && !response.contains("\"data\":{}")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                String key = data.toString().substring(2,5);
                Log.d("JSON_RESULTS", "key = " + key);
                JSONObject data_actual = data.getJSONObject(key);
                Log.d("JSON_RESULTS", "data = " + data.toString());
                Log.d("JSON_RESULTS", "data_actual = " + data_actual.toString());

                for(int i = 0; i < data_actual.length(); i++) {
                    JSONObject d = data_actual.getJSONObject("" + i);
                    Log.d("JSON_RESULTS", "d = " + d.toString());
                    // add the flight prices to Arraylist for the graph data

                    sb.add(d.getInt("price"));
                    // add values to Strings for hashmap
                   // String depart_date = "Departure Date: " + d.getString("depart_at");
                   // String return_date = "Return Date: " + d.getString("return_at");
                   // String value = "Ticket Price: $" + d.getString("price");
                   // String origin = "Airline: " + d.getString("airline");
                   // String destination = "Flight Number: " + d.getInt("flight_number");
                    String trueName = null;

                    trueName = mAirlineCodes.get(d.getString("airline"));
                    Log.d("TRUENAME", d.getString("airline"));
                    if(trueName != null)
                        Log.d("TRUENAME", trueName);
                    else
                        Log.d("TRUENAME", "NULL");

                    Flights flight = new Flights(d.getString("departure_at"),
                            d.getString("return_at"),
                            d.getString("price"),
                            d.getString("airline"),
                            d.getInt("flight_number"),
                            trueName);
                    // add strings to hashmap
                    /*HashMap<String, String> hashData = new HashMap<>();
                    hashData.put("depart_date", depart_date);
                    hashData.put("return_date", return_date);
                    hashData.put("value", value);
                    hashData.put("origin", origin);
                    hashData.put("destination", destination);*/

                    //add hashmap back to the HashMap ArrayList
                    flight.setSortBy(mSortOption);
                    dataJSON.add(flight);

                }

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        //change the return statement to dataJSON.toString() to print out the Hashmap with all the data.
        return sb.toString();

    }

    /**
     * When doInBackground has finished executing, this method will create a new recylcer view
     * to display the result of the API call.
     * @param result The result of doInBackground, which is the result of the API call.
     */
    @Override
    protected void onPostExecute(String result)
    {

        FlightListRecyclerView adapter;


        //RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Log.d("FlightSearchTask", result);
        if(result.length() < 3)
        {
            result = "Sorry, no results were found.";
            AlertDialog.Builder buildalert = new AlertDialog.Builder(mContext);
            buildalert.setMessage("Sorry, no results were found.");
            buildalert.setCancelable(true);
            buildalert.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = buildalert.create();
            alert.show();
            ((FragmentActivity) mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new FlightSearchFragment())
                    .addToBackStack(null).commit();

            return;
        }
        //dataJSON.sort();
        Collections.sort(dataJSON);

        adapter = new FlightListRecyclerView(dataJSON, email);
        mRecyclerView.setAdapter(adapter);

        Integer[] stockArr = new Integer[sb.size()];
        stockArr = sb.toArray(stockArr);
        //TextView tx_results = getActivity().findViewById(R.id.tx_flight_list);
        //mTx_results.setText(result);
        //graphArr = stockArr;

    }


    private void initializeTask(Context theContext, RecyclerView theRecycler, TextView theTextView, int sortOption, String email)

    {
        mContext = theContext;
        mRecyclerView = theRecycler;
        mTx_results = theTextView;
        mSortOption = sortOption;
        this.email = email;
    }

}
