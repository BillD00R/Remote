package lulz.just.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends AsyncTask<String, String, String> {
    private ProgressBar progress_bar = null;
    private Context mContext;
    private Handler myhandler = null;
    private boolean needGUID = true;
    private String progressText = null;
    private boolean exitMessage = false;

    Connection(Context c) {
        super();
        mContext = c;
    }

    Connection(Context c, boolean ndG) {
        super();
        mContext = c;
        needGUID = ndG;
    }

    Connection(Context c, Handler hndlr) {
        super();
        mContext = c;
        myhandler = hndlr;
    }

    public Connection(Context c, ProgressBar pb) {
        super();
        mContext = c;
        progress_bar = pb;
    }

    private static boolean checkWithRegExp(String userNameString) {
        Pattern p = Pattern.compile("([\\d|,-])");
        Matcher m = p.matcher(userNameString);
        return m.replaceAll("").equals("");
    }

    boolean thisIsAnError(String result) {

        if (exitMessage)
            return false;

        if (result.startsWith("<?xml"))
            return false;
        if (result.startsWith("{"))
            return false;
        if (result.startsWith("OK"))
            return false;
        if (result.length() > 250)
            return false;
        /*if (result.contains("Access violation"))
            return false;*/
        return !result.contains("|") && !checkWithRegExp(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext.getApplicationContext());

        if (sharedPrefs.getString(Globals.ADDRESS, "").equals("")) {
            if (exitMessage) {
                returnTaskResult("nein");
                this.cancel(true);
                return;
            }
            Dialogs.showAlert("Введите IP в настройках приложения", mContext);
            this.cancel(true);
            return;
        }
        if (sharedPrefs.getString(Globals.PORT, "").equals("")) {
            if (exitMessage) {
                returnTaskResult("nein");
                this.cancel(true);
                return;
            }
            Dialogs.showAlert("Введите порт в настройках приложения", mContext);
            this.cancel(true);
            return;
        }

        if (null != progress_bar) {
            progress_bar.setVisibility(View.VISIBLE);
        } else if (null != mContext) {
            /*if (null == progressText) progressText = mContext.getString(R.string.loadingData_text);
            Dialogs.showPD(mContext, progressText);*/
        }

    }

    //Чтоб не писать каждый раз всё это вот
    private String prepareString(String sCommand, String... paramsArr) {

        String sParams = "";
        if ((null != paramsArr)&&(paramsArr.length>0)) {
            for (String param : paramsArr) {
                sParams = sParams + param + "|";
            }
            sCommand = sCommand + "|" + sParams;
        }

        return sCommand + "<EOF>";
    }

    //то же, но сразу и выполняем
    void sendString(String sCommand, String... paramsArr) {

        this.execute(prepareString(sCommand, paramsArr));
    }

    //то же, но сразу и выполняем
    void sendString(int resource, String... paramsArr) {

        String sCommand = (String) mContext.getText(resource);
        this.execute(prepareString(sCommand, paramsArr));
    }

    @Override
    protected String doInBackground(String... params) {

        if (this.isCancelled()) {
            return "";
        }

        String temp, buf = "nonono";
        String st = "";

        Socket socket = null;
        try {


            socket = SocketCreator.getSocket(mContext);

            if (null == socket) {
                return "Сервер недоступен!";
            }

            Log.d("ClientActivity", "good");
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                    .getOutputStream())), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            for (String send : params) {
                buf = send;
                Log.d("Connection", send);
                out.println(send);
            }

            while (true) {
                if (Dialogs.isCanceled())
                    return "Загрузка данных отменена.";
                temp = input.readLine();

                if (temp == null) {
                    break;
                }
                if (temp.equals("WAIT_ANSWER_SERVER_APP")) {
                    continue;
                }
                if (temp.contains("<EOF>")) {
                    st += temp.replace("<EOF>", "");
                    break;
                }
                Log.d("Icoming", temp);
                st += temp;
            }

            socket.close();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            if (socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (Dialogs.isCanceled())
                return "Загрузка данных отменена.";

            Log.e("Error on command", "1" + buf);
            return "Превышено время ожидания ответа от сервера.";

        } catch (Exception e) {
            e.printStackTrace();
            if (null != socket) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                if (Dialogs.isCanceled())
                    return "Загрузка данных отменена.";
            }
            return "Сервер недоступен!";
        }
        return st;
    }

    private boolean thisIsGUID(String st) {
        if (st.equals("")) return false;
        Pattern p = Pattern.compile("^(\\{?([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12}\\}{0,1})$");
        Matcher m = p.matcher(st);
        return m.replaceAll("").equals("");
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Result:", result);
        result = result.replaceAll("\\\\n", "\n");
        if (exitMessage) {
            returnTaskResult("nein");
            return;
        }
        if (result.equals("Загрузка данных отменена.")) {
            Dialogs.setCanceled(false);
            return;
        }

        if (thisIsAnError(result)) {
            Dialogs.showAlert(result.replace("#ERROR", ""), mContext);
        } else if (result.startsWith("{")) {
            String error = JSONMaster.getFieldFromJSON("ERROR", result);
            if (!error.equals("")) Dialogs.showMessage(error, mContext);
            else returnTaskResult(result);
        } else returnTaskResult(result);

        if (null != progress_bar) {
            progress_bar.setVisibility(View.GONE);
            return;
        }
        //Dialogs.closePD();
    }

    protected void returnTaskResult(String result) {
        Log.d("returnResult", result);
        if (null != myhandler) {
            Message msg = myhandler.obtainMessage(0, result);
            myhandler.sendMessage(msg);
        }
    }

    void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public void executeOff(String s) {
        exitMessage = true;
        this.execute(s);
    }
}
