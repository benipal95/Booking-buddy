package group10.tcss450.uw.edu.bookingbuddy.Frontend.Login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import group10.tcss450.uw.edu.bookingbuddy.Backend.User.EmailVerificationTask;
import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * @author Lorenzo Pacis
 * This class defines the UI elements and behavior of the register screen of the
 * application.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
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
    public RegisterFragment() {
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
                    if(registerPassword.getText().toString().length() > 5) {
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
         */
        void registerFragmentInteraction(String verificationCode, String email);
    }


    /**
     * This inner class will be used for creating a new AsyncTask to register the user in the database
     * so long as the user does not already exist in the database, and their information is valid.
     * It will also register the user into Firebase for email verification.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "insertnew.php";
        Random rand = new Random();
        int randomGen = rand.nextInt(2000) + 1000;
        String code = randomGen +"";


        /**
         * Performs a background task, which will make a POST call to register
         * a new user in the database.
         * @param strings The arguments to be used to create the POST call.
         * @return the result of the POST call.
         */
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
                        + URLEncoder.encode(strings[2], "UTF-8") + "&" + URLEncoder.encode("rp", "UTF-8") + "=no"
                        +"&" + URLEncoder.encode("code","UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");
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


        /**
         * When the background task has finished executing,
         * if a user has been created in the database this method will
         * also create a new user in Firebase and send them an email to verify that they
         * own that email.
         * @param result The result of doInBackground.
         */
        @Override
        protected void onPostExecute(String result) {
            Log.d("RESULT", result);
            if (result.equals("User Created")) {
                EmailVerificationTask verifyEmailTask = new EmailVerificationTask();
                verifyEmailTask.execute(code,registerUsername.getText().toString());
                mListener.registerFragmentInteraction(code, registerUsername.getText().toString());
                Log.d("RESULT", "EMAIL SENT");

            }
            if (result.equals("username already in database")){
                registerUsername.setError("Username already registered");

            }
        }
    }
}
