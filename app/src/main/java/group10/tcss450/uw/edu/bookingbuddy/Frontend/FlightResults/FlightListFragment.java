package group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.FlightSearchTask;
import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * @author Jacob
 * This class will display all UI elements for when a user searches for flights
 * will display all the flights from the origin to the destination.
 */
public class FlightListFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private OnGraphInteractionListener gListener;
    private static Integer[] graphArr;
    private static String origin;
    private static String dest;
    private RecyclerView recyclerView;
    /**
     * Empty Constructor
     */
    public FlightListFragment() {
        // Required empty public constructor
    }

    /**
     * When this fragment is started,
     * Gets the arguments passed in by the bundle.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        if (getArguments() != null)
        {
            origin = getArguments().getString("ORIGIN");
            dest = getArguments().getString("DESTI");
            int sorting = getArguments().getInt("SORT");

            String email = getArguments().getString("email");
            AsyncTask<String, Void, String> task = null;


            recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list);

            TextView tx_results = getActivity().findViewById(R.id.tx_flight_list);
            Context context = getContext();



            task = new FlightSearchTask(context, recyclerView, tx_results, sorting, email);

            //task.initializeTask(context, recyclerView, tx_results);
            task.execute(origin, dest);

        }
    }

    /**
     * When this view is created, instantiates all UI elements necessary for the fragment.
     * @param inflater The inflater.
     * @param container This fragments container.
     * @param savedInstanceState The saved instance of this fragment.
     * @return The view that is created by this method.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View searchView = inflater.inflate(R.layout.fragment_flight_list, container, false);
       // Button graphButton = (Button) searchView.findViewById(R.id.graph_submit);
        //graphButton.setOnClickListener(this);
        View searchView2 = inflater.inflate(R.layout.list_recycler_layout, container, false);
        return searchView;
    }


    /**
     * When attached, sets the context of the mListener and the gListener.
     * @param context The application context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            gListener = (OnGraphInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * When this fragment is detached, sets the mListener and gListeners contexts to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        gListener = null;
    }

    /**
     * When the graph button is clicked, calls the onGraphSubmit method.
     * @param view
     */
    @Override
    public void onClick(View view) {
        gListener.onGraphSubmit(graphArr);
        Log.d("grapharr", graphArr.toString());
    }
//
//
//    private Integer[] getGraphArr(void) {
//        graphArr = temp;
//    }

    /**
     * This interface must be implemented by the activity that will be creating this fragment.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri); // Don't know yet if we'll need this, but including it just in case
    }

    /**
     * This interface is implemented in this class, when the view graph button is pressed,
     * the method onGraphSubmit wil be called.
     */
    public interface OnGraphInteractionListener {
        void onGraphSubmit(Integer[] arr);
    }

    /**
     * This class handles the actions to be taken when a new FlightSearchTask is executed and will make
     * calls to the API to get the flights that the user is searching for.
     */

//    private class FlightSearchTask extends AsyncTask<String, Void, String>
//    {
//        private static final String URL_FIRST = "http://api.travelpayouts.com/v2/prices/latest?currency=usd&period_type=year&page=1&limit=10&origin=";
//        private static final String URL_MID = "&destination=";
//        private static final String URL_LAST = "&show_to_affiliates=true&sorting=price&trip_class=0&token=9f0202d35e6767803ce5e453f702e6f6";
//        private ArrayList<Integer> sb = new ArrayList<>();
//        ArrayList<HashMap<String, String>> dataJSON;
//
//
//        /**
//         * This method will make a call to the API and create new JSON objects and create a hashmap of their values that will be later
//         * used by a recycler view and a graph class to display this information.
//         * @param strings
//         * @return The result of the API call to the webservice.
//         */
//        @Override
//        protected String doInBackground(String... strings) {
//            String response = "";
//            HttpURLConnection connection = null;
//            dataJSON = new ArrayList<>();
//
//
//            try {
//                URL urlObj = new URL(URL_FIRST + origin + URL_MID + dest + URL_LAST);
//
//                connection = (HttpURLConnection) urlObj.openConnection();
//                InputStream content = connection.getInputStream();
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//                String s = "";
//                while ((s = buffer.readLine()) != null) {
//                    response += s;
//                }
//            } catch (Exception e) {
//                response = "Unable to connect, Reason: " + e.getMessage();
//            } finally {
//                if (connection != null)
//                    connection.disconnect();
//            }
//
//            if (response != null) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray data = jsonObject.getJSONArray("data");
//
//                    for(int i = 0; i < data.length(); i++) {
//                        JSONObject d = data.getJSONObject(i);
//                        // add the flight prices to Arraylist for the graph data
//                        sb.add(d.getInt("value"));
//                        // add values to Strings for hashmap
//                        String depart_date = d.getString("depart_date");
//                        String return_date = d.getString("return_date");
//                        String value = d.getString("value");
//                        String origin = d.getString("origin");
//                        String destination = d.getString("destination");
//
//                        // add strings to hashmap
//                        HashMap<String, String> hashData = new HashMap<>();
//                        hashData.put("depart_date", depart_date);
//                        hashData.put("return_date", return_date);
//                        hashData.put("value", value);
//                        hashData.put("origin", origin);
//                        hashData.put("destination", destination);
//
//                        //add hashmap back to the HashMap ArrayList
//                        dataJSON.add(hashData);
//
//                    }
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                }
//            }
//            //change the return statement to dataJSON.toString() to print out the Hashmap with all the data.
//            return sb.toString();
//
//        }
//
//        /**
//         * When doInBackground has finished executing, this method will create a new recylcer view
//         * to display the result of the API call.
//         * @param result The result of doInBackground, which is the result of the API call.
//         */
//        @Override
//        protected void onPostExecute(String result)
//        {
//
//            FlightListRecyclerView adapter;
//
//            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            adapter = new FlightListRecyclerView((ArrayList) dataJSON);
//            //adapter.setClickListener(this);
//            recyclerView.setAdapter(adapter);
//
//            Integer[] stockArr = new Integer[sb.size()];
//            stockArr = sb.toArray(stockArr);
//            TextView tx_results = getActivity().findViewById(R.id.tx_flight_list);
//            tx_results.setText(result);
//            graphArr = stockArr;
//
//        }
//
//    }
}

