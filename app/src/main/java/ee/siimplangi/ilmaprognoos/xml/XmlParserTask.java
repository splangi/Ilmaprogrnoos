package ee.siimplangi.ilmaprognoos.xml;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ee.siimplangi.ilmaprognoos.data.Forecast;
import ee.siimplangi.ilmaprognoos.data.ForecastWrapper;
import ee.siimplangi.ilmaprognoos.data.DayTime;
import ee.siimplangi.ilmaprognoos.data.Phenomenon;
import ee.siimplangi.ilmaprognoos.data.PlaceForecast;
import ee.siimplangi.ilmaprognoos.listeners.XMLParserTaskListener;

/**
 * Created by Siim on 8.04.2015.
 */
public class XmlParserTask extends AsyncTask<XmlPullParser, Void, ArrayList<ForecastWrapper>>{

    private static final String ns = null;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private XMLParserTaskListener listener;

    public XmlParserTask(XMLParserTaskListener listener) {
        this.listener = listener;
    }

    private static final String LOG_TAG = "XmlParser";

    @Override
    protected ArrayList<ForecastWrapper> doInBackground(XmlPullParser... xmlPullParsers) {
        XmlPullParser parser = xmlPullParsers[0];
        ArrayList<ForecastWrapper> forecastWrappers = null;
        try {
            forecastWrappers = parseXml(parser);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failure furing parsing", e);
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG, "Failure during parsing. Most likely not required TAG", e);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Failure during parsing the date in forecast TAG", e);
        }
        return forecastWrappers;
    }

    @Override
    protected void onPostExecute(ArrayList<ForecastWrapper> forecastWrappers) {
        super.onPostExecute(forecastWrappers);
        listener.onXMLParserTaskFinished(this);
    }

    public static ArrayList<ForecastWrapper> parseXml(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        ArrayList<ForecastWrapper> forecasts = new ArrayList<>();
        parser.next();
        parser.require(XmlPullParser.START_TAG, ns, XmlTag.FORECASTS.toString());
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(XmlTag.FORECAST.toString())) {
                forecasts.add(parseForecastWrapper(parser));
            } else {
                skip(parser);
            }
        }
        return forecasts;
    }

    private static ForecastWrapper parseForecastWrapper(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException{
        parser.require(XmlPullParser.START_TAG, ns, XmlTag.FORECAST.toString());
        Date forecastDate = sdf.parse(parser.getAttributeValue(0));
        ForecastWrapper forecastWrapper = new ForecastWrapper(forecastDate);
        List<PlaceForecast> placeForecasts = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            XmlTag tag = XmlTag.getXmlTag(parser.getName());

            switch (tag) {
                case NIGHT:
                    forecastWrapper.setForecastNight(parseForecast(placeForecasts, parser, XmlTag.NIGHT));
                    break;
                case DAY:
                    forecastWrapper.setForecastDay(parseForecast(placeForecasts, parser, XmlTag.DAY));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        placeForecasts = mergePlaceForecasts(placeForecasts);
        forecastWrapper.setPlaceForecasts(placeForecasts);
        return forecastWrapper;
    }

    private static Forecast parseForecast(List<PlaceForecast> placeForecasts, XmlPullParser parser, XmlTag dayTimeTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, dayTimeTag.toString());
        Forecast forecast;
        if (dayTimeTag.equals(XmlTag.DAY)){
            forecast = new Forecast(DayTime.DAY);
        }
        else{
            forecast = new Forecast(DayTime.NIGHT);
        }
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            XmlTag tag = XmlTag.getXmlTag(parser.getName());

            switch (tag) {

                case PHENOMENON:
                    Phenomenon phenomenon = Phenomenon.getPhenomenon(read(parser, XmlTag.PHENOMENON));
                    forecast.setPhenomenon(phenomenon);
                    break;
                case TEMP_MIN:
                    forecast.setTempMin(readInt(parser, XmlTag.TEMP_MIN));
                    break;
                case TEMP_MAX:
                    forecast.setTempMax(readInt(parser, XmlTag.TEMP_MAX));
                    break;
                case TEXT:
                    forecast.setText(read(parser, XmlTag.TEXT));
                    break;
                case WIND:
                    readWindTag(parser, forecast);
                    break;
                case PLACE:
                    placeForecasts.add(readPlaceForecast(parser, dayTimeTag));
                    break;
                default:
                    skip(parser);
                    break;

            }
        }
        return forecast;
    }

    private static void readWindTag(XmlPullParser parser, Forecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, XmlTag.WIND.toString());
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            XmlTag tag = XmlTag.getXmlTag(parser.getName());

            switch (tag) {

                case SPEED_MIN:
                    forecast.setMinWindSpeedIfLower(readInt(parser, XmlTag.SPEED_MIN));
                    break;
                case SPEED_MAX:
                    forecast.setMaxWindSpeedIfHigher(readInt(parser, XmlTag.SPEED_MAX));
                    break;
                default:
                    skip(parser);
                    break;

            }
        }
    }

    private static PlaceForecast readPlaceForecast(XmlPullParser parser, XmlTag dayTimeTag) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, XmlTag.PLACE.toString());
        PlaceForecast placeForecast;
        if (dayTimeTag.equals(XmlTag.DAY)){
            placeForecast = new PlaceForecast(DayTime.DAY);
        }
        else{
            placeForecast = new PlaceForecast(DayTime.NIGHT);
        }
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            XmlTag tag = XmlTag.getXmlTag(parser.getName());

            switch (tag) {

                case PHENOMENON:
                    Phenomenon phenomenon = Phenomenon.getPhenomenon(read(parser, XmlTag.PHENOMENON));
                    placeForecast.setPhenomenon(phenomenon);
                    break;
                case NAME:
                    placeForecast.setPlace(read(parser, XmlTag.NAME));
                    break;
                case TEMP_MAX:
                    placeForecast.setTempDay(readInt(parser, XmlTag.TEMP_MAX));
                    break;
                case TEMP_MIN:
                    placeForecast.setTempNight(readInt(parser, XmlTag.TEMP_MIN));
                    break;

                default:
                    skip(parser);
                    break;

            }
        }
        return placeForecast;
    }

    private static List<PlaceForecast> mergePlaceForecasts(List<PlaceForecast> placeForecastList){
        List<PlaceForecast> newPlaceForecastList = new ArrayList<>();
        for (PlaceForecast placeForecast : placeForecastList){
            int index = newPlaceForecastList.indexOf(placeForecast);
            if (index == -1){
                newPlaceForecastList.add(placeForecast);
            }
            else{
                PlaceForecast mergedPlaceForecast = newPlaceForecastList.get(index).merge(placeForecast);
                newPlaceForecastList.set(index, mergedPlaceForecast);
            }
        }
        return newPlaceForecastList;
    }

    private static int readInt(XmlPullParser parser, XmlTag tag) throws IOException, XmlPullParserException{
        String strResult = read(parser, tag);
        int result = Integer.parseInt(strResult);
        return result;
    }

    private static String read(XmlPullParser parser, XmlTag tag) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, tag.toString());
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag.toString());
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
