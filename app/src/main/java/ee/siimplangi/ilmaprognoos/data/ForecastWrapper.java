package ee.siimplangi.ilmaprognoos.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Siim on 8.04.2015.
 */
public class ForecastWrapper implements Parcelable{

    private Forecast forecastNight;
    private Forecast forecastDay;
    private List<PlaceForecast> placeForecasts;
    private Date forecastDate;
    private DataLocale dataLocale;

    public static final Creator<ForecastWrapper> CREATOR = new Creator<ForecastWrapper>(){

        @Override
        public ForecastWrapper createFromParcel(Parcel parcel) {
            ForecastWrapper forecastWrapper = new ForecastWrapper();
            forecastWrapper.setForecastDay((Forecast) parcel.readParcelable(null));
            forecastWrapper.setForecastNight((Forecast) parcel.readParcelable(null));
            forecastWrapper.setPlaceForecasts(parcel.readArrayList(null));
            forecastWrapper.setForecastDate(new Date(parcel.readLong()));
            forecastWrapper.setDataLocale((DataLocale) parcel.readSerializable());
            return forecastWrapper;
        }

        @Override
        public ForecastWrapper[] newArray(int size) {
            return new ForecastWrapper[size];
        }
    };

    public ForecastWrapper (){
    }

    public ForecastWrapper(Date forecastDate){
        this.forecastDate = forecastDate;
        placeForecasts = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(forecastDay, 0);
        parcel.writeParcelable(forecastNight, 0);
        parcel.writeList(placeForecasts);
        parcel.writeLong(forecastDate.getTime());
        parcel.writeSerializable(dataLocale);

    }

    public List<PlaceForecast> getPlaceForecasts() {
        return placeForecasts;
    }

    public void setPlaceForecasts(List<PlaceForecast> placeForecasts) {
        this.placeForecasts = placeForecasts;
    }

    public Forecast getForecastNight() {
        return forecastNight;
    }

    public void setForecastNight(Forecast forecastNight) {
        this.forecastNight = forecastNight;
    }

    public Forecast getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(Forecast forecastDay) {
        this.forecastDay = forecastDay;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public DataLocale getDataLocale() {
        return dataLocale;
    }

    public void setDataLocale(DataLocale dataLocale) {
        this.dataLocale = dataLocale;
    }

}
