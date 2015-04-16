package ee.siimplangi.ilmaprognoos.listeners;

import ee.siimplangi.ilmaprognoos.xml.XmlDownloadTask;

/**
 * Created by Siim on 13.04.2015.
 */
public interface XMLDownloadTaskListener {

    public void onXMLDownloadTaskFinished(XmlDownloadTask task);

}
