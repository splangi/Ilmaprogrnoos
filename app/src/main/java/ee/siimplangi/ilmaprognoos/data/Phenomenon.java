package ee.siimplangi.ilmaprognoos.data;

import android.util.Log;

import ee.siimplangi.ilmaprognoos.R;

/**
 * Created by Siim on 8.04.2015.
 */
public enum Phenomenon {

    CLEAR("Clear", R.string.clear, R.drawable.icon_sunny, R.drawable.day_clear, R.drawable.night_clear),
    FEW_CLOUDS("Few clouds", R.string.few_clouds, R.drawable.icon_sun_cloud, R.drawable.day_few_clouds, R.drawable.night_few_clouds),
    VARIABLE_CLOUDS("Variable clouds", R.string.variable_clouds, R.drawable.icon_sun_cloud, R.drawable.day_variable_clouds, R.drawable.night_variable_vlouds),
    CLOUDY_WITH_CLEAR_SPELLS("Cloudy with clear spells", R.string.cloudy_with_clear_spells, R.drawable.icon_cloud, R.drawable.day_cloudy_with_clear_spells, R.drawable.night_cloudy_with_clear_spells),
    CLOUDY("Cloudy", R.string.cloudy, R.drawable.icon_cloud, R.drawable.day_night_cloudy, 0),
    LIGHT_SNOW_SHOWER("Light snow shower", R.string.light_snow_shower, R.drawable.icon_snow, R.drawable.day_snow_showers, R.drawable.night_snow_showers),
    MODERATE_SNOW_SHOWER("Moderate snow shower", R.string.moderate_snow_shower, R.drawable.icon_snow,  R.drawable.day_snow_showers, R.drawable.night_snow_showers),
    HEAVY_SNOW_SHOWER("Heavy snow shower", R.string.heavy_snow_shower, R.drawable.icon_snow,  R.drawable.day_snow_showers, R.drawable.night_snow_showers),
    LIGHT_SHOWER("Light shower", R.string.light_shower, R.drawable.icon_shower_light, R.drawable.day_showers, R.drawable.night_showers),
    MODERATE_SHOWER("Moderate shower", R.string.moderate_shower, R.drawable.icon_shower,  R.drawable.day_showers, R.drawable.night_showers),
    HEAVY_SHOWER("Heavy shower", R.string.heavy_shower, R.drawable.icon_shower,  R.drawable.day_showers, R.drawable.night_showers),
    LIGHT_RAIN("Light rain", R.string.light_rain, R.drawable.icon_rain_light, R.drawable.day_night_rain, 0),
    MODERATE_RAIN("Moderate rain", R.string.moderate_rain, R.drawable.icon_rain, R.drawable.day_night_rain, 0),
    HEAVY_RAIN("Heavy rain", R.string.heavy_rain, R.drawable.icon_rain, R.drawable.day_night_rain, 0),
    RISK_OF_GLAZE("Risk of glaze", R.string.risk_of_glaze, R.drawable.icon_galze, R.drawable.day_night_risk_of_glaze, R.drawable.day_night_risk_of_glaze),
    LIGHT_SLEET("Light sleet", R.string.light_sleet, R.drawable.icon_sleet_light, R.drawable.day_night_sleet, 0),
    MODERATE_SLEET("Moderate sleet", R.string.moderate_sleet, R.drawable.icon_sleet, R.drawable.day_night_sleet, 0),
    LIGHT_SNOWFALL("Light snowfall", R.string.light_snowfall, R.drawable.icon_snow, R.drawable.day_night_snow, 0),
    MODERATE_SNOWFALL("Moderate snowfall", R.string.moderate_snowfall, R.drawable.icon_snow, R.drawable.day_night_snow, 0),
    HEAVY_SNOWFALL("Heavy snowfall", R.string.heavy_snowfall, R.drawable.icon_snow, R.drawable.day_night_snow, 0),
    SNOWSTORM("Snowstorm", R.string.snowstorm, R.drawable.icon_snowstorm, R.drawable.day_night_snowstorm, 0),
    DRIFTING_SNOW("Drifting snow", R.string.drifting_snow, R.drawable.icon_snowstorm, R.drawable.day_night_snowstorm, 0),
    HAIL("Hail", R.string.hail, R.drawable.icon_hail, R.drawable.day_night_hail, 0),
    MIST("Mist", R.string.mist, R.drawable.icon_hail, R.drawable.day_night_fog, 0),
    FOG("Fog", R.string.fog, R.drawable.icon_fog, R.drawable.day_night_fog, 0),
    THUNDER("Thunder", R.string.thunder, R.drawable.icon_thunder, R.drawable.day_night_thunder, 0),
    THUNDERSTORM("Thunderstorm", R.string.thunderstorm, R.drawable.icon_thunderstorm, R.drawable.day_night_thunder, 0),
    UNKNOWN("unknown", R.string.unknown, 0, R.drawable.unknown, 0);


    private final String name;
    private final int stringId;
    private final static String TAG = "Phenomenon";
    private final int iconId;
    private final int dayLargeIconId;
    private final int nightLargeIconId;

    private Phenomenon(String name, int stringId, int iconId, int dayBackgroundId, int nightBackgroundId) {
        this.name = name;
        this.stringId = stringId;
        this.iconId = iconId;
        this.dayLargeIconId = dayBackgroundId;
        this.nightLargeIconId = nightBackgroundId;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public static Phenomenon getPhenomenon(String stringPhenomenon) {
        for (Phenomenon phenomenon : values()) {
            if (phenomenon.equalsName(stringPhenomenon)) {
                return phenomenon;
            }
        }
        Log.e(TAG, "Phenomenon not found!");
        return UNKNOWN;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getStringId() {
        return stringId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getDayLargeIconId() {
        return dayLargeIconId;
    }

    public int getNightLargeIconId() {
        if (nightLargeIconId != 0){
            return nightLargeIconId;
        }
        //Fallback to day icon
        return dayLargeIconId;
    }
}
