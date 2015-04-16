package ee.siimplangi.ilmaprognoos.utils;

import ee.siimplangi.ilmaprognoos.R;

/**
 * Created by Siim on 10.04.2015.
 */
public enum Number {

    ZERO("zero" ,0, R.string.zero),
    ONE("one", 1, R.string.one),
    TWO("two", 2, R.string.two),
    THREE("three", 3, R.string.three),
    FOUR("four", 4, R.string.four),
    FIVE("five", 5, R.string.five),
    SIX("six", 6, R.string.six),
    SEVEN("seven", 7, R.string.seven),
    EIGHT("eight", 8, R.string.eight),
    NINE("nine", 9, R.string.nine),
    TEN("ten", 10, R.string.ten),
    ELEVEN("eleven", 11, R.string.eleven),
    TWELVE("twelve", 12, R.string.twelve),
    THIRTEEN("thirteen", 13, R.string.thirteen),
    FOURTEEN("fourteen", 14, R.string.fourteen),
    FIFTEEN("fifteen", 15, R.string.fifteen),
    SIXTEEN("sixteen", 16, R.string.sixteen),
    SEVENTEEN("seventeen", 17, R.string.seventeen),
    EIGHTEEN("eighteen", 18, R.string.eighteen),
    NINETEEN("nineteen", 19, R.string.nineteen),
    TWENTY("twenty", 20, R.string.twenty),
    THIRTY("thirty", 30, R.string.thirty),
    FORTY("forty", 40, R.string.forty),
    FIFTY("fifty", 50, R.string.fifty),
    SIXTY("sixty", 60, R.string.sixty),
    SEVENTY("seventy", 70, R.string.seventy),
    EIGHTY("eighty", 80, R.string.eighty),
    NINETY("ninety", 90, R.string.ninety);


    private String wordRepresentation;
    private int intRepresentation;
    private int stringId;


    private Number(String wordRepresentation, int intRepresentation, int stringId) {
        this.wordRepresentation = wordRepresentation;
        this.intRepresentation = intRepresentation;
        this.stringId = stringId;
    }

    public int getIntRepresentation(){
        return intRepresentation;
    }

    public static Number getNumber(int i){
        for (Number number : values()){
            if (number.getIntRepresentation() == i){
                return number;
            }
        }
        throw new IllegalStateException("Integer was not found in enum");
    }


    public int getStringId() {
        return stringId;
    }
}
