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
import java.net.URLEncoder;import group10.tcss450.uw.edu.bookingbuddy.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link registerScreen.registerFragmentInteractionListener} interface
 * to handle interaction events.
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
    public registerScreen() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            AsyncTask<String, Void, String> task = null;
            if(registerUsername.getText().toString() != null) {

                if(registerPassword.getText().toString().equals(registerComfirmPasword.getText().toString())) {
                    task = new PostWebServiceTask();
                    task.execute(PARTIAL_URL, registerUsername.getText().toString(), registerPassword.getText().toString());

                } else {
                    registerPassword.setError("Passwords Not Matching");
                }
            }

        }
    }
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
    public interface registerFragmentInteractionListener {
        // TODO: Update argument type and name
        void registerFragmentInteraction(String username, String password);
    }
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
                String data =URLEncoder.encode("first_name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8") +"&" +URLEncoder.encode("user_pass","UTF-8") + "="
                        + URLEncoder.encode(strings[2], "UTF-8");
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
            // Something wrong with the network or the URL.
            Log.d("result", result);
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
                                                Log.d("SUCESS", "EMAIL VERIFICATION");

                                            } else {
                                                Log.e("NOPE", "sendEmailVerification", task.getException());

                                            }
                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("PROBLEM", "signInWithEmail:failure", task.getException());

                                }

                                // ...
                            }
                        });



                mListener.registerFragmentInteraction("PHP MESSAGE", result);

            }
            if (result.equals("username already in database")){
                registerUsername.setError("Username already registered");

            }
        }
    }
}
