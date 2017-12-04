package group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.AirlineCodeParser;
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
    private String usersEmail;
    ArrayList<String> mAirlineCodes;

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
        AirlineCodeParser air_parse = new AirlineCodeParser(getContext());
        try {
            mAirlineCodes = air_parse.parseAirlineJSON_AsList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        View theview = inflater.inflate(R.layout.fragment_flight_search, container, false);
        Button thebutton = (Button) theview.findViewById(R.id.b_submit);
        thebutton.setOnClickListener(this);
        Button departButton = theview.findViewById(R.id.button_depart_date);
        Button returnButton = theview.findViewById(R.id.button_return_date);
        departButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        CheckBox airlineFilter = theview.findViewById(R.id.cb_filter_airline);
        airlineFilter.setOnClickListener(this);

        AutoCompleteTextView spinner = theview.findViewById(R.id.spinner);

        List<String> spinnerArray =  new ArrayList<String>();

        for(int i = 0; i < mAirlineCodes.size(); i++)
        {
            spinnerArray.add(mAirlineCodes.get(i));
        }
        //spinnerArray.add("item1");
        //spinnerArray.add("item2");

        ArrayAdapter<String> airlineArray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        spinner.setAdapter(airlineArray);

        Bundle args = getArguments();

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

        DialogFragment alert = null;
        if(view.getId() == getActivity().findViewById(R.id.b_submit).getId()) {
            String originTx = originText.getText().toString();
            String destTx = destinationText.getText().toString();
            if(originTx.isEmpty() && destTx.isEmpty())
            {
                AlertDialog.Builder buildalert = new AlertDialog.Builder(getContext());
                buildalert.setMessage("Please enter the names of the airports you would like to start from and end at.");
                buildalert.setCancelable(true);
                buildalert.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = buildalert.create();
                alert1.show();
                originText.setError("Please specify a starting airport");
                destinationText.setError("Please specify a destination airport");
            }
            else if(originTx.isEmpty())
            {
                AlertDialog.Builder buildalert = new AlertDialog.Builder(getContext());
                buildalert.setMessage("Please enter the name of the airport you would like to start from.");
                buildalert.setCancelable(true);
                buildalert.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = buildalert.create();
                alert1.show();
                originText.setError("Starting airport missing!");
                //destinationText.setError("Please specify a destination airport");
            }
            else if(destTx.isEmpty())
            {
                AlertDialog.Builder buildalert = new AlertDialog.Builder(getContext());
                buildalert.setMessage("Please enter the name of the airport you would like to end at.");
                buildalert.setCancelable(true);
                buildalert.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = buildalert.create();
                alert1.show();
                destinationText.setError("Destination airport missing!");
            }
            else if (originText.getText().toString().equals(destinationText.getText().toString())) {
                AlertDialog.Builder buildalert = new AlertDialog.Builder(getContext());
                buildalert.setMessage("Your destination and origin airports are the same. City tours by air are beyond our scope.\n\nSorry.");
                buildalert.setCancelable(true);
                buildalert.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = buildalert.create();
                alert1.show();
                originText.setError("Destination and Origin cannot be the same.");

            } else {
                CheckBox checker = getActivity().findViewById(R.id.cb_filter_airline);
                AutoCompleteTextView filter = getActivity().findViewById(R.id.spinner);
                if(checker.isChecked() && !filter.getText().toString().isEmpty())
                    mListener.onSearchSubmit(airportCodes.get(originText.getText().toString()), airportCodes.get(destinationText.getText().toString()), filter.getText().toString());
                else if(checker.isChecked() && !filter.getText().toString().isEmpty())
                    filter.setError("Please enter an airline name to filter by.");
                else
                    mListener.onSearchSubmit(airportCodes.get(originText.getText().toString()), airportCodes.get(destinationText.getText().toString()));
            }
        } else if(view.getId() == getActivity().findViewById(R.id.button_depart_date).getId()) {

            alert = new DepartureDatePickerFragment();

        } else if(view.getId() == getActivity().findViewById(R.id.button_return_date).getId()) {

            alert = new ReturnDatePickerFragment();
        }
        else if(view.getId() == getActivity().findViewById(R.id.cb_filter_airline).getId())
        {
            CheckBox checker = getActivity().findViewById(R.id.cb_filter_airline);

            if(checker.isChecked())
            {
                AutoCompleteTextView spin = getActivity().findViewById(R.id.spinner);
                spin.setVisibility(View.VISIBLE);
            }
            else
            {
                AutoCompleteTextView spin = getActivity().findViewById(R.id.spinner);
                spin.setText("");
                spin.setVisibility(View.GONE);
            }
        }

        if(alert != null) {

            alert.show(getActivity().getSupportFragmentManager(), "launch");
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
        void onSearchSubmit(String origin, String destination, String airlineFilter);
    }

    public static class DepartureDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Warning - month is 0 based.
            //Toast.makeText(getActivity(), "You picked " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year,
            //        Toast.LENGTH_LONG).show();
            TextView text = getActivity().findViewById(R.id.button_depart_date);
            if(monthOfYear < 9)
                text.setText("Departure: " + year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
            else
                text.setText("Departure: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }

    public static class ReturnDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Warning - month is 0 based.
            //Toast.makeText(getActivity(), "You picked " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year,
            //        Toast.LENGTH_LONG).show();
            TextView text = getActivity().findViewById(R.id.button_return_date);
            if(monthOfYear < 9)
                text.setText("Return: " + year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
            else
                text.setText("Return: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }
}
