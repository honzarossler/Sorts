package cz.janrossler.sorts;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.adapter.SessionPreviewAdapter;

@SuppressWarnings({"FieldCanBeLocal", "deprecation"})
public class SessionActivity extends AppCompatActivity implements SortingService.Callbacks {
    private NumberManager numberManager;
    private JSONObject session;
    private Intent intent;
    Intent sortIntent;
    private JSONArray unsorted = new JSONArray();
    private JSONArray sorted = new JSONArray();

    SortingService myService;

    private TextView instance;
    private TextView total_length;
    private TextView sort_technology;
    private TextView sort_time;
    private FloatingActionButton fab_sort_now;
    private FloatingActionButton fab_delete_session;
    private RecyclerView session_preview;
    private SessionPreviewAdapter adapter;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        numberManager = new NumberManager(this);

        intent = getIntent();
        if(intent == null || !intent.hasExtra("session")) finish();

        setContentView(R.layout.activity_session);

        instance = findViewById(R.id.instance);
        total_length = findViewById(R.id.total_length);
        sort_technology = findViewById(R.id.sort_technology);
        sort_time = findViewById(R.id.sort_time);
        fab_sort_now = findViewById(R.id.fab_sort_now);
        fab_delete_session = findViewById(R.id.fab_delete_session);
        session_preview = findViewById(R.id.session_preview);

        sortIntent = new Intent(SessionActivity.this, SortingService.class);
        bindService(sortIntent, mConnection, Context.BIND_AUTO_CREATE);

        pDialog = new ProgressDialog(this);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        update();
    }

    @SuppressLint("SetTextI18n")
    void update(){
        session = numberManager.getSession(intent.getStringExtra("session"));
        sorted = new JSONArray();
        unsorted = new JSONArray();

        List<Integer> list_sorted = numberManager.getSortedSessionList(intent.getStringExtra("session"));
        List<Integer> list_unsorted = numberManager.getUnsortedSessionList(intent.getStringExtra("session"));

        for(int i = 0; i < list_unsorted.size(); i++)
            unsorted.put(list_unsorted.get(i));
        for(int i = 0; i < list_sorted.size(); i++)
            sorted.put(list_sorted.get(i));

        try{
            instance.setText("Informace o instanci: %instance%".replace("%instance%", session.has("session") ? session.getString("session") : ""));
            total_length.setText("Počet čísel v seznamu: %total%".replace("%total%", session.has("length") ? session.getString("length") : ""));
            sort_technology.setText("Třídicí algoritmus: %alg%".replace("%alg%", session.has("algorithm") ? session.getString("algorithm") : ""));
            sort_time.setText("Doba třídění: %time%".replace("%time%", session.has("time") ? session.getString("time")+"s" : "~s"));
        }catch (Exception e){
            e.fillInStackTrace();
            instance.setText("Informace o instanci");
            total_length.setText("Počet čísel v seznamu:");
            sort_technology.setText("Třídicí algoritmus:");
            sort_time.setText("Doba třídění:");
        }

        adapter = new SessionPreviewAdapter(this, unsorted, sorted);
        session_preview.setAdapter(adapter);
        session_preview.setLayoutManager(new LinearLayoutManager(this));

        fab_sort_now.setOnClickListener(v -> {
            try {
                JSONArray sorts = Utilities.getSortAlgorithms(this);
                List<String> allowedSorts = new ArrayList<>();

                int length = session.has("length")
                        ? session.getInt("length") : 0;

                for(int i = 0; i < sorts.length(); i++){
                    JSONObject sort = sorts.getJSONObject(i);

                    if(!sort.has("recommended_length")){
                        allowedSorts.add(sort.getString("name"));
                    }else{
                        JSONObject rl = sort.getJSONObject("recommended_length");
                        if(rl.has("max")){
                            if(rl.getInt("max") >= length
                                    || (rl.has("out_of_range") && rl.getBoolean("out_of_range"))){
                                allowedSorts.add(sort.getString("name"));
                            }
                        }else{
                            allowedSorts.add(sort.getString("name"));
                        }
                    }
                }

                CharSequence[] charSequences = new CharSequence[allowedSorts.size()];
                int index = 0;
                for(String sort : allowedSorts){
                    charSequences[index] = sort;
                    index++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Zvolte třídicí algoritmus.");
                builder.setSingleChoiceItems(charSequences, 0, null);
                builder.setPositiveButton("Třídit", (dialog, which) -> {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    Log.i("Selected", charSequences[selectedPosition].toString());

                    sortIntent = new Intent(SessionActivity.this, SortingService.class);
                    sortIntent.putExtra("session", intent.getStringExtra("session"));
                    sortIntent.putExtra("use-sort", charSequences[selectedPosition]);

                    pDialog.setMessage(Utilities.getRandomDialogStringWhileSorting());
                    pDialog.show();
                    startService(sortIntent);
                });
                builder.create().show();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        fab_delete_session.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Smazat instanci");
            builder.setMessage("Tato operace nenávratně smaže celou instanci včetně uložených dat.\n\nPřejete si pokračovat?");
            builder.setPositiveButton("Smazat", (dialog, which) -> {
                numberManager.removeSession(intent.getStringExtra("session"));
                finish();
            });
            builder.setNegativeButton("Zachovat", null);
            builder.show();
        });
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SortingService.LocalBinder binder = (SortingService.LocalBinder) service;
            myService = binder.getServiceInstance();
            myService.registerClient(SessionActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    public void updateClient() {
        update();
        pDialog.dismiss();
    }
}