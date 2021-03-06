package group10.tcss450.uw.edu.bookingbuddy.Frontend.MainUI;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import group10.tcss450.uw.edu.bookingbuddy.Backend.Flight.GetSavedFlightsTask;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightListFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.FlightSearchFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.FlightResults.GraphFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.Login.LoginFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.Register.RegisterFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.Login.VerifyEmailFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.PasswordReset.EnterNewPasswordFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.PasswordReset.ForgotPasswordFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.PasswordReset.ResetPasswordFragment;
import group10.tcss450.uw.edu.bookingbuddy.Frontend.SavedFlights.SavedFlightsFragment;
import group10.tcss450.uw.edu.bookingbuddy.R;

/**
 * The main activity of the application. Currently housing ALL fragments.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FlightSearchFragment.OnSearchSubmitListener, FlightListFragment.OnFragmentInteractionListener,
        StartupFragment.splashFragmentInteractionListener, LoginFragment.loginFragmentInteractionListener, FlightListFragment.OnGraphInteractionListener,
        RegisterFragment.registerFragmentInteractionListener, ForgotPasswordFragment.forgotPasswordInteractionListener, ResetPasswordFragment.resetFragmentInteractionListener,EnterNewPasswordFragment.EnterNewPasswordFragmentInteractionListener,
        VerifyEmailFragment.VerifyEmailFragmentInteractionListener, SavedFlightsFragment.SavedFlightsFragmentListener{

    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawer;
    private String userEmail;
    private SharedPreferences mPrefs;

    /**
     * This method will be called when the activity is created. It will instantiate and create
     * all necessary UI elements as well as open a new StartupFragment fragment that the user will see
     * when the application is started.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (mPrefs.getInt(getString(R.string.LOGIN_STATUS), 0) == 1) {
            if (savedInstanceState == null) {
                if (findViewById(R.id.fragmentContainer) != null) {
                    openDisplayScreen();
                    userEmail = mPrefs.getString(getString(R.string.USER_EMAIL), "");
                    Log.d("EMAIL", "mPrefs.email="+userEmail);
                    mToggle.setDrawerIndicatorEnabled(true);
                    if(mToggle.isDrawerIndicatorEnabled())
                    {
                        Log.d("DRAWER_STATUS", "true");
                    }
                    else
                        Log.d("DRAWER_STATUS", "false");

                }
            }
        }
        else
        {
            if (savedInstanceState == null) {
                if (findViewById(R.id.fragmentContainer) != null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragmentContainer, new StartupFragment())
                            .commit();
                }
            }
            //hides the hamburger button for nav menu until after the user has logged in
            mToggle.setDrawerIndicatorEnabled(false);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        View v  = findViewById(R.id.fragmentContainer);
        //v.setBackgroundColor(Color.rgb(30,144,255));

    }

    /**
     * Defines the behavior when the back button is pressed for the drawer.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        //Gets the current fragment
        Fragment f =  getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Log.d("VISIBLE",f.getClass().toString());

        //checks if it is of the type we want to do something with
        if (f instanceof FlightSearchFragment) {
                mToggle.setDrawerIndicatorEnabled(false);
                //clearBackStack();
                super.onBackPressed();
        } else {
            super.onBackPressed();
        }
        //super.onBackPressed();
        //add code
    }

    /**
     * Inflates the menu for when the toolbar menu is created.
     * @param menu The menu.
     * @return Returns true that the menu was inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Defines behavior for when an item is selected.
     * @param item The selected item.
     * @return If the item was found in the selection.
     */
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

    /**
     * Handles the behavior for when a navigation item is selected by the user.
     * @param item The item selected.
     * @return returns true for all current implementations of any item pressed.
     */
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

            if (findViewById(R.id.fragmentContainer) != null)
            {
                SavedFlightsFragment savedFlights = new SavedFlightsFragment();
                Bundle args = new Bundle();
                args.putSerializable("email", userEmail);
                savedFlights.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, savedFlights).addToBackStack(null)
                        .commit();
            }
        } else if (id == R.id.nav_search_history) {

        } else if (id == R.id.logout) {

            if (findViewById(R.id.fragmentContainer) != null)
            {
                saveToSharedPrefs(0);
                saveToSharedPrefs("");
                mToggle.setDrawerIndicatorEnabled(false);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new StartupFragment())//.addToBackStack(null)
                        .commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Clears the backstack of fragments when a user has logged out.
     */
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    /**
     * Defines the behavior for when the search button is pressed.
     * @param origin
     * @param destination
     */
    @Override
    public void onSearchSubmit(String origin, String destination) {

        FlightListFragment listFrag = new FlightListFragment();
        RadioButton fare = findViewById(R.id.rb_fare_sort);
        RadioButton date = findViewById(R.id.rb_date_sort);
        int sorting = 0;

        /*
         * In order to determine the sorting method, you'll need to do checks on
         * which radio button got checked.
         */
        if(fare.isChecked())
            sorting = 0;
        else if(date.isChecked())
            sorting = 1;
        //int sorting = sortingGroup.getCheckedRadioButtonId();
        String departureDate, returnDate;
        Button depart_button = findViewById(R.id.button_depart_date);
        Button return_button = findViewById(R.id.button_return_date);
        departureDate = depart_button.getText().toString();
        returnDate = return_button.getText().toString();
        if(!departureDate.startsWith("Pick")) {
            departureDate = departureDate.substring(11, 18);
            returnDate = returnDate.substring(8, 15);
        }

        Bundle args = new Bundle();
        args.putSerializable("ORIGIN", origin);
        args.putSerializable("DESTI", destination);
        args.putSerializable("SORT", sorting);
        args.putSerializable("DEPART", departureDate);
        args.putSerializable("RETURN", returnDate);
        args.putSerializable("email", userEmail);

        listFrag.setArguments(args);
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, listFrag)
                .addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onSearchSubmit(String origin, String destination, String airlineFilter) {
        FlightListFragment listFrag = new FlightListFragment();
        RadioButton fare = findViewById(R.id.rb_fare_sort);
        RadioButton date = findViewById(R.id.rb_date_sort);
        int sorting = 0;

        /*
         * In order to determine the sorting method, you'll need to do checks on
         * which radio button got checked.
         */
        if(fare.isChecked())
            sorting = 0;
        else if(date.isChecked())
            sorting = 1;
        //int sorting = sortingGroup.getCheckedRadioButtonId();
        String departureDate, returnDate;
        Button depart_button = findViewById(R.id.button_depart_date);
        Button return_button = findViewById(R.id.button_return_date);
        departureDate = depart_button.getText().toString();
        returnDate = return_button.getText().toString();
        if(!departureDate.startsWith("Pick")) {
            departureDate = departureDate.substring(11, 18);
            Log.d("SEARCH_SUBMIT.DEPART", departureDate);
            returnDate = returnDate.substring(8, 15);
            Log.d("SEARCH_SUBMIT.RETURN", returnDate);
        }

        Bundle args = new Bundle();
        args.putSerializable("ORIGIN", origin);
        args.putSerializable("DESTI", destination);
        args.putSerializable("SORT", sorting);
        args.putSerializable("DEPART", departureDate);
        args.putSerializable("RETURN", returnDate);
        args.putSerializable("FILTER", airlineFilter);
        args.putSerializable("email", userEmail);

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
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, loginFragment)
                    .addToBackStack(null);
            transaction.commit();

        } else if(selection == 1){
            Log.d("Something","stuff");
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, registerFragment)
                    .addToBackStack(null);
            transaction.commit();

        }



    }

    /**
     * Parameters are currently redundant - TODO: Remove them and update args if needed.
     * This method is to be called by login and register fragment interactions
     * it will create a new display screen.
     */
    public void openDisplayScreen() {
        saveToSharedPrefs(1);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new FlightSearchFragment());
                //.addToBackStack(null);
        transaction.commit();

    }

    private void saveToSharedPrefs(int theStatus)
    {
        mPrefs.edit().putInt(getString(R.string.LOGIN_STATUS), theStatus).apply();
    }

    private void saveToSharedPrefs(String theEmail)
    {
        mPrefs.edit().putString(getString(R.string.USER_EMAIL), theEmail).apply();
    }

    @Override
    public void loginFragmentInteraction(Boolean loggedIn, Boolean verified, String verificationCode, String email) {
        userEmail = email;
        if(loggedIn && verified) {
            saveToSharedPrefs(userEmail);
            openDisplayScreen();
        } else if(!loggedIn && !verified) {

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                    .addToBackStack(null);
            transaction.commit();


        } else if(loggedIn &&!verified) {
            VerifyEmailFragment verifyFrag = new VerifyEmailFragment();
            Bundle args = new Bundle();
            args.putSerializable("code", verificationCode);
            args.putSerializable("email", email);
            verifyFrag.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, verifyFrag)
                    .addToBackStack(null);
            transaction.commit();
        }



    }

    /**
     * After a user has logged in, they will be asked to then register.
     * This will also require the user to verify their email address before they can login.
     */
    @Override
    public void registerFragmentInteraction(String verificationCode, String email) {
        VerifyEmailFragment verifyFrag = new VerifyEmailFragment();
        Bundle args = new Bundle();
        args.putSerializable("email", email);
        args.putSerializable("code", verificationCode);
        verifyFrag.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, verifyFrag)
                .addToBackStack(null);
        transaction.commit();

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
    public void forgotPasswordInteraction(String resetCode, String email) {
        ResetPasswordFragment resetFrag = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putSerializable("Code", resetCode);
        args.putSerializable("Email", email);
        resetFrag.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, resetFrag)
                .addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void resetFragmentInteraction(String email) {
        EnterNewPasswordFragment enterNewPassFrag = new EnterNewPasswordFragment();
        Bundle args = new Bundle();
        args.putSerializable("Email", email);
        enterNewPassFrag.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer,enterNewPassFrag )
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void EnterNewPasswordFragmentInteraction(String email) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable("Email", email);
        loginFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, loginFragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void VerifyEmailFragmentInteraction() {
        openDisplayScreen();
    }


    @Override
    public void SavedFlightsFragmentInteraction(Uri uri) {

    }
}
