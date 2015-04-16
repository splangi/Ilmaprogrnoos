/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ee.siimplangi.ilmaprognoos;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ee.siimplangi.ilmaprognoos.data.DataHandler;
import ee.siimplangi.ilmaprognoos.data.ForecastWrapper;
import ee.siimplangi.ilmaprognoos.listeners.DataAvailabilityListener;
import ee.siimplangi.ilmaprognoos.views.WeatherFragment;


public class MainActivity extends ActionBarActivity implements DataAvailabilityListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DataHandler dataHandler;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<String> availableDates;
    private List<ForecastWrapper> forecastData;
    private TextView loadingTextView;
    private ProgressBar loadingProgressBar;
    private Button tryAgainButton;
    private RelativeLayout loadingViewLayout;
    private static final String LAST_SELECTED_ITEM_KEY = "selectedItem";
    private int selectedItem = 0;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        tryAgainButton = (Button) findViewById(R.id.tryAgainButton);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(null);
            }
        });

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        loadingViewLayout = (RelativeLayout) findViewById(R.id.loadingLayout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        loadData(savedInstanceState);
    }


    private void initDrawer(List<ForecastWrapper> forecastData) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        availableDates = new ArrayList<>();
        for (ForecastWrapper forecast : forecastData) {
            availableDates.add(sdf.format(forecast.getForecastDate()));
        }
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, availableDates));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        invalidateOptionsMenu();
        selectItem(selectedItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData(Bundle savedInstanceState) {
        loadingViewLayout.setVisibility(View.VISIBLE);
        loadingTextView.setText(R.string.loading);
        tryAgainButton.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState != null){
            selectedItem = savedInstanceState.getInt(LAST_SELECTED_ITEM_KEY, 0);
        } else {
            selectedItem = 0;
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Locale locale = getResources().getConfiguration().locale;

        dataHandler = new DataHandler(cm, this);
        dataHandler.fetchData(savedInstanceState, locale);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_SELECTED_ITEM_KEY, selectedItem);
        dataHandler.saveData(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDataAvailable() {
        loadingViewLayout.setVisibility(View.INVISIBLE);
        forecastData = dataHandler.getData();
        initDrawer(forecastData);
    }


    @Override
    public void onDataNotAvailable(DataHandler.FailReason failReason) {
        loadingViewLayout.setVisibility(View.VISIBLE);
        loadingTextView.setText(getString(failReason.getStringId()));
        tryAgainButton.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        try{
            selectedItem = position;
            WeatherFragment fragment = new WeatherFragment();
            fragment.setRetainInstance(false);
            FragmentManager fragmentManager = getFragmentManager();
            Bundle args = new Bundle();
            args.putParcelable(WeatherFragment.FORECAST_KEY, forecastData.get(position));
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
            setTitle(availableDates.get(position));
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        } catch (IllegalStateException e){
            Log.d(TAG, "Expected error", e);
            // If we received the XML results but the savedInstanceState was already called.
            // This happened for me when I launched the application from android studio, but the screen was locked

        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}