package ee.siimplangi.ilmaprognoos.data;

import java.util.Locale;

/**
 * Created by Siim on 13.04.2015.
 */
public enum DataLocale {

    ENG ("est", "http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php"),
    EST ("eng", "http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php?lang=eng");

    private String ISO3Code;
    private String dataUrl;

    public static DataLocale getDataLocale(Locale locale){
        for (DataLocale dataLocale : values()){
            if (dataLocale.getISO3Code().equals(locale.getISO3Language().toLowerCase())){
                return dataLocale;
            }
        }
        //fallback to eng
        return ENG;
    }

    private DataLocale(String ISO3Code, String dataUrl) {
        this.ISO3Code = ISO3Code.toLowerCase();
        this.dataUrl = dataUrl;
    }

    public String getISO3Code() {
        return ISO3Code;
    }

    public String getDataUrl() {
        return dataUrl;
    }
}
