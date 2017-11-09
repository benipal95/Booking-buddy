package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FlightSearchFragment.OnSearchSubmitListener} interface
 * to handle interaction events.
 */
public class FlightSearchFragment extends Fragment implements View.OnClickListener {


    private OnSearchSubmitListener mListener;

    public FlightSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theview = inflater.inflate(R.layout.fragment_flight_search, container, false);
        Button thebutton = (Button) theview.findViewById(R.id.b_submit);
        thebutton.setOnClickListener(this);

        return theview;
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        EditText dest = getActivity().findViewById(R.id.edtx_destination);
        EditText orig = getActivity().findViewById(R.id.edtx_origin);

        mListener.onSearchSubmit(orig.getText().toString(), dest.getText().toString());
    }

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
    public interface OnSearchSubmitListener {
        // TODO: Update argument type and name
        void onSearchSubmit(String origin, String destination);
    }


}
