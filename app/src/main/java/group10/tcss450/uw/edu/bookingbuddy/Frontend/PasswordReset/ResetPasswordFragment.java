package group10.tcss450.uw.edu.bookingbuddy.Frontend.PasswordReset;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResetPasswordFragment.resetFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    EditText inputCode;
    Button contiuneButton;
    private resetFragmentInteractionListener mListener;

    public ResetPasswordFragment() {
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
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        final String resetCode = (String) args.getSerializable("Code");
        final String email = (String) args.getSerializable("Email");
        View v =inflater.inflate(R.layout.fragment_reset_password, container, false);
        inputCode = v.findViewById(R.id.resetCodeText);
        contiuneButton = v.findViewById(R.id.resetPasswordButton);
        contiuneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputCode.getText().toString().equals(resetCode)) {
                    mListener.resetFragmentInteraction(email);
                }

            }
        });
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof resetFragmentInteractionListener) {
            mListener = (resetFragmentInteractionListener) context;
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
    public interface resetFragmentInteractionListener {
        // TODO: Update argument type and name
        void resetFragmentInteraction(String email);
    }
}
