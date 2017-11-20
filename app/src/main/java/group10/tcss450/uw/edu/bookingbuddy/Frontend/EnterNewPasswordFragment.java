package group10.tcss450.uw.edu.bookingbuddy.Frontend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group10.tcss450.uw.edu.bookingbuddy.Backend.ResetPasswordTask;
import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class EnterNewPasswordFragment extends Fragment {

    EditText newPass;
    EditText confirmNewPass;
    Button submitNewPass;

    private EnterNewPasswordFragmentInteractionListener mListener;

    public EnterNewPasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String email = (String) args.getSerializable("Email");
        View v =  inflater.inflate(R.layout.fragment_enter_new_password, container, false);
        submitNewPass = v.findViewById(R.id.confirmNewPasswordButton);
        newPass = v.findViewById(R.id.newPassword);
        confirmNewPass = v.findViewById(R.id.confirmNewPassword);

        submitNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checks = false;
                if(newPass.getText().toString().equals(confirmNewPass.getText().toString())) {
                    checks = true;
                } else {
                    newPass.setError("Passwords do not match");
                }
                if(newPass.getText().length() < 5) {
                    newPass.setError("Password Length must be more than five characters");
                    checks = false;
                }
                if(checks) {
                    ResetPasswordTask newPassTask = new ResetPasswordTask();
                    newPassTask.execute(email,newPass.getText().toString());
                    mListener.EnterNewPasswordFragmentInteraction(email);
                }

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EnterNewPasswordFragmentInteractionListener) {
            mListener = (EnterNewPasswordFragmentInteractionListener) context;
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
    public interface EnterNewPasswordFragmentInteractionListener {
        // TODO: Update argument type and name
        void EnterNewPasswordFragmentInteraction(String email);
    }
}
