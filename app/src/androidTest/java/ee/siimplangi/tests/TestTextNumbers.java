package ee.siimplangi.tests;

import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityTestCase;

import junit.framework.AssertionFailedError;

import java.lang.reflect.Method;

import ee.siimplangi.ilmaprognoos.utils.IntervalSentence;

/**
 * Created by Siim on 15.04.2015.
 */
public class TestTextNumbers extends ActivityTestCase{



    public void testInterval() throws Exception {



        Method method = IntervalSentence.class.getDeclaredMethod("getString", int.class);
        method.setAccessible(true);
        Context testContext = getInstrumentation().getTargetContext();
        Resources testRes = testContext.getResources();
        for (int i = -99; i < 100; i++){
            for (int j = i; j < 100; j++){
                IntervalSentence intervalSentence = new IntervalSentence(i, j, testRes);
                String first = EnglishNumberToWords.convert99(Math.abs(i));
                String second = EnglishNumberToWords.convert99(Math.abs(j));
                String sentence = intervalSentence.toString();
                try{
                    assertTrue(sentence.contains(first));
                    assertTrue(sentence.contains(second));
                    if (i < 0 || j < 0){
                        assertTrue(sentence.contains("minus"));
                    }
                } catch (AssertionFailedError e){
                    //Use debugging to catch
                    throw e;
                }

            }

        }


    }


}
