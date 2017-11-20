package group10.tcss450.uw.edu.bookingbuddy.Backend;

import android.os.AsyncTask;
import android.util.Log;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Email.GMailSender;

/**
 * Created by lorenzopacis on 11/20/2017.
 */

public class ForgotPasswordTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings) {
        Log.d(strings[0], "code");
        try {
            GMailSender sender = new GMailSender("BookingBuddyApp@gmail.com", "Lol082344");
            sender.sendMail("Password Reset for BookingBuddy",
                    "Please use the following code to reset your password. " + strings[0],
                    "BookingBuddy",
                    "lorenzopacis@gmail.com");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }


}
