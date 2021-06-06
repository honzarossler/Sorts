package cz.janrossler.sorts.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Utilities {
    public static final int MAX_TREE_SIZE = 1000;
    public static final int MAX_GRAVITY_ALLOC = 1500000000;

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
}
