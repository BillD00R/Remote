package lulz.just.remote;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

public class Sound {

    private static SoundPool soundPool = null;
    private static int mGoodAdded, mGoodNotFoundInInventory, mGoodNotRegisteredInSystem, mGoodAmountIncreased;
    private static int mStreamID;
    private static AssetManager mAssetManager;

    public static void playCritStop(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedPrefs.getBoolean("Sounds", false)){
			
			try {
			    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			    Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
			    r.play();
			    Log.d("Sound:", "playing ringtone");
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
		else
			Log.d("Sound:", "Disabled");
	}

    public static void initInventorySound(Context mContext) {
        AudioAttributes attributes = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = mContext.getAssets();

        // получим идентификаторы
        mGoodAdded = loadSound("added_to_inventory.wav");
        mGoodNotFoundInInventory = loadSound("good_not_found_in_inventorylist.wav");
        mGoodNotRegisteredInSystem = loadSound("good_not_registered.wav");
        mGoodAmountIncreased = loadSound("chimes.wav");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private static void createOldSoundPool() {
        soundPool = new SoundPool(3, AudioManager.STREAM_NOTIFICATION, 0);
    }

    private static int playSound(int sound) {
        if ((sound > 0) && (null != soundPool)) {
            mStreamID = soundPool.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }

    private static int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "Cannot open file!");
            return -1;
        }
        return soundPool.load(afd, 1);
    }

    static void playGoodNotFound() {
        playSound(mGoodNotFoundInInventory);
    }

    static void playGoodAdded() {
        playSound(mGoodAdded);
    }

    static void playGoodIncreased() {
        playSound(mGoodAmountIncreased);
    }

    static void playGoodNotRegistered() {
        playSound(mGoodNotRegisteredInSystem);
    }

    static void pause() {
        soundPool.release();
        soundPool = null;
    }
}
