package ee.siimplangi.ilmaprognoos.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ee.siimplangi.ilmaprognoos.R;

/**
 * Created by Siim on 13.04.2015.
 */
public class WeatherDescriptionDialog extends AlertDialog {



    protected WeatherDescriptionDialog(Context context, String phenomenon, String description, int style) {
        super(context, style);
        setTitle(phenomenon);
        setMessage(description);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setButton(BUTTON_NEUTRAL, context.getString(R.string.dismiss), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        show();
    }


}
