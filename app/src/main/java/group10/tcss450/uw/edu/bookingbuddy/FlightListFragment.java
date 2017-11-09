package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FlightListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FlightListFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private OnGraphInteractionListener gListener;
    private static Integer[] graphArr;
    private static String origin;
    private static String dest;
    public FlightListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getArguments() != null)
        {
            origin = getArguments().getString("ORIGIN");
            dest = getArguments().getString("DESTI");
            AsyncTask<String, Void, String> task = null;

            task = new FlightSearchTask();
            task.execute(origin, dest);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View searchView = inflater.inflate(R.layout.fragment_flight_list, container, false);
        Button graphButton = (Button) searchView.findViewById(R.id.graph_submit);
        graphButton.setOnClickListener(this);
        return searchView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        gListener = null;
    }

    @Override
    public void onClick(View view) {
        gListener.onGraphSubmit(graphArr);
        Log.d("grapharr", graphArr.toString());
    }
//
    private void setGraphArr(Integer[] temp) {
        graphArr = new Integer[10];
        graphArr = temp;
        Log.d("grapharrset", graphArr.toString());

    }
//
//    private Integer[] getGraphArr(void) {
//        graphArr = temp;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri); // Don't know yet if we'll need this, but including it just in case
    }

    public interface OnGraphInteractionListener {
        void onGraphSubmit(Integer[] arr);
    }

    private class FlightSearchTask extends AsyncTask<String, Void, String>
    {
        private static final String URL_FIRST = "http://api.travelpayouts.com/v2/prices/latest?currency=usd&period_type=year&page=1&limit=10&origin=";
        private static final String URL_MID = "&destination=";
        private static final String URL_LAST = "&show_to_affiliates=true&sorting=price&trip_class=0&token=9f0202d35e6767803ce5e453f702e6f6";
        private ArrayList<Integer> sb = new ArrayList<>();
        ArrayList<HashMap<String, String>> dataJSON;



        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection connection = null;
            dataJSON = new ArrayList<>();


            try {
                URL urlObj = new URL(URL_FIRST + origin + URL_MID + dest + URL_LAST);

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
                        String depart_date = d.getString("depart_date");
                        String return_date = d.getString("return_date");
                        String value = d.getString("value");
                        String origin = d.getString("origin");
                        String destination = d.getString("destination");

                        // add strings to hashmap
                        HashMap<String, String> hashData = new HashMap<>();
                        hashData.put("depart_date", depart_date);
                        hashData.put("return_date", return_date);
                        hashData.put("value", value);
                        hashData.put("origin", origin);
                        hashData.put("destination", destination);

                        //add hashmap back to the HashMap ArrayList
                        dataJSON.add(hashData);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
            //change the return statement to dataJSON.toString() to print out the Hashmap with all the data.
            return sb.toString();

        }
        @Override
        protected void onPostExecute(String result)
        {

            FlightListRecyclerView adapter;

            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.result_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new FlightListRecyclerView((ArrayList) dataJSON);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);

            Integer[] stockArr = new Integer[sb.size()];
            stockArr = sb.toArray(stockArr);
            TextView tx_results = getActivity().findViewById(R.id.tx_flight_list);
            tx_results.setText(result);
            graphArr = stockArr;

        }

    }
}

