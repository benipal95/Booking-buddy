package group10.tcss450.uw.edu.bookingbuddy;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements splashScreen.splashFragmentInteractionListener, loginScreen.loginFragmentInteractionListener,
        registerScreen.registerFragmentInteractionListener, displayScreen.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new splashScreen())
                    .commit();
        }
    }


    @Override
    public void splashFragmentInteraction(int selection) {

        if(selection == 0) {
            loginScreen loginFragment = new loginScreen();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, loginFragment)
                    .addToBackStack(null);
            transaction.commit();

        } else if(selection == 1){
            Log.d("Something","stuff");
            registerScreen registerFragment = new registerScreen();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, registerFragment)
                    .addToBackStack(null);
            transaction.commit();

        }



    }

    public void openDisplayScreen(String username, String password) {

        displayScreen displayFragment = new displayScreen();
        Bundle args = new Bundle();
        args.putSerializable("Username", username);
        args.putSerializable("Password", password);
        displayFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, displayFragment)
                .addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void loginFragmentInteraction(String username, String password) {
        openDisplayScreen(username,password);


    }

    @Override
    public void registerFragmentInteraction(String username, String password) {
        openDisplayScreen(username,password);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
