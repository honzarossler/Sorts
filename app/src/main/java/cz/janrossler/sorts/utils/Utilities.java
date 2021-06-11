package cz.janrossler.sorts.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Utilities {
    public static final int MAX_TREE_SIZE = 1_500;
    public static final int MAX_GRAVITY_ALLOC = 1_000_000_000;
    public static final int MAX_CHUNK_SIZE = 500_000;

    @NonNull
    public static JSONArray getSortAlgorithms(@NonNull Context context){
        JSONArray array;
        String json;
        try {
            InputStream is = context.getAssets().open("sorts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int readedBytes = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            array = new JSONArray(json);

            Log.d("ReadedData", "Readed " + readedBytes + " bytes.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONArray();
        }
        return array;
    }

    public static int getAllocSize(int size, int i){
        return size * 8 * i;
    }

    @NonNull
    public static JSONObject loadLanguagesFromAsset(@NonNull Context context) {
        JSONObject obj;
        String json;
        try {
            InputStream is = context.getAssets().open("lang.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int readedBytes = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            obj = new JSONObject(json);

            Log.d("ReadedData", "Readed " + readedBytes + " bytes.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
        return obj;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Contract(pure = true)
    public static String getTimeFormat(int millis){
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
