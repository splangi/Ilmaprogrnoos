package ee.siimplangi.tests;

import junit.framework.TestCase;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import ee.siimplangi.ilmaprognoos.data.DataLocale;
import ee.siimplangi.ilmaprognoos.data.ForecastWrapper;
import ee.siimplangi.ilmaprognoos.listeners.XMLDownloadTaskListener;
import ee.siimplangi.ilmaprognoos.listeners.XMLParserTaskListener;
import ee.siimplangi.ilmaprognoos.xml.XmlDownloadTask;
import ee.siimplangi.ilmaprognoos.xml.XmlParserTask;

/**
 * Created by Siim on 8.04.2015.
 */
public class XmlTaskTests extends TestCase{

    XmlDownloadTask downloader = new XmlDownloadTask(new XMLDownloadTaskListener() {
        @Override
        public void onXMLDownloadTaskFinished(XmlDownloadTask task) {

        }
    }, DataLocale.ENG);
    int forecastCount = 4;


    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testXmlParser() throws Exception {
        downloader.execute();
        XmlPullParser parser =  downloader.get();
        XmlParserTask mXmlParser = new XmlParserTask(new XMLParserTaskListener() {
            @Override
            public void onXMLParserTaskFinished(XmlParserTask task) {

            }
        });
        mXmlParser.execute(parser);
        assertTrue(parser != null);
        ArrayList<ForecastWrapper> result = mXmlParser.get();
        assertEquals(forecastCount, result.size());
    }
}
