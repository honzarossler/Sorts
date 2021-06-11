package cz.janrossler.sorts.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NumberManager {
    private SharedPreferences smallData;
    private SharedPreferences bigData;
    private Context context;

    public NumberManager(@NonNull Context context){
        this.context = context;
        smallData = context.getSharedPreferences("sorting-sessions", Activity.MODE_PRIVATE);
        bigData = context.getSharedPreferences("sorting-lists", Activity.MODE_PRIVATE);
    }

    public void removeSession(String session){
        if(smallData.contains(session))
            smallData.edit().remove(session).apply();

        if(bigData.contains(session + "unsorted"))
            bigData.edit().remove(session + "unsorted").apply();

        if(bigData.contains(session + "sorted"))
            bigData.edit().remove(session + "sorted").apply();
    }

    public void createList(String session, int length, int min, int max){
        Session _session = getSession(session);

        List<String> chunks = new ArrayList<>();
        int chunk_sum = (int)(length / Utilities.MAX_CHUNK_SIZE) + 1;
        for(int i = 0; i < chunk_sum; i++){
            chunks.add("pt" + i);
        }

        for(int i = 0; i < chunks.size(); i++){
            JSONArray numbers = new JSONArray();
            if(i == chunks.size() - 1){
                for(int j = 0; j < length - (Utilities.MAX_CHUNK_SIZE * (chunk_sum - 1)); j++){
                    int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                    numbers.put(randomNum);
                }
            }else
                for(int j = 0; j < Utilities.MAX_CHUNK_SIZE; j++){
                    int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
                    numbers.put(randomNum);
                }
            bigData.edit().putString(session + "unsorted_" + chunks.get(i), numbers.toString()).apply();
        }

        _session.setLength(length);
        _session.setMin(min);
        _session.setMax(max);
        _session.setIsEditable(false);
        _session.setChunks(chunk_sum);
        smallData.edit().putString(session, _session.toObject().toString()).apply();
    }

    public void pushList(String session, @NonNull List<Integer> list){
        int min = 0;
        int max = 0;

        for(int i  = 0; i < list.size(); i++){
            if(min > list.get(i)) min = list.get(i);
            if(max < list.get(i)) max = list.get(i);
        }

        int chunks = saveUnsortedList(session, list);

        Session _session = getSession(session);
        _session.setLength(list.size());
        _session.setMin(min);
        _session.setMax(max);
        _session.setIsEditable(true);
        _session.setChunks(chunks);
        smallData.edit().putString(session, _session.toObject().toString()).apply();
    }

    protected int saveUnsortedList(String session, @NonNull List<Integer> list){
        int length = list.size();

        List<String> chunks = new ArrayList<>();
        int chunk_sum = (int)(length / Utilities.MAX_CHUNK_SIZE) + 1;
        for(int i = 0; i < chunk_sum; i++){
            chunks.add("pt" + i);
        }

        for(int i = 0; i < chunks.size(); i++){
            JSONArray numbers = new JSONArray();
            if(i == chunks.size() - 1){
                for(int j = 0; j < length - (Utilities.MAX_CHUNK_SIZE * (chunk_sum - 1)); j++){
                    numbers.put(list.get(j));
                }
            }else
                for(int j = 0; j < Utilities.MAX_CHUNK_SIZE; j++){
                    numbers.put(list.get(j));
                }
            bigData.edit().putString(session + "unsorted_" + chunks.get(i), numbers.toString()).apply();
        }
        return chunk_sum;
    }

    public void saveMergedChunks(String _session, String algorithm, int lastTime, @NonNull JSONArray chunk, @NonNull int[] indexes){
        Session session = getSession(_session);
        session.setAlgorithm(algorithm);
        session.setTime(lastTime);

        smallData.edit().putString(_session, session.toObject().toString()).apply();

        try {
            List<String> chunks = session.getChunks();
            int total = indexes.length;
            JSONArray l = new JSONArray();

            for(int i = 0; i < total; i++){
                JSONArray s = new JSONArray();
                for (int j = Utilities.MAX_CHUNK_SIZE * i; j < Utilities.MAX_CHUNK_SIZE * (i + 1); j++)
                    if(j < chunk.length()) s.put(chunk.getInt(j));
                l.put(s);
            }

            for(int i = 0; i < total; i++){
                JSONArray arr = l.optJSONArray(i);
                bigData.edit().putString(_session + "sorted_" + chunks.get(indexes[i]), arr.toString()).apply();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Session getSession(String _session){
        Session session = new Session();
        try {
            JSONObject sessionO = new JSONObject(smallData.getString(_session, "{}"));
            sessionO.put("session", _session);
            session = new Session(sessionO);
        }catch (Exception ignored) {}
        return session;
    }

    public List<Integer> getUnsortedSessionList(String session, int index){
        List<Integer> numbers = new ArrayList<>();
        Session sess = getSession(session);

        int ii;
        if(sess.getChunkPages() > index){
            ii = index;
        }else{
            ii = 0;
        }

        List<String> chunks = sess.getChunks();

        if(bigData.contains(session + "unsorted_" + chunks.get(ii))){
            try{
                JSONArray unsorted = new JSONArray(bigData.getString(session + "unsorted_" + chunks.get(ii), "[]"));
                for(int i = 0; i < unsorted.length(); i++)
                    if (unsorted.get(i) instanceof Integer)
                        numbers.add(unsorted.getInt(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return numbers;
    }

    public List<Integer> getSortedSessionList(String session, int index){
        List<Integer> numbers = new ArrayList<>();
        Session sess = getSession(session);

        int ii;
        if(sess.getChunkPages() > index){
            ii = index;
        }else{
            ii = 0;
        }

        List<String> chunks = sess.getChunks();

        if(bigData.contains(session + "sorted_" + chunks.get(ii))){
            try{
                JSONArray unsorted = new JSONArray(bigData.getString(session + "sorted_" + chunks.get(ii), "[]"));
                for(int i = 0; i < unsorted.length(); i++)
                    if (unsorted.get(i) instanceof Integer)
                        numbers.add(unsorted.getInt(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return numbers;
    }

    public JSONArray getAllSessions(){
        JSONArray sessions = new JSONArray();

        try{
            Map<String,?> keys = smallData.getAll();
            for(Map.Entry<String,?> entry : keys.entrySet()){
                JSONObject session = new JSONObject(smallData.getString(entry.getKey(), "{}"));
                session.put("session", entry.getKey());
                sessions.put(session);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return sessions;
    }
}
