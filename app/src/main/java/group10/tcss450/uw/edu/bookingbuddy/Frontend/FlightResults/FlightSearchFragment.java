package group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.IATACodeParser;
import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * @author Jacob
 * This class will handle displaying all UI elements for when a user
 * wants to search for a flight.
 */
public class FlightSearchFragment extends Fragment implements View.OnClickListener {

    private HashMap<String, String> airportCodes;
    private AutoCompleteTextView destinationText;
    private AutoCompleteTextView originText;
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

       // destLocation.setAdapter(adapter);
     //   originLocation.setAdapter(adapter);
        IATACodeParser parser = new IATACodeParser(getActivity());
        List<HashMap> dataJSON = new ArrayList<>();
        try {
            dataJSON = (ArrayList) parser.parseIATAJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        airportCodes = new HashMap();
        final String[] IATACODES = new String[dataJSON.size()];
        final String[] Airports = new String[dataJSON.size()];
        for(int i = 0; i < dataJSON.size(); i++) {
            IATACODES[i] = dataJSON.get(i).get("iata").toString();
            Airports[i] =  dataJSON.get(i).get("name").toString();
            airportCodes.put(Airports[i], IATACODES[i]);
        }
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,IATACODES);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,Airports);
        originText = (AutoCompleteTextView) theview.findViewById(R.id.flight_search_origin);
        destinationText = (AutoCompleteTextView) theview.findViewById(R.id.flight_search_destination);
        originText.setAdapter(adapter);
        destinationText.setAdapter(adapter);

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

       if(originText.getText().toString().equals(destinationText.getText().toString())) {
            originText.setError("Destination and Origin cannot be the same.");

        } else {

           mListener.onSearchSubmit(airportCodes.get(originText.getText().toString()), airportCodes.get(destinationText.getText().toString()));
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
