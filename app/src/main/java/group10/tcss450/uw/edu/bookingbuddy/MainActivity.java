package group10.tcss450.uw.edu.bookingbuddy;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FlightSearchFragment.OnSearchSubmitListener, FlightListFragment.OnFragmentInteractionListener,
        splashScreen.splashFragmentInteractionListener, loginScreen.loginFragmentInteractionListener, FlightListFragment.OnGraphInteractionListener,
        registerScreen.registerFragmentInteractionListener, displayScreen.OnFragmentInteractionListener, forgotPassword.forgotPasswordInteractionListener{

    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new splashScreen())
                        .commit();
            }
        }

        //hides the hamburger button for nav menu until after the user has logged in
        toggle.setDrawerIndicatorEnabled(false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_flight_search) {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new FlightSearchFragment()).addToBackStack(null)
                        .commit();
            }
        } else if (id == R.id.nav_load_pinned_flights) {

        } else if (id == R.id.nav_search_history) {

        } else if (id == R.id.logout) {
            if (findViewById(R.id.fragmentContainer) != null)
            {
                toggle.setDrawerIndicatorEnabled(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new splashScreen()).addToBackStack(null)
                        .commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchSubmit(String origin, String destination) {
        FlightListFragment listFrag = new FlightListFragment();
        Bundle args = new Bundle();
        args.putSerializable("ORIGIN", origin);
        args.putSerializable("DESTI", destination);

        listFrag.setArguments(args);
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, listFrag)
                .addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This method will open either a login or register fragment depending
     * on the integer passed into the method.
     * @param selection The selected choice of either login or register.
     */
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

    /**
     * Parameters are currently redundant - TODO: Remove them and update args if needed.
     */
    public void openDisplayScreen() {
        toggle.setDrawerIndicatorEnabled(true);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new FlightSearchFragment())
                .addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void loginFragmentInteraction(Boolean result) {
        if(result) {
            openDisplayScreen();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new forgotPassword())
                    .addToBackStack(null);
            transaction.commit();


        }



    }

    /**
     * After a user has logged in, they will be asked to then register.
     * This will also require the user to verify their email address before they can login.
     * @param username
     * @param password
     */
    @Override
    public void registerFragmentInteraction(String username, String password) {
        //opens the login after you register
        splashFragmentInteraction(0);

    }

    @Override
    public void onGraphSubmit(Integer[] arr) {
        GraphFragment graphFrag = new GraphFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARRAY", arr);
        graphFrag.setArguments(args);
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, graphFrag)
                .addToBackStack(null);
        trans.commit();
    }

    @Override
    public void forgotPasswordInteraction(Uri uri) {

    }
}
