package ee.siimplangi.ilmaprognoos.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Siim on 8.04.2015.
 */
public class PlaceForecast implements Parcelable {

    private static final String TAG = "PlaceForecast";

    private String place;
    private int tempDay;
    private int tempNight;
    private Phenomenon phenomenon;
    private DayTime dayTime;

    public PlaceForecast(DayTime dayTime) {
        this.dayTime = dayTime;
    }

    public final static Creator CREATOR = new Creator<PlaceForecast>() {

        @Override
        public PlaceForecast createFromParcel(Parcel parcel) {
            PlaceForecast placeForecast = new PlaceForecast((DayTime) parcel.readSerializable());
            placeForecast.setPlace(parcel.readString());
            placeForecast.setTempDay(parcel.readInt());
            placeForecast.setTempNight(parcel.readInt());
            placeForecast.setPhenomenon((Phenomenon) parcel.readSerializable());
            return placeForecast;
        }

        @Override
        public PlaceForecast[] newArray(int i) {
            return new PlaceForecast[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(dayTime);
        parcel.writeString(place);
        parcel.writeInt(tempDay);
        parcel.writeInt(tempNight);
        parcel.writeSerializable(phenomenon);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof PlaceForecast) {
            PlaceForecast placeForecast = (PlaceForecast) o;
            return getPlace().equals(placeForecast.getPlace());
        }
        return false;
    }

    public PlaceForecast merge(PlaceForecast anotherForecast) {
        if (this.equals(anotherForecast)) {
            switch (dayTime) {

                case DAY:
                    tempNight = anotherForecast.getTempNight();
                    break;
                case NIGHT:
                    tempDay = anotherForecast.getTempDay();
                    phenomenon = anotherForecast.getPhenomenon();
                    break;
                default:
                    Log.e(TAG, "PlaceForecast did not have the phenomenon set");
            }
        } else {
            Log.e(TAG, "Merge was called for forecasts that were not for the same place!");
        }
        return this;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getTempDay() {
        return tempDay;
    }

    public void setTempDay(int tempDay) {
        this.tempDay = tempDay;
    }

    public int getTempNight() {
        return tempNight;
    }

    public void setTempNight(int tempNight) {
        this.tempNight = tempNight;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = phenomenon;
    }
}
