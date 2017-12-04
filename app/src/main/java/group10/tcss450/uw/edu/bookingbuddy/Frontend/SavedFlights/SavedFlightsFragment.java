package group10.tcss450.uw.edu.bookingbuddy.Frontend.SavedFlights;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.FlightSearchTask;
import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.GetSavedFlightsTask;
import group10.tcss450.uw.edu.bookingbuddy.R;


public class SavedFlightsFragment extends Fragment {


    private SavedFlightsFragmentListener mListener;
    private RecyclerView recyclerView;
    private String email;
    public SavedFlightsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        Bundle args = getArguments();
        email = (String) args.get("email");
    }

    @Override
    public void onStart(){
        super.onStart();
        recyclerView =  getActivity().findViewById(R.id.saved_result_list);
        GetSavedFlightsTask task = new GetSavedFlightsTask(recyclerView, getContext());
        task.execute(email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_saved_list, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SavedFlightsFragmentListener) {
            mListener = (SavedFlightsFragmentListener) context;
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
    public interface SavedFlightsFragmentListener {
        // TODO: Update argument type and name
        void SavedFlightsFragmentInteraction(Uri uri);
    }
}
