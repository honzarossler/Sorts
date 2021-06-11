package cz.janrossler.sorts.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Utilities {
    public static final int MAX_TREE_SIZE = 1500;
    public static final int MAX_SESSION_SIZE = 2000000;
    public static final int MAX_GRAVITY_ALLOC = 1000000000;
    public static final int MAX_CHUNK_SIZE = 500000;

    @NonNull
    public static JSONArray getSortAlgorithms(Context context){
        return loadSortsFromAsset(context);
    }

    public static int getAllocSize(int size, int i){
        return size * 8 * i;
    }

    @NonNull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static JSONArray loadSortsFromAsset(@NonNull Context context) {
        JSONArray array;
        String json;
        try {
            InputStream is = context.getAssets().open("sorts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            array = new JSONArray(json);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return new JSONArray();
        }
        return array;
    }

    @NonNull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static JSONObject loadLanguagesFromAsset(@NonNull Context context) {
        JSONObject obj;
        String json;
        try {
            InputStream is = context.getAssets().open("lang.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            obj = new JSONObject(json);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
        return obj;
    }

    public static String getRandomDialogStringWhileSorting(){
        List<String> strings = new ArrayList<>();
        strings.add("Třídím čísla ...");
        strings.add("Hledám postupně jdoucí čísla ...");
        strings.add("Prohazuji a skládám ...");
        strings.add("Posouvám nuly na začátek ...");
        strings.add("Promíchávám a kontroluji ...");
        strings.add("Sestupně či vzestupně, to je oč tu běží ...");
        strings.add("Indexuji od nuly ...");
        strings.add("Pokud jsi použil BogoSort, přeji hodně štěstí s čekáním na výsledek.");

        return strings.get(ThreadLocalRandom.current().nextInt(0, strings.size()));
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
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
