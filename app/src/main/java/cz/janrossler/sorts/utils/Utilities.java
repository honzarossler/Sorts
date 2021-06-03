package cz.janrossler.sorts.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utilities {
    public static final int MAX_TREE_SIZE = 1000;
    public static final int MAX_GRAVITY_ALLOC = 1500000000;

    @NonNull
    public static JSONArray getSortAlgorithms(Context context){
        JSONArray sorts = new JSONArray();

        try{
            sorts = new JSONArray(loadSortsFromAsset(context));
        }catch (Exception e){
            e.fillInStackTrace();
        }

        return sorts;
    }

    public static int getAllocSize(int size, int i){
        return size * 8 * i;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Nullable
    private static String loadSortsFromAsset(@NonNull Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("sorts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
}
