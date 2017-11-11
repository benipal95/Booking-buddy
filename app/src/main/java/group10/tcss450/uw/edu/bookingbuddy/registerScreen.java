package group10.tcss450.uw.edu.bookingbuddy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Lorenzo Pacis
 * This class defines the UI elements and behavior of the register screen of the
 * application.
 */
public class registerScreen extends Fragment implements View.OnClickListener {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~pacis93/";
    private registerFragmentInteractionListener mListener;
    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerComfirmPasword;
    private Button registerButton;
    private FirebaseAuth mAuth;

    /**
     * Empty Constructor.
     */
    public registerScreen() {
        // Required empty public constructor
    }

    /**
     * Handles the actions to be taken when a view is clicked, namely
     * when the submit button when registering is clicked. It will perform checks
     * to make sure that the email is in a valid format and that the passwords match
     * and the password length is long enough.
     * @param v the view that has been clicked.
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            AsyncTask<String, Void, String> task = null;
            String emailAddress = registerUsername.getText().toString();
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {

                if(registerPassword.getText().toString().equals(registerComfirmPasword.getText().toString())) {
                    if(registerPassword.getText().toString().length() > 6) {
                        task = new PostWebServiceTask();
                        task.execute(PARTIAL_URL, registerUsername.getText().toString().toLowerCase(), registerPassword.getText().toString());
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Password to short/weak please try again", Toast.LENGTH_LONG);
                        toast.show();
                    }


                } else {
                    registerPassword.setError("Passwords Not Matching");
                }
            } else {
                registerUsername.setError("Must be a valid email.");
            }

        }
    }

    /**
     * Sets up all UI elements for the registration fragment.
     * @param inflater The inflater
     * @param container The container of this fragment.
     * @param savedInstanceState The saved instance of this fragment when it has been detached.
     * @return returns the view created by this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("STUFF", "ONCREATE");
        View v = inflater.inflate(R.layout.fragment_register_screen, container, false);
        registerUsername = (EditText) v.findViewById(R.id.registerUsername);
        registerComfirmPasword = (EditText) v.findViewById(R.id.registerComfirmPasword);
        registerPassword = (EditText) v.findViewById(R.id.registerPassword);
        registerButton  = (Button) v.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        return v;
    }


    /**
     * When this fragment is attached, sets the mListeners context to this fragment.
     * @param context The context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof registerFragmentInteractionListener) {
            mListener = (registerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Factory detach method. Sets the mListener to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * The interface which must be implemented by the activity that wants to create and display
     * this fragment.
     */
    public interface registerFragmentInteractionListener {

        /**
         * The method that must be implemeneted by the activity. This method currently
         * passes the username and password of the user, however this functionality is no longer
         * needed in the current implementation of this application
         * TODO:Update this method later when needed, current implementation is unnecessary and potentially dangerous to user data.
         * @param username the email of the user.
         * @param password the password of the user.
         */
        void registerFragmentInteraction(String username, String password);
    }


    /**
     * This inner class will be used for creating a new AsyncTask to register the user in the database
     * so long as the user does not already exist in the database, and their information is valid.
     * It will also register the user into Firebase for email verification.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "insertnew.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("first_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8") +"&" + URLEncoder.encode("user_pass","UTF-8") + "="
                        + URLEncoder.encode(strings[2], "UTF-8") + "&" + URLEncoder.encode("rp", "UTF-8") + "=no";
                Log.d(data, "DATA ");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("RESULT", result);
            if (result.equals("User Created")) {

                mAuth.createUserWithEmailAndPassword(registerUsername.getText().toString(), registerPassword.getText().toString());
                FirebaseUser user = null;
                mAuth.signInWithEmailAndPassword(registerUsername.getText().toString(), registerPassword.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SUCCESS", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {

                                            if (task.isSuccessful()) {
                                                Log.d("SUCESS", "EMAIL SENT");

                                            } else {
                                                Log.e("NOPE", "sendEmailVerification failed", task.getException());

                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("PROBLEM", "signInWithEmail:failure", task.getException());

                                }
                            }
                        });

                mListener.registerFragmentInteraction("PHP MESSAGE", result);
                Toast toast = Toast.makeText(getContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG);
                toast.show();
            }
            if (result.equals("username already in database")){
                registerUsername.setError("Username already registered");

            }
        }
    }
}
