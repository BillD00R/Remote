package lulz.just.remote;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.util.ArrayList;

public class Dialogs {
    private static ProgressDialog pd = null;
    private static boolean canceled = false;
    private static int count = 0;
    private static ArrayList<String> alerts = new ArrayList<String>();

    static void showPD(Context cnt, String txt) {
        if ((null != cnt) && (null != txt)) {

            count++;
            Log.d("COUNT++", String.valueOf(count));
            if (null != pd)
                if (pd.isShowing())
                    return;

            setCanceled(false);
            pd = new ProgressDialog(cnt);
            pd.setMessage(txt);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    count = 0;
                    setCanceled(true);
                }
            });
            pd.show();
        }
    }

    static void closePD() {
        count--;
        if (count < 0)
            count = 0;
        Log.d("COUNT--", String.valueOf(count));
        if (count <= 0) {
            if ((null != pd)) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pd = null;
            }
        }
    }


    static boolean isCanceled() {
        return canceled;
    }

    static void setCanceled(boolean canceled) {
        Dialogs.canceled = canceled;
    }

    static void showAlert(final String text, final Context cnt) {

        Log.d("Immortal", "AlertDialog creating");

        for (int i = 0; i < alerts.size(); i++) {
            if (alerts.get(i).equals(text))
                return;
        }

        if (null == cnt)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
        builder.setTitle("Ошибка")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(text)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alerts.remove(text);
                                dialog.cancel();
                            }
                        });
        alerts.add(text);
        AlertDialog alert = builder.create();
        Sound.playCritStop(cnt);
        alert.show();

    }

    static void showMessage(final String text, final Context cnt) {

        Log.d("Immortal", "MessageDialog creating: " + text);
        if (null == cnt)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
        builder.setMessage(text)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
