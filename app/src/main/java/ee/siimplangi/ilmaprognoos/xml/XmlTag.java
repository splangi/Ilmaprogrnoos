package ee.siimplangi.ilmaprognoos.xml;

/**
 * Created by Siim on 8.04.2015.
 */
public enum XmlTag {

    FORECASTS("forecasts"),
    FORECAST("forecast"),
    NIGHT("night"),
    DAY("day"),
    PHENOMENON("phenomenon"),
    TEMP_MIN("tempmin"),
    TEMP_MAX("tempmax"),
    TEXT("text"),
    PLACE("place"),
    NAME("name"),
    SPEED_MIN("speedmin"),
    SPEED_MAX("speedmax"),
    WIND("wind"),
    UNKNOWN("unknown");


    private final String name;

    private XmlTag(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName != null) && name.equals(otherName);
    }

    public static XmlTag getXmlTag(String tag){
        for (XmlTag xmlTag : values()){
            if (xmlTag.equalsName(tag)){
                return xmlTag;
            }
        }
        return UNKNOWN;
    }

    public String toString(){
        return name;
    }


}
