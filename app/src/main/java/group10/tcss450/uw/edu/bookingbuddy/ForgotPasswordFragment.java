package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Lorenzo Pacis
 * This class defines the UI elements of the forgot password fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private forgotPasswordInteractionListener mListener;
    private FirebaseAuth auth;
    Button resetButton;
    EditText resetEmail;

    /**
     * Empty Constructor
     */
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    /**
     * Initializes all UI elements when the view is created.
     * @param inflater The inflater.
     * @param container The container holding this fragment.
     * @param savedInstanceState The saved instance of this fragment when the back button is pressed.
     * @return The view that is returned when created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "Enter your registered email address", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        resetButton = v.findViewById(R.id.resetPasswordButton);
        resetEmail = v.findViewById(R.id.forgotPasswordEmail);
        auth = FirebaseAuth.getInstance();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString().trim();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }



        });

        return v;
    }


    /**
     * When this fragment is attached sets the mListeners context to this fragment.
     * @param context returns the context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof forgotPasswordInteractionListener) {
            mListener = (forgotPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Factory on detatch, sets the mListener to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * The interface that must be implemented by the activity that creates
     * this fragment.
     */
    public interface forgotPasswordInteractionListener {


        /**
         * Not currently in use
         * @param uri
         */
        void forgotPasswordInteraction(Uri uri);
    }
}
