package group10.tcss450.uw.edu.bookingbuddy.Backend.Flight;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightListRecyclerView;


/**
 * Created by lorenzopacis on 12/1/2017.
 */

public class GetSavedFlightsTask extends AsyncTask<String, Void, String> {
        private static final String URL
            = "http://cssgate.insttech.washington.edu/~pacis93/fetchUserFlights.php?";
        private RecyclerView recView;
        private String email;
        private Context mContext;

    public GetSavedFlightsTask(RecyclerView recyclerView, Context theContext) {
        super();
        recView = recyclerView;
        mContext = theContext;


    }
        @Override
        protected String doInBackground(String... strings) {

            if (strings.length != 1) {
                throw new IllegalArgumentException("One String arguments required.");
            }
            Log.d("Getting to here", "in DIB");
            String response = "";
            String userEmail= strings[0];
            email = userEmail;
            String theEmail = "";
            for(int i = 0; i < userEmail.length(); i++) {
                if(userEmail.charAt(i) != '@' && userEmail.charAt(i) != '.') {
                    theEmail+=userEmail.charAt(i);
                }
            }
            Log.d("Email" , userEmail);
            String args = "table=" + theEmail + "_flights";
            String result = "";
            try{
                HttpURLConnection urlConnection = null;
                URL urlObject = new URL(URL + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    result += s;

                }
            } catch (Exception e) {
                result = "Unable to connect, Reason: "
                        + e.getMessage();
            }


            return result;
        }




        @Override
        protected void onPostExecute(String result) {
            Log.d("result of get tables", result);
            FlightListRecyclerView adapter;
            ArrayList<Flights> savedFlights = new ArrayList<>();
            String delimiter = " ";
            String[] arr = result.split(delimiter);
            for(int i = 0; i < arr.length; i++) {
                String delim = ",";
                String[] values = arr[i].split(delim);
                Flights flight = new Flights(values[2], values[3], values[4], values[0], Integer.parseInt(values[1]), values[0]);
                flight.setSaved();
                savedFlights.add(flight);
            }

            recView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new FlightListRecyclerView(savedFlights, email);
            recView.setAdapter(adapter);
        }



}
