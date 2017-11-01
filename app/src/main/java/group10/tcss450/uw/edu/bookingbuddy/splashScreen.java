package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link splashScreen.splashFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class splashScreen extends Fragment implements View.OnClickListener {

    private splashFragmentInteractionListener mListener;

    public splashScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        Button b = (Button) v.findViewById(R.id.loginButton);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.registerButton);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.loginButton:
                    mListener.splashFragmentInteraction(0);

                    break;
                case R.id.registerButton:
                    mListener.splashFragmentInteraction(1);
                    break;

    }
}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof splashFragmentInteractionListener) {
            mListener = (splashFragmentInteractionListener) context;
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
    public interface splashFragmentInteractionListener {
        // TODO: Update argument type and name
        void splashFragmentInteraction(int selection);
    }
}
