package com.example.root.adhkar.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;

import com.example.root.adhkar.R;
import com.example.root.adhkar.fragment.AdhkarAtTimeFragment;
import com.example.root.adhkar.fragment.LastReadFragment;
import com.example.root.adhkar.fragment.NavigationDrawerFragment;
import com.example.root.adhkar.fragment.QuranFragment;
import com.example.root.adhkar.fragment.SettingFragment;
import com.example.root.adhkar.fragment.StaticticsFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    android.app.Fragment fr;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        Log.d("TAG", "onSectionAttached");

        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        boolean firstCall = true;
        if (fr != null) {

            firstCall = false;
        }
        switch (number) {
            case 1:
                mTitle = getString(R.string.statistics);
                fr = new StaticticsFragment();

                break;
            case 2:

                mTitle = getString(R.string.quran);

                fr = new QuranFragment();


                break;
            case 3:

                mTitle = getString(R.string.last_Read);


                fr = new LastReadFragment();
              /*  Intent intent = new Intent(MainActivity.this, com.radaee.reader.PDFViewAct.class);
                intent.putExtra("PDFAsset", "Quran.pdf");
                intent.putExtra("PDFPswd", "");//password
                intent.putExtra("lastread", "55");
                startActivity(intent);*/
                Bundle args = new Bundle();
                args.putString("PDFAsset", "Quran.pdf");
                fr.setArguments(args);

             //   intent.putExtra("PDFPswd", "");//password
               // intent.putExtra("lastread", "55");
                //startActivity(intent);
                break;
            case 4:
                fr = new AdhkarAtTimeFragment();
                mTitle = getString(R.string.adhkar_at_this_time);

                break;
            case 5:
                mTitle = getString(R.string.action_settings);
                fr = new SettingFragment();

                break;

        }
        if (firstCall) {
            fragmentTransaction.add(R.id.container, fr);
        } else {
            fragmentTransaction.replace(R.id.container, fr);

        }

        fragmentTransaction.commit();
    }

    private String loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String pos = sharedPreferences.getString("idsura", "001");
        return pos;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//////////

        /////////

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dilogdescription);
            dialog.setTitle(getResources().getString(R.string.desciption));

            Button dialogButton = (Button) dialog.findViewById(R.id.concel);
            // if button is clicked, close the custom dialog


            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}