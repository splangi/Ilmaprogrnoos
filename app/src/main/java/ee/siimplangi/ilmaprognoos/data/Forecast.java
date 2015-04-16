package ee.siimplangi.ilmaprognoos.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Siim on 8.04.2015.
 */
public class Forecast implements Parcelable{

    private DayTime dayTime;
    private Phenomenon phenomenon;
    private int tempMin;
    private int tempMax;
    private String text;
    private String name;
    private int windSpeedMin = -1;
    private int windSpeedMax = -1;

    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {

        public Forecast createFromParcel(Parcel in) {
            Forecast forecast = new Forecast((DayTime) in.readSerializable());
            forecast.setPhenomenon((Phenomenon) in.readSerializable());
            forecast.setTempMax(in.readInt());
            forecast.setTempMin(in.readInt());
            forecast.setWindSpeedMax(in.readInt());
            forecast.setWindSpeedMin(in.readInt());
            forecast.setText(in.readString());
            forecast.setName(in.readString());
            return forecast;
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(dayTime);
        parcel.writeSerializable(phenomenon);
        parcel.writeInt(tempMax);
        parcel.writeInt(tempMin);
        parcel.writeInt(windSpeedMax);
        parcel.writeInt(windSpeedMin);
        parcel.writeString(text);
        parcel.writeString(name);
    }

    public void setMinWindSpeedIfLower(int speed){
        if (windSpeedMin == -1 || windSpeedMin>speed){
            windSpeedMin = speed;
        }
    }

    public void setMaxWindSpeedIfHigher(int speed){
        if (windSpeedMax == -1 || windSpeedMax < speed){
            windSpeedMax = speed;
        }
    }

    public Forecast(DayTime dayTime) {
        this.dayTime = dayTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWindSpeedMin() {
        return windSpeedMin;
    }

    public void setWindSpeedMin(int windSpeedMin) {
        this.windSpeedMin = windSpeedMin;
    }

    public int getWindSpeedMax() {
        return windSpeedMax;
    }

    public void setWindSpeedMax(int windSpeedMax) {
        this.windSpeedMax = windSpeedMax;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = phenomenon;
    }

    public DayTime getDayTime() {
        return dayTime;
    }

    public void setDayTime(DayTime dayTime) {
        this.dayTime = dayTime;
    }

}
