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
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.SortingService;
import cz.janrossler.sorts.utils.Utilities;

@SuppressWarnings({"FieldCanBeLocal", "deprecation"})
public class SessionActivity extends AppCompatActivity implements SortingService.Callbacks {
    private NumberManager numberManager;
    private JSONObject session;
    private Intent intent;
    private Intent sortIntent;
    private JSONArray unsorted = new JSONArray();
    private JSONArray sorted = new JSONArray();
    private boolean isAscending = true;
    private boolean isEditable = false;
    private int defaultIndex = 0;
    private LinearLayoutManager manager;
    private SortingService myService;

    private TextView instance;
    private TextView total_length;
    private TextView sort_technology;
    private TextView sort_time;
    private FloatingActionButton fab_sort_now;
    private FloatingActionButton fab_delete_session;
    private FloatingActionButton fab_tree;
    private FloatingActionButton fab_sort_view;
    private RecyclerView session_preview;
    private SessionPreviewAdapter adapter;

    private ProgressDialog pDialog;
    private ProgressDialog pSearchDialog;

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
        fab_tree = findViewById(R.id.fab_tree);
        fab_sort_view = findViewById(R.id.fab_sort_view);
        session_preview = findViewById(R.id.session_preview);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        sortIntent = new Intent(SessionActivity.this, SortingService.class);
        bindService(sortIntent, mConnection, Context.BIND_AUTO_CREATE);

        pDialog = new ProgressDialog(this);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        pSearchDialog = new ProgressDialog(this);
        pSearchDialog.setIndeterminate(true);
        pSearchDialog.setCancelable(false);
        pSearchDialog.setMessage("Počítám výskyty čísel");

        session_preview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    fab_sort_now.hide();
                    fab_delete_session.hide();
                    fab_tree.hide();
                    fab_sort_view.hide();
                } else if (dy < 0) {
                    // Scroll Up
                    fab_sort_now.show();
                    fab_delete_session.show();
                    fab_tree.show();
                    fab_sort_view.show();
                }
            }
        });

        fab_sort_view.setOnClickListener(v -> {
            if(isAscending){
                manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
                fab_sort_view.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                defaultIndex = unsorted.length()-1;
            }else{
                manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                fab_sort_view.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                defaultIndex = 0;
            }
            isAscending = !isAscending;
            update();
        });

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
            total_length.setText("%total% čísel".replace("%total%", session.has("length") ? session.getString("length") : ""));
            sort_technology.setText("%alg%".replace("%alg%", session.has("algorithm") ? session.getString("algorithm") : "Neznámé"));
            sort_time.setText("%time%".replace("%time%", session.has("time") ? session.getString("time")+"s" : "~s"));
            isEditable = session.has("editable") && session.getBoolean("editable");
        }catch (Exception e){
            e.printStackTrace();
            instance.setText("Informace o instanci");
            total_length.setText("0 čísel");
            sort_technology.setText("Neznámé");
            sort_time.setText("~s");
            isEditable = false;
        }

        adapter = new SessionPreviewAdapter(this, unsorted, sorted);
        session_preview.setAdapter(adapter);
        session_preview.setLayoutManager(manager);
        session_preview.scrollToPosition(defaultIndex);

        fab_tree.setEnabled(unsorted.length() <= Utilities.MAX_TREE_SIZE || isEditable);

        fab_sort_now.setOnClickListener(v -> {
            try {
                JSONArray sorts = Utilities.getSortAlgorithms(this);
                List<String> allowedSorts = new ArrayList<>();

                int length = session.has("length")
                        ? session.getInt("length") : 0;

                int max_gen = session.has("max")
                        ? session.getInt("max") : length;

                for(int i = 0; i < sorts.length(); i++){
                    JSONObject sort = sorts.getJSONObject(i);

                    if(sort.has("enabled") && !sort.getBoolean("enabled"))
                        continue;

                    if(!sort.has("recommended_length")){
                        allowedSorts.add(sort.getString("name"));
                    }else{
                        JSONObject rl = sort.getJSONObject("recommended_length");
                        boolean hasMax = rl.has("max");
                        boolean hasMaxGen = rl.has("max_gen");
                        boolean hasMaxAlloc = rl.has("max_alloc");

                        boolean maxDone = !hasMax || rl.getInt("max") >= length;
                        boolean maxGenDone = !hasMaxGen || rl.getInt("max_gen") >= max_gen;
                        boolean maxAllocDone = !hasMaxAlloc
                                || rl.getInt("max_alloc")
                                >= Utilities.getAllocSize(unsorted.length(), unsorted.getInt(0));

                        if(maxDone && maxGenDone && maxAllocDone)
                            allowedSorts.add(sort.getString("name"));
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

        fab_tree.setOnClickListener(v -> {
            Intent i = new Intent(this, TreeViewActivity.class).putExtra("session", intent.getStringExtra("session"));
            startActivity(i);
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

    @Override
    public void sortFailed(String message) {
        update();
        pDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}