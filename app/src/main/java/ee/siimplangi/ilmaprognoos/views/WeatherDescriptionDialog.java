package ee.siimplangi.ilmaprognoos.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ee.siimplangi.ilmaprognoos.R;

/**
 * Created by Siim on 13.04.2015.
 */
public class WeatherDescriptionDialog extends DialogFragment {

    public static final String PHENOMENON_KEY = "phenomenonKey";
    public static final String DESCRIPTION_KEY = "descriptionKey";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        return new AlertDialog.Builder(getActivity())
                .setTitle(args.getString(PHENOMENON_KEY))
                .setMessage(args.getString(DESCRIPTION_KEY))
                .setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setCancelable(true)
                .create();
    }


}
