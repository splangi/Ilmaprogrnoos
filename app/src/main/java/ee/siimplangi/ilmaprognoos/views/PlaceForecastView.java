package ee.siimplangi.ilmaprognoos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ee.siimplangi.ilmaprognoos.R;
import ee.siimplangi.ilmaprognoos.data.DayTime;
import ee.siimplangi.ilmaprognoos.data.Phenomenon;
import ee.siimplangi.ilmaprognoos.data.PlaceForecast;

/**
 * Created by Siim on 13.04.2015.
 */
public class PlaceForecastView extends RelativeLayout{

    private TextView placeNameTextView;
    private TextView placeTemperatureTextView;
    private ImageView phenomenonIconImageView;

    public PlaceForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        placeNameTextView = (TextView) findViewById(R.id.placeName);
        placeTemperatureTextView = (TextView) findViewById(R.id.placeTempText);
        phenomenonIconImageView = (ImageView) findViewById(R.id.placePhenomenonIcon);
    }

    public void setData(PlaceForecast forecast){
        setPlaceName(forecast);
        setPlaceTemperatureText(forecast);
        setPhenomenonIcon(forecast);
    }

    private void setPhenomenonIcon(PlaceForecast forecast){
        int iconId = forecast.getPhenomenon().getIconId();
        if (iconId != 0){
            phenomenonIconImageView.setImageDrawable(getResources().getDrawable(iconId));
        }
    }

    private void setPlaceTemperatureText(PlaceForecast forecast){
        int min = forecast.getTempNight();
        int max = forecast.getTempDay();
        if (min > max){
            int a = min;
            min = max;
            max = a;
        }
        String text = min + ".." + max + "\u00B0" + "C";
        placeTemperatureTextView.setText(text);
    }

    private void setPlaceName(PlaceForecast forecast){
        placeNameTextView.setText(forecast.getPlace());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
        if (isInEditMode()){
            editModeInit();
        }
    }

    private void editModeInit(){
        PlaceForecast placeForecast2 = new PlaceForecast(DayTime.DAY);
        placeForecast2.setTempDay(-12);
        placeForecast2.setTempNight(-10);
        placeForecast2.setPhenomenon(Phenomenon.CLEAR);
        placeForecast2.setPlace("Tartu");
        setData(placeForecast2);
    }

}
