package ee.siimplangi.ilmaprognoos.data;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ee.siimplangi.ilmaprognoos.R;
import ee.siimplangi.ilmaprognoos.listeners.DataAvailabilityListener;
import ee.siimplangi.ilmaprognoos.listeners.XMLDownloadTaskListener;
import ee.siimplangi.ilmaprognoos.listeners.XMLParserTaskListener;
import ee.siimplangi.ilmaprognoos.xml.XmlDownloadTask;
import ee.siimplangi.ilmaprognoos.xml.XmlParserTask;

/**
 * Created by Siim on 13.04.2015.
 */
public class DataHandler implements XMLDownloadTaskListener, XMLParserTaskListener {

    private static final String TAG = "DataHandler";
    private static final String FORECAST_WRAPPER_ARRAY_KEY = "forecastWrapperArrayKey";
    private static final String LAST_FETCH_TIME_KEY = "lastFetchTime";
    private static final long HOUR = 1000*60*60;

    private final DataAvailabilityListener listener;
    private final ConnectivityManager connectivityManager;
    private ArrayList<ForecastWrapper> data;
    private long lastDataFetch;


    public DataHandler(ConnectivityManager cm, DataAvailabilityListener listener){
        data = null;
        this.listener = listener;
        this.connectivityManager = cm;
    }

    public void fetchData(Bundle savedInstance, Locale locale){
        // Possibly a ASync task.
        DataLocale dataLocale = DataLocale.getDataLocale(locale);
        if (savedInstance != null){
            restoreData(savedInstance);
        }
        if (data == null){
            // We failed on restoring the data, lets fetch new.
            downloadData(dataLocale);
        } else {
            listener.onDataAvailable();
        }

    }

    private void downloadData(DataLocale dataLocale){
        if (isNetworkOnline()){
            new XmlDownloadTask(this, dataLocale).execute();
        }
        else{
            listener.onDataNotAvailable(FailReason.NO_CONNECTION);
        }
    }

    private void restoreData(Bundle savedInstanceState) {
        long currentTime = System.currentTimeMillis();
        lastDataFetch = savedInstanceState.getLong(LAST_FETCH_TIME_KEY, 0);
        long timePassed = currentTime - lastDataFetch;
        data = savedInstanceState.getParcelableArrayList(FORECAST_WRAPPER_ARRAY_KEY);
        /* We only try to restore data if
            *the time passed is less than an hour
            *there is no internet connection to fetch data
        */
        if ((timePassed < HOUR && timePassed >= 0) || !isNetworkOnline()) {
            data = savedInstanceState.getParcelableArrayList(FORECAST_WRAPPER_ARRAY_KEY);
            // We dont know yet if the data retrival was successful, it can be null
        }
    }

    public void saveData(Bundle onSaveInstanceState){
        onSaveInstanceState.putLong(LAST_FETCH_TIME_KEY, lastDataFetch);
        onSaveInstanceState.putParcelableArrayList(FORECAST_WRAPPER_ARRAY_KEY, data);
    }

    @Override
    public void onXMLDownloadTaskFinished(XmlDownloadTask task) {
        try {
            XmlPullParser parser = task.get(10, TimeUnit.SECONDS);
            new XmlParserTask(this).execute(parser);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e(TAG, "Exception during XML download", e);
            listener.onDataNotAvailable(FailReason.INTERNAL_ERROR);
        }
    }

    @Override
    public void onXMLParserTaskFinished(XmlParserTask task) {
        try {
            data = task.get(10, TimeUnit.SECONDS);
            lastDataFetch = System.currentTimeMillis();
            listener.onDataAvailable();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e(TAG, "Exception during XML Parsing", e);
            listener.onDataNotAvailable(FailReason.INTERNAL_ERROR);
        }
    }


    private boolean isNetworkOnline() {
        boolean status=false;
        try{
            NetworkInfo netInfo = connectivityManager.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = connectivityManager.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public List<ForecastWrapper> getData() {
        return data;
    }

    public enum FailReason{
        NO_CONNECTION (R.string.no_connectivity), INTERNAL_ERROR (R.string.internal_error);

        private int stringId;

        private FailReason(int stringId){
            this.stringId = stringId;
        }

        public int getStringId() {
            return stringId;
        }
    }
}
