package cz.janrossler.sorts.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Session {
    protected String id = "";
    protected int length = 0;
    protected int max = 1;
    protected int min = 0;
    protected boolean editable = false;
    protected String algorithm = "";
    protected int time = 0;
    protected List<String> chunks = new ArrayList<>();

    public Session(){}

    public Session(JSONObject sess){
        try{
            if(sess.has("session"))
                this.id = sess.getString("session");
            if(sess.has("length"))
                this.length = sess.getInt("length");
            if(sess.has("min"))
                this.min = sess.getInt("min");
            if(sess.has("max"))
                this.max = sess.getInt("max");
            if(sess.has("editable"))
                this.editable = sess.getBoolean("editable");
            if(sess.has("time"))
                this.time = sess.getInt("time");
            if(sess.has("algorithm"))
                this.algorithm = sess.getString("algorithm");
            if(sess.has("chunks")) {
                for (int i = 0; i < sess.getJSONArray("chunks").length(); i++)
                    this.chunks.add(sess.getJSONArray("chunks").getString(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getTime() {
        return time;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public List<String> getChunks() {
        return chunks;
    }

    public int getChunkPages(){
        return getChunks().size();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setIsEditable(boolean editable) {
        this.editable = editable;
    }

    public void setChunks(int chunk_size) {
        List<String> chunks = new ArrayList<>();
        for(int i = 0; i < chunk_size; i++)
            chunks.add("pt" + i);
        this.chunks = chunks;
    }

    public JSONObject toObject(){
        JSONObject object = new JSONObject();
        try {
            object.put("session", id);
            object.put("length", length);
            object.put("min", min);
            object.put("max", max);
            object.put("algorithm", algorithm);
            object.put("time", time);
            object.put("editable", editable);

            JSONArray ch = new JSONArray();
            for (String chunk:chunks) {
                ch.put(chunk);
            }

            object.put("chunks", ch);
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
