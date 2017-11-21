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

public class VerifyUserTask extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... strings) {
        String username = strings[0];
        String password =  strings[1];
        postCall(username,password);
        return null;
    }


    private String postCall(String firstArg, String secondArg) {
        HttpURLConnection urlConnection = null;
        String response = "";
        try {
            URL urlObject = new URL("http://cssgate.insttech.washington.edu/~pacis93/verifyUser.php");
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            String data = URLEncoder.encode("first_name", "UTF-8")
                    + "=" + URLEncoder.encode(firstArg, "UTF-8") + "&" + URLEncoder.encode("code", "UTF-8") + "="
                    + URLEncoder.encode("0", "UTF-8");

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

        Log.d("RESPONSE", response);
        return response;
    }
}
