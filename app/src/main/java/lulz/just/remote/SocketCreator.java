package lulz.just.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

class SocketCreator {

    static SharedPreferences sharedPrefs = null;
    static String ip = "";
    static int port = 0;
    private static Socket socket = null;

    static Socket getSocket(Context cnt) {



        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(cnt.getApplicationContext());



        ip = sharedPrefs.getString(Globals.ADDRESS, "");

        String portST = sharedPrefs.getString(Globals.PORT, "");

        if (!portST.equals(""))
            port= Integer.valueOf(portST);

        if(connect()) {
            Log.d("IP:", ip);
            return socket;
        }

        return null;
    }

    private static boolean connect(){
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        socket = new Socket();
        try {
            socket.connect(socketAddress, 5000);
            socket.setSoTimeout(5000);
            if (socket.isConnected())
                return true;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
