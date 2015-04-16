package ee.siimplangi.ilmaprognoos.xml;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ee.siimplangi.ilmaprognoos.data.DataLocale;
import ee.siimplangi.ilmaprognoos.listeners.XMLDownloadTaskListener;

/**
 * Created by Siim on 8.04.2015.
 */
public class XmlDownloadTask extends AsyncTask<Void, Void, XmlPullParser> {

    private static final String TAG = "XmlDownloader";

    private static XmlPullParserFactory factory;

    private DataLocale dataLocale;

    private XMLDownloadTaskListener listener;

    public XmlDownloadTask(XMLDownloadTaskListener listener, DataLocale dataLocale) {
        this.listener = listener;
        this.dataLocale = dataLocale;
    }

    @Override
    protected XmlPullParser doInBackground(Void... voids) {
        HttpURLConnection connection;
        InputStream xmlInputStream = null;
        try{
            URL url = new URL(dataLocale.getDataUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                xmlInputStream = connection.getInputStream();
            }
            return createNewParser(xmlInputStream);
        } catch(IOException e){
            Log.e(TAG, "Connection error", e);
            return null;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Parse creation error", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(XmlPullParser xmlPullParser) {
        super.onPostExecute(xmlPullParser);
        listener.onXMLDownloadTaskFinished(this);
    }

    private XmlPullParser createNewParser(InputStream stream) throws XmlPullParserException {
        if (factory == null){
            factory = XmlPullParserFactory.newInstance();
        }
        XmlPullParser parser =  factory.newPullParser();
        parser.setInput(stream, null);
        return parser;
    }
}
