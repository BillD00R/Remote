package lulz.just.remote;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements View.OnTouchListener {

    private static final int PAGES_COUNT = 2;
    SectionsPagerAdapter mPAdapter;
    ViewPager mViewPager;
    private SharedPreferences mPrefs;
    private MenuItem connItem;

    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                | (addrBytes[0] & 0xff);
        return addr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mPAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPAdapter);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mPAdapter.getCount(); i++) {

            actionBar.addTab(actionBar.newTab()
                    .setText(mPAdapter.getPageTitle(i))
                    .setTabListener(new TabListener() {

                        public void onTabSelected(ActionBar.Tab tab,
                                                  FragmentTransaction fragmentTransaction) {
                            // When the given tab is selected, switch to the corresponding page in
                            // the ViewPager.
                            mViewPager.setCurrentItem(tab.getPosition());
                        }

                        public void onTabUnselected(ActionBar.Tab tab,
                                                    FragmentTransaction fragmentTransaction) {
                        }

                        public void onTabReselected(ActionBar.Tab tab,
                                                    FragmentTransaction fragmentTransaction) {
                        }
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setIntent(new Intent(this, SettingsActivity.class));
        connItem = menu.findItem(R.id.action_connect);
        connect();
        //prepare();
        return true;
    }

    private void connect() {

        Connection connection = new Connection(this) {
            @Override
            protected void returnTaskResult(String result) {
                connItem.setChecked(true);
            }
        };
        connection.sendString("ping");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(item.getIntent());
                return true;
            case R.id.action_connect:
                connect();
                return true;
            case R.id.action_run_xbmc: {
                Connection connection = new Connection(this);
                connection.sendString("run_xbmc");
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onButtonPlay_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("play");
    }

    public void onButtonStop_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("stop");
    }

    public void onButtonNext_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("next");
    }

    public void onButtonPrev_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("next");
    }

    public void onButtonVolUp_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("vol_up");
    }

    public void onButtonVolDn_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("vol_dn");
    }

    public void onXbmcRight_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("right");
    }

    public void onXbmcLeft_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("left");
    }

    public void onXbmcUp_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("up");
    }

    public void onXbmcDown_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("down");
    }

    public void onXbmcEnter_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("enter");
    }

    public void onXbmcEscape_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("escape");
    }

    public void onXbmcTab_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("tab");
    }

    public void onXbmcSpace_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("space");
    }

    public void onXbmcVolUp_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("xbmc_volUp");
    }

    public void onXbmcVolDn_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("xbmc_volDn");
    }

    public void onXbmcMute_Click(View v) {
        Connection connection = new Connection(this);
        connection.sendString("xbmc_mute");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            Connection connection = new Connection(this);
            connection.sendString("xbmc_volDn");
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            Connection connection = new Connection(this);
            connection.sendString("xbmc_volUp");
        }
        return true;
    }

    public static class SectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public SectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (section) {
                case 0:
                    View rootView1 = inflater.inflate(R.layout.fragment_main_player,
                            container, false);

                    return rootView1;
                case 1:
                    View rootView2 = inflater.inflate(R.layout.fragment_main_xbmc,
                            container, false);
                    return rootView2;

            }
            return null;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(SectionFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_play).toUpperCase(l);
                case 1:
                    return getString(R.string.title_keys).toUpperCase(l);
            }
            return null;
        }
    }
}
