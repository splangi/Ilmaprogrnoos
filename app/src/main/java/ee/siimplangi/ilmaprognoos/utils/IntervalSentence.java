package ee.siimplangi.ilmaprognoos.utils;

import android.content.res.Resources;

import java.util.Locale;

import ee.siimplangi.ilmaprognoos.*;

/**
 * Created by Siim on 10.04.2015.
 */
public class IntervalSentence {

    private static final String estISO3Code = "est";

    private int min;
    private int max;
    private Locale locale;
    private Resources resources;

    public IntervalSentence(int min, int max, Resources resources) {
        this.min = min;
        this.max = max;
        this.resources = resources;
        this.locale = resources.getConfiguration().locale;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(min));
        sb.append(" ");
        sb.append(getStringFromResources(R.string.to));
        sb.append(" ");
        sb.append(getString(max));
        sb.append(" ");
        sb.append(getStringFromResources(R.string.degrees));
        return sb.toString();
    }

    private String getString(int num){
        StringBuilder sb = new StringBuilder();
        if (num < 0) {
            sb.append(getStringFromResources(R.string.minus));
            sb.append(" ");
        }
        num = Math.abs(num);
        if (num < 20){
            sb.append(getNumberFromResources(num));
        }
        else if (num < 100){
            sb.append(getTwentyToHundredSting(num, locale));
        }
        else{
            throw new IllegalStateException("Number out of bound!");
        }
        return sb.toString();
    }


    private String getTwentyToHundredSting(int num, Locale locale) {
        StringBuilder sb = new StringBuilder();
        int ones = num % 10;
        int tens = num - ones;
        sb.append(getNumberFromResources(tens));
        if (ones != 0) {
            if (locale.getISO3Country().toLowerCase().equals(estISO3Code)) {
                sb.append(" ");
            }
            //Fallback to english
            else {
                sb.append("-");
            }
            sb.append(getNumberFromResources(ones));
        }
        return sb.toString();
    }


    private String getNumberFromResources(int num){
        if (num < 0 || num > 99 || (num > 20 && num % 10 != 0)){
            throw new AssertionError("Number not in bounds");
        }
        Number number = Number.getNumber(num);
        int id = number.getStringId();
        return resources.getString(id);
    }

    private String getStringFromResources(int id){
        return resources.getString(id);
    }


}

