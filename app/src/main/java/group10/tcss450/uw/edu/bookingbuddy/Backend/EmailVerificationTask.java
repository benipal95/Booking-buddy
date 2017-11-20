package group10.tcss450.uw.edu.bookingbuddy.Backend;

import android.os.AsyncTask;
import android.util.Log;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Email.GMailSender;

/**
 * Created by lorenzopacis on 11/20/2017.
 */

public class EmailVerificationTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings) {

        try {
            GMailSender sender = new GMailSender("BookingBuddyApp@gmail.com", "Lol082344");
            sender.sendMail("Verification for BookingBuddy",
                    "Please use the following code to verify your email address. " + strings[0],
                    "BookingBuddy",
                    strings[1]);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }

}
