package ee.siimplangi.ilmaprognoos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ee.siimplangi.ilmaprognoos.utils.IntervalSentence;
import ee.siimplangi.ilmaprognoos.R;
import ee.siimplangi.ilmaprognoos.data.DayTime;
import ee.siimplangi.ilmaprognoos.data.Forecast;
import ee.siimplangi.ilmaprognoos.data.Phenomenon;

/**
 * Created by Siim on 10.04.2015.
 */
public class ForecastView extends RelativeLayout{

    private static final String TAG = "ForecastView";
    private TextView tempNumberTextView;
    private TextView tempWordTextView;
    private TextView dayTimeTextView;
    private TextView windSpeedTextView;
    private TextView weatherDescriptionTextView;
    private ImageView logoWindSpeed;
    private ImageView forecastIcon;

    public ForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        logoWindSpeed = (ImageView) findViewById(R.id.logoWind);
        tempNumberTextView = (TextView) findViewById(R.id.tempTextView);
        tempWordTextView = (TextView) findViewById(R.id.tempSentenceTextView);
        dayTimeTextView = (TextView) findViewById(R.id.dayTimeTextView);
        windSpeedTextView = (TextView) findViewById(R.id.windSpeedTextView);
        weatherDescriptionTextView = (TextView) findViewById(R.id.weatherDescriptionTextView);
        forecastIcon = (ImageView) findViewById(R.id.forecastIcon);
    }

    public void setData(Forecast forecast){
        setBackGround(forecast);
        setTextTemperatureNumber(forecast);
        setTextTemperatureSentence(forecast);
        setDayTimeText(forecast);
        setWindSpeedText(forecast);
        setWeatherDescription(forecast);
    }

    private void setBackGround(Forecast forecast){
        int backgroundId = 0;
        switch(forecast.getDayTime()){

            case DAY:
                backgroundId = forecast.getPhenomenon().getDayLargeIconId();
                break;
            case NIGHT:
                backgroundId = forecast.getPhenomenon().getNightLargeIconId();
                break;
            default:
                Log.e(TAG, "dayTime was not set!");
        }
        if (backgroundId != 0){
            forecastIcon.setImageDrawable(getResources().getDrawable(backgroundId));
        }
    }

    private void setWeatherDescription(final Forecast forecast){
        weatherDescriptionTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getResources().getString(forecast.getPhenomenon().getStringId());
                String text = forecast.getText();
                new WeatherDescriptionDialog(getContext(), title, text, R.style.dialogTheme);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
        if (isInEditMode()){
            editModeInit();
        }
    }

    private void setTextTemperatureNumber(Forecast forecast){
        int tempMax = forecast.getTempMax();
        int tempMin = forecast.getTempMin();
        String tempText = tempMin + ".." + tempMax + "\u00B0" + "C";
        tempNumberTextView.setText(tempText);
    }

    private void setTextTemperatureSentence(Forecast forecast){
        int tempMax = forecast.getTempMax();
        int tempMin = forecast.getTempMin();
        IntervalSentence sentence = new IntervalSentence(tempMin, tempMax, getResources());
        tempWordTextView.setText(sentence.toString());
    }

    private void setDayTimeText(Forecast forecast){
        String text = "";
        switch (forecast.getDayTime()){
            case DAY:
                text = getResources().getString(R.string.day);
                break;
            case NIGHT:
                text = getResources().getString(R.string.night);
                break;
        }
        dayTimeTextView.setText(text.toUpperCase());
    }

    private void setWindSpeedText(Forecast forecast){
        int speedMin = forecast.getWindSpeedMin();
        int speedMax = forecast.getWindSpeedMax();
        if (speedMin == -1 || speedMax == -1){
            windSpeedTextView.setVisibility(INVISIBLE);
            logoWindSpeed.setVisibility(INVISIBLE);
        }
        else{
            windSpeedTextView.setVisibility(VISIBLE);
            logoWindSpeed.setVisibility(VISIBLE);
            String text = speedMin + ".." + speedMax + "ms";
            windSpeedTextView.setText(text);
        }

    }

    private void editModeInit(){
        Forecast dayForecast = new Forecast(DayTime.DAY);
        dayForecast.setTempMin(3);
        dayForecast.setTempMax(21);
        dayForecast.setWindSpeedMax(12);
        dayForecast.setWindSpeedMin(10);
        dayForecast.setText("dummyText");
        dayForecast.setName("something");
        dayForecast.setPhenomenon(Phenomenon.CLEAR);
        setData(dayForecast);
    }


}
