package group10.tcss450.uw.edu.bookingbuddy;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;import group10.tcss450.uw.edu.bookingbuddy.R;


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

    private Button loginButton;

    public loginScreen() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        AsyncTask<String, Void, String> task = null;
        if (mListener != null) {
            task = new GetWebServiceTask();

        }
        task.execute(PARTIAL_URL, loginUsername.getText().toString(), loginPassword.getText().toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_screen, container, false);
        loginUsername = (EditText) v.findViewById(R.id.loginUsername);
        loginPassword = (EditText) v.findViewById(R.id.loginPassword);
        loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

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
        void loginFragmentInteraction(String username, String password);
    }

    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "login.php";
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?first_name="+ strings[1] + "&user_pass=" + strings[2];
            try {
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
                loginUsername.setError("Unable to login");

            }
            Log.d(result,"result");
            if(result.equals("found")) {
                mListener.loginFragmentInteraction("PHP MESSAGE", result);
            }


        }
    }
}
