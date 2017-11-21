package group10.tcss450.uw.edu.bookingbuddy.Frontend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * @author Lorenzo Pacis
 * This class defines the UI fragment that will be displayed upon application
 * startup.
 */
public class StartupFragment extends Fragment implements View.OnClickListener {

    private splashFragmentInteractionListener mListener;

    /**
     * Empty constructor.
     */
    public StartupFragment() {
        // Required empty public constructor
    }

    /**
     * When this view is created, instantiates all UI elements.
     * @param inflater The inflater.
     * @param container The container of this fragment.
     * @param savedInstanceState The saved instance of this fragment when the backbutton
     *                           is pressed.
     * @return Returns the view that is created inside of this method.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_startup_screen, container, false);
        Button b = (Button) v.findViewById(R.id.loginButton);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.registerButton);
        b.setOnClickListener(this);

        return v;
    }

    /**
     * Handles what happens when either the login
     * or register buttons are pressed and notifies the
     * main activity.
     * @param v Which UI element has been clicked.
     */
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

    /**
     * When this fragment is attached, sets the mListeners context to this fragment.
     * @param context
     */
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

    /**
     * Handles the detaching of this fragment.
     * Sets the mListener to null, removing the context.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface that must be implemented by the activity
     * that wants to create this fragment.
     */
    public interface splashFragmentInteractionListener {

        /**
         * Method that must be implemented in the activity
         * that implements this interface.
         * @param selection The selected UI element, either
         *                  login or register.
         */
        void splashFragmentInteraction(int selection);
    }
}
