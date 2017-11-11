package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author Jacob
 * This class will handle displaying all UI elements for when a user
 * wants to search for a flight.
 */
public class FlightSearchFragment extends Fragment implements View.OnClickListener {

    private Spinner originLocation;
    private Spinner destLocation;
    private OnSearchSubmitListener mListener;

    /**
     * Empty constructor.
     */
    public FlightSearchFragment() {
        // Required empty public constructor
    }


    /**
     * When the view is created, instantiates all UI elements and populates the spinners
     * with IATA Codes.
     * @param inflater The inflater.
     * @param container The fragments container,
     * @param savedInstanceState The saved instance of this fragment.
     * @return The view that this method creates.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theview = inflater.inflate(R.layout.fragment_flight_search, container, false);
        Button thebutton = (Button) theview.findViewById(R.id.b_submit);
        thebutton.setOnClickListener(this);
        destLocation = theview.findViewById(R.id.dest);
        originLocation = theview.findViewById(R.id.origin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.IATA_Codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destLocation.setAdapter(adapter);
        originLocation.setAdapter(adapter);
        return theview;
    }


    /**
     * When attached, will set the context of the mListener to this fragment.
     * @param context This fragments context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchSubmitListener) {
            mListener = (OnSearchSubmitListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * On detach sets the mListener's context to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handles when a view is clicked. If the origin and the destination are not the same,
     * it will call onSearchSubmit, which is this fragments interaction.
     * If they are the same, then it will display a toast notiying the user that the origin
     * and destination cannot be the same place.
     * @param view
     */
    @Override
    public void onClick(View view) {

        if(originLocation.getSelectedItem().toString().equals(destLocation.getSelectedItem().toString())) {
            Toast toast = Toast.makeText(getContext(), "The destination and origin cannot be the same.", Toast.LENGTH_LONG);
            toast.show();

        } else {
            mListener.onSearchSubmit(originLocation.getSelectedItem().toString(), destLocation.getSelectedItem().toString());
        }


    }

    /**
     * This interface must be implemented by the activity or fragment that wants to
     * create and display this fragment.
     */
    public interface OnSearchSubmitListener {
        /**
         * This method must be implemented by whichever class implements this interface.
         * @param origin The origin IATA code.
         * @param destination The destination IATA code.
         */
        void onSearchSubmit(String origin, String destination);
    }


}
