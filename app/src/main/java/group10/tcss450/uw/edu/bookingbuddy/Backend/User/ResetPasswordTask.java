package group10.tcss450.uw.edu.bookingbuddy.Backend.User;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by lorenzopacis on 11/20/2017.
 */

public class ResetPasswordTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
       String username = strings[0];
        String password =  strings[1];
        postCall(username,password);
        return null;
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
}
