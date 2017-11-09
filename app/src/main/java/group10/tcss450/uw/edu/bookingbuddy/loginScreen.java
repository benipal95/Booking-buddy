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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link loginScreen.loginFragmentInteractionListener} interface
 * to handle interaction events.
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
    public loginScreen() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {

        mAuth = FirebaseAuth.getInstance();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        AsyncTask<String, Void, String> task = null;
        if (mListener != null) {
            if(v.equals(loginButton)) {
                task = new GetWebServiceTask();
                task.execute(PARTIAL_URL, loginUsername.getText().toString().toLowerCase(), loginPassword.getText().toString());
            } else if(v.equals(forgotPasswordButton)) {
                mListener.loginFragmentInteraction(false);

            }

        }

    }
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
    public interface loginFragmentInteractionListener {
        // TODO: Update argument type and name
        void loginFragmentInteraction(Boolean result);
    }

    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "login.php";
        private boolean loggedInFirebase = false;
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Two String arguments required.");
            }

            mAuth.signInWithEmailAndPassword(strings[1], strings[2])
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SUCCESS", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()) {

                                } else {
                                    Toast toast = Toast.makeText(getContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                            } else {
                                Log.d("FAILURE", "signinWithEmail");
                            }
                        }
                    });
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?first_name="+ strings[1] + "&user_pass=" + strings[2];
            try {
                if(loggedInFirebase) {
                    URL urlObject = new URL(url + SERVICE);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    String data = URLEncoder.encode("first_name", "UTF-8")
                            + "=" + URLEncoder.encode(strings[1], "UTF-8") +"&" + URLEncoder.encode("user_pass","UTF-8") + "="
                            + URLEncoder.encode(strings[2], "UTF-8")+ "&" + URLEncoder.encode("rp", "UTF-8") + "=1";
                    Log.d(data, "DATA ");
                    wr.write(data);
                    wr.flush();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                }

                URL urlObject = new URL(url + SERVICE + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
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
            // Something wrong with the network or the URL.
            if (result.equals("not found")) {
                loginUsername.setError("Check email or password");
            }

            if(result.equals("found")) {

                mListener.loginFragmentInteraction(true);

                 }

            }


        }
    }
