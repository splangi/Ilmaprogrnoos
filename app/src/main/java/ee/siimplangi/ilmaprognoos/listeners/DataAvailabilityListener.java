package ee.siimplangi.ilmaprognoos.listeners;

import ee.siimplangi.ilmaprognoos.data.DataHandler;

/**
 * Created by Siim on 13.04.2015.
 */
public interface DataAvailabilityListener {

    public void onDataAvailable();

    public void onDataNotAvailable(DataHandler.FailReason reason);

}
