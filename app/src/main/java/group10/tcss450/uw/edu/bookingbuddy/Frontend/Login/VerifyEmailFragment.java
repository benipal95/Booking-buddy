package group10.tcss450.uw.edu.bookingbuddy.Frontend.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group10.tcss450.uw.edu.bookingbuddy.Backend.User.VerifyUserTask;
import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class VerifyEmailFragment extends Fragment {

    private VerifyEmailFragmentInteractionListener mListener;
    private EditText verificationCode;
    private Button submitVerificationButton;

    public VerifyEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        final String code = (String) args.getSerializable("code");
        final String email = (String) args.getSerializable("email");
        View v = inflater.inflate(R.layout.fragment_verify_email, container, false);
        verificationCode = v.findViewById(R.id.emailVerificationCode);
        submitVerificationButton = v.findViewById(R.id.verifyEmailButton);
        submitVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.equals(verificationCode.getText().toString())) {
                    VerifyUserTask verify = new VerifyUserTask();
                    verify.execute(email,code);
                    mListener.VerifyEmailFragmentInteraction();
                }

            }
        });
        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VerifyEmailFragmentInteractionListener) {
            mListener = (VerifyEmailFragmentInteractionListener) context;
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

    public interface VerifyEmailFragmentInteractionListener {
        // TODO: Update argument type and name
        void VerifyEmailFragmentInteraction();
    }
}
