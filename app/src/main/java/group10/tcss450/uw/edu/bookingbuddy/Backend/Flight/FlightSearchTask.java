package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

/**
 * Created by jjtowers on 11/17/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightListRecyclerView;

import static android.content.ContentValues.TAG;

/**
 * This class handles the actions to be taken when a new FlightSearchTask is executed and will make
 * calls to the API to get the flights that the user is searching for.
 */
public class FlightSearchTask extends AsyncTask<String, Void, String>
{
    private static final String URL_FIRST = "http://api.travelpayouts.com/v2/prices/latest?currency=usd&period_type=year&page=1&limit=10&origin=";
    //private static final String URL_FIRST = "http://api.travelpayouts.com/v1/prices/cheap?currency=usd&origin=";
    private static final String URL_MID = "&destination=";
    private static final String URL_LAST = "&token=9f0202d35e6767803ce5e453f702e6f6";
    private ArrayList<Integer> sb = new ArrayList<>();
    ArrayList<Flights> dataJSON;
    RecyclerView mRecyclerView;
    TextView mTx_results;
    Context mContext;
    String mEmail;
    private int mSortOption;
    //Flights mFlights;


    public FlightSearchTask(Context theContext, RecyclerView theRecycler, TextView theTextView, int sortOption, String email)
    {
        super();
        initializeTask(theContext, theRecycler, theTextView, sortOption, email);
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

        try {
            URL urlObj = new URL(URL_FIRST + theOrigin + URL_MID + theDest + URL_LAST);

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

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray data = jsonObject.getJSONArray("data");

                for(int i = 0; i < data.length(); i++) {
                    JSONObject d = data.getJSONObject(i);
                    // add the flight prices to Arraylist for the graph data
                    sb.add(d.getInt("value"));
                    // add values to Strings for hashmap
                    String depart_date = "Departure Date: " + d.getString("depart_date");
                    String return_date = "Return Date: " + d.getString("return_date");
                    String value = "Ticket Price: $" + d.getString("value");
                    String origin = "Origin Airport: " + d.getString("origin");
                    String destination = "Destination Airport: " + d.getString("destination");

                    Flights flight = new Flights(d.getString("depart_date"),
                            d.getString("return_date"),
                            d.getString("value"),
                            d.getString("origin"),
                            d.getString("destination"));
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
            mTx_results.setText(result);
            return;
        }
        //dataJSON.sort();
        Collections.sort(dataJSON);

        adapter = new FlightListRecyclerView(dataJSON, mEmail);
        mRecyclerView.setAdapter(adapter);
        //adapter.
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
        mEmail = email;
    }

}