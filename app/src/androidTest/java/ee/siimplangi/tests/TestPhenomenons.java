package ee.siimplangi.tests;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ee.siimplangi.ilmaprognoos.data.Phenomenon;

/**
 * Created by Siim on 14.04.2015.
 */
public class TestPhenomenons extends TestCase{

    private static final String TAG = "TestPhenomenons";

    String sList[] = {"Clear",
            "Few clouds",
            "Variable clouds",
            "Cloudy with clear spells",
            "Cloudy",
            "Light snow shower",
            "Moderate snow shower",
            "Heavy snow shower",
            "Light shower",
            "Moderate shower",
            "Heavy shower",
            "Light rain",
            "Moderate rain",
            "Heavy rain",
            "Risk of glaze",
            "Light sleet",
            "Moderate sleet",
            "Light snowfall",
            "Moderate snowfall",
            "Heavy snowfall",
            "Snowstorm",
            "Drifting snow",
            "Hail",
            "Mist",
            "Fog",
            "Thunder",
            "Thunderstorm",
            };




    public void testPhenomenons(){
        List<String> list1 = new ArrayList<>();
        Collections.addAll(list1, sList);
        List<String> list2 = new ArrayList<>();
        for (Phenomenon phenomenon :Phenomenon.values()){
            list2.add(phenomenon.toString());
        }
        for (String s : list1){
            try {
                if (!s.equals("unknown")){
                    assertTrue(list2.contains(s));
                }
            } catch (AssertionFailedError e){
                throw e;
            }
        }
        for (String s : list2){
            try {
                if (!s.equals("unknown")){
                    assertTrue(list1.contains(s));
                }
            } catch (AssertionFailedError e){
                throw e;
            }
        }
    }

    public void testLength() throws Exception {
        //Minus the unknown value;
        assertEquals(sList.length, Phenomenon.values().length-1);
    }


}
