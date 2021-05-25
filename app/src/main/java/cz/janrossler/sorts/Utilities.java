package cz.janrossler.sorts;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utilities {

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
}
