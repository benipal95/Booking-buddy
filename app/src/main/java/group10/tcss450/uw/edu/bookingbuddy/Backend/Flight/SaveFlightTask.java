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
import java.util.Random;

/**
 * Created by lorenzopacis on 11/24/2017.
 */

public class SaveFlightTask extends AsyncTask<String, Void, String> {

    private final String SERVICE = "saveFlight.php";


    /**
     * Performs a background task, which will make a POST call to register
     * a new user in the database.
     * @param strings The arguments to be used to create the POST call.
     * @return the result of the POST call.
     */
    @Override
    protected String doInBackground(String... strings) {
        if (strings.length != 6) {
            throw new IllegalArgumentException("Six String arguments required.");
        }
        String userEmail= strings[0];
        String origin = strings[1];
        String dest = strings[2];
        String returnDate = strings[4];
        String deptDate = strings[3];
        String price = strings[5];
        String email = "";
        for(int i = 0; i < userEmail.length(); i++) {
            if(userEmail.charAt(i) != '@' && userEmail.charAt(i) != '.') {
                email+=userEmail.charAt(i);
            }
        }
        Log.d("DEST", dest);

        String response = "";
        HttpURLConnection urlConnection = null;
        String url = strings[0];
        try {
            URL urlObject = new URL("http://cssgate.insttech.washington.edu/~pacis93/saveFlight.php");
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                    +"&" + URLEncoder.encode("origin","UTF-8") + "=" + URLEncoder.encode(origin, "UTF-8")
                    +"&" + URLEncoder.encode("destination", "UTF-8") + "=" +  URLEncoder.encode(dest,"UTF-8")
                    +"&" + URLEncoder.encode("dept_date","UTF-8") + "=" + URLEncoder.encode(deptDate, "UTF-8")
                    +"&" + URLEncoder.encode("return_date","UTF-8") + "=" + URLEncoder.encode(returnDate, "UTF-8")
                    +"&" + URLEncoder.encode("price","UTF-8") + "=" + URLEncoder.encode(price, "UTF-8");
            Log.d("DATA", data);
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
        Log.d("result", result);

    }
}
