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
 * This class displays the login screen that a user will see when they choose to login.
 * It will also handle the GET call for when the user enters their information and attempts
 * to login to the system by checking the database to see if the user is present.
 * It will notify the user if their email has not been verified.
 */
public class loginScreen extends Fragment implements View.OnClickListener{
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~pacis93/";
    private loginFragmentInteractionListener mListener;
    private EditText loginUsername;
    private EditText loginPassword;
    private Button forgotPasswordButton;

    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    /**
     * Empty constructor.
     */
    public loginScreen() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        mAuth = FirebaseAuth.getInstance();
        super.onStart();
    }


    /**
     * @author Lorenzo Pacis
     * This method handles what happens on button clicks, namely when the submit button for
     * logging in is pressed. It will first check to see if logging into firebase is scuessful
     * and if so, perform a new AsyncTask and check our database to see if they are present.
     * @param v
     */
    @Override
    public void onClick(View v) {
        final AsyncTask<String, Void, String> loginTask = new GetWebServiceTask();
        if (mListener != null) {
            if(v.equals(loginButton)) {
                mAuth.signInWithEmailAndPassword(loginUsername.getText().toString().toLowerCase(), loginPassword.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SUCCCESS", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    setUser(user);
                                    loginTask.execute(PARTIAL_URL, loginUsername.getText().toString().toLowerCase(), loginPassword.getText().toString());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast toast = Toast.makeText(getContext(), "Unable to login, check email or password.", Toast.LENGTH_LONG);
                                    toast.show();
                                    Log.w("FAIL", "signInWithEmail:failure", task.getException());
                                }

                            }
                        });

            } else if(v.equals(forgotPasswordButton)) {
                mListener.loginFragmentInteraction(false);

            }

        }

    }

    /**
     * @author Lorenzo Pacis
     * This method instantiates all of the objects needed for this class,
     * namely all UI elements.
     * @param inflater The inflater.
     * @param container The container of this fragment.
     * @param savedInstanceState The instance of this fragment when the back button is pressed.
     * @return the view object V.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_screen, container, false);
        loginUsername = v.findViewById(R.id.loginUsername);
        loginPassword =  v.findViewById(R.id.loginPassword);
        loginButton = v.findViewById(R.id.loginButton);
        forgotPasswordButton = v.findViewById(R.id.forgotPassword);
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        return v;
    }

    /**
     * If the context is an instance of this fragment then
     * our mListener is set to this context.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof loginFragmentInteractionListener) {
            mListener = (loginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Factory onDetatch.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This sets the current user when loggin in.
     * The purpose of this method is ONLY for seeing if the attempted login of a
     * user in firebase is valid and if they have verified thier email. Firebase will
     * always return a non-null object, therefore checking for non null is redudant.
     * @param theUser The user who has logged in.
     */
    private void setUser(FirebaseUser theUser) {
        user = theUser;

    }

    /**
     * This is the fragment interaction listener for this class.
     * It must be implemented by the acitivty that it is to be created and
     * displayed by.
     */
    public interface loginFragmentInteractionListener {

        /**
         * This method will be called in the main activity and handles what
         * should happen when this fragment interaction occurs.
         * @param result The result is if the user has was able to
         *               login or not.
         */
        void loginFragmentInteraction(Boolean result);
    }

    /**
     * @author Lorenzo Pacis
     * This inner class defines what will happen when the user attempts to login
     * to the application and will create a new AsyncTask that will run a webservice
     * call to see if the user is in the database.
     */
    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "login.php";
        @Override
        protected String doInBackground(String... strings) {
            final String firstArg = strings[1];
            final String secondArg = strings[2];

            if (strings.length != 3) {
                throw new IllegalArgumentException("Two String arguments required.");
            }

            String response = "";
            String url = strings[0];
            String args = "?first_name=" + strings[1] + "&user_pass=" + strings[2];
            response = getCall(url, args);

            //If the user is emailVerified meaning they have logged into firebase
            //and the get call cannot find them in the database then they have
            //recently updated their password. Therefore, make a post call updating
            //the password of this user.
            if(user.isEmailVerified() && response.equals("not found")) {
                Log.d("Yes","sucessful");
                String result = postCall(firstArg, secondArg);
                Log.d("post Result",result);
            }

            response = getCall(url, args);
                return response;
            }

        /**
         * This method when called with mill a post call to the webservice.
         * This functionality is to be used only when a user has recently reset their password
         * so that they can then make a post call that will change their password
         * in the database.
         * @author Lorenzo Pacis
         * @param firstArg The email address.
         * @param secondArg The new password.
         * @return Returns the result of the post call.
         */
        private String postCall(String firstArg, String secondArg) {
            HttpURLConnection urlConnection = null;
            String response = "";
            try {
                //if firebase is able to login the user but they are not in the database, then
                //this means that they have reset the password. So make a new POST call to update them in the database.
                URL urlObject = new URL("http://cssgate.insttech.washington.edu/~pacis93/insertnew.php");
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("first_name", "UTF-8")
                        + "=" + URLEncoder.encode(firstArg, "UTF-8") + "&" + URLEncoder.encode("user_pass", "UTF-8") + "="
                        + URLEncoder.encode(secondArg, "UTF-8") + "&" + URLEncoder.encode("rp", "UTF-8") + "=yes";

                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                Log.d("response from post", response);


            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("ERROR", response);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        /**
         * This method will make a GET call to the database through the given URL and
         * with the string arguments. This is used to see if a user is in the database
         * so that they can log into the application.
         * @param url The url of the webservice.
         * @param args The arguments that follow the url.
         * @return The result of the GET call. Either found or not found.
         */
        private String getCall(String url, String args) {
            String result = "";
            try{
                HttpURLConnection urlConnection = null;
                URL urlObject = new URL(url + SERVICE + args);
                Log.d("URL IN GET", urlObject.toString());
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    result += s;
                }
            } catch (Exception e) {
                result = "Unable to connect, Reason: "
                        + e.getMessage();
            }

            return result;

        }

        /**
         * This method will be called after doInBackground has finished.
         * It will either set the loginUsername edit text to have an error
         * or it will create a new fragment. If the user has not verified
         * their email, it will display a toast notifying them to do
         * so before logging in.
         * @param result This parameter is the result of doInBackground.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.equals("not found")) {
                loginUsername.setError("Check email or password");
            }

            if(result.equals("found")) {
                if(mAuth.getCurrentUser().isEmailVerified()) {
                    mListener.loginFragmentInteraction(true);
                } else {

                    Toast toast = Toast.makeText(getContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG);
                    toast.show();

                }


            }

        }


    }
}

