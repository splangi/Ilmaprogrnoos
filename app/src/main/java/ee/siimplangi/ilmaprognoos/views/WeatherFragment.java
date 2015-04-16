package ee.siimplangi.ilmaprognoos.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ee.siimplangi.ilmaprognoos.R;
import ee.siimplangi.ilmaprognoos.data.ForecastWrapper;
import ee.siimplangi.ilmaprognoos.data.PlaceForecast;

/**
 * Created by Siim on 30.03.2015.
 */
public class WeatherFragment extends Fragment {

    public static final String FORECAST_KEY = "forecast";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ForecastWrapper forecast = (ForecastWrapper) savedInstanceState.getSerializable(FORECAST_KEY);


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather, container, false);

        ForecastWrapper forecastWrapper;

        Bundle args = getArguments();
        forecastWrapper = args.getParcelable(FORECAST_KEY);

        ForecastView dayForecastView = (ForecastView) rootView.findViewById(R.id.dayForecast);
        ForecastView nightForecastView = (ForecastView) rootView.findViewById(R.id.nightForecast);
        LinearLayout placesForecastContentWindow = (LinearLayout) rootView.findViewById(R.id.placesForecastContentWindow);

        dayForecastView.setData(forecastWrapper.getForecastDay());
        nightForecastView.setData(forecastWrapper.getForecastNight());

        for (PlaceForecast placeForecast : forecastWrapper.getPlaceForecasts()) {
            PlaceForecastView placeForecastView = (PlaceForecastView) inflater.inflate(R.layout.place_forecast, placesForecastContentWindow, false);
            placeForecastView.setData(placeForecast);
            placesForecastContentWindow.addView(placeForecastView);
        }

        return rootView;
    }


}

