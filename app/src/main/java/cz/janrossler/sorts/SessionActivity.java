package cz.janrossler.sorts;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.adapter.SessionPreviewAdapter;
import cz.janrossler.sorts.sortable.AsyncSearch;
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.Session;
import cz.janrossler.sorts.utils.SortingService;
import cz.janrossler.sorts.utils.Template;
import cz.janrossler.sorts.utils.Utilities;

@SuppressWarnings({"FieldCanBeLocal", "deprecation"})
public class SessionActivity extends AppCompatActivity implements SortingService.Callbacks {
    private NumberManager numberManager;
    private Session session;
    private Intent intent;
    private Intent sortIntent;
    private JSONArray unsorted = new JSONArray();
    private JSONArray sorted = new JSONArray();
    private boolean isAscending = true;
    private boolean isEditable = false;
    private int defaultIndex = 0;
    private int chunkPage = 0;
    private LinearLayoutManager manager;
    private SortingService myService;

    private TextView instance;
    private TextView total_length;
    private TextView sort_technology;
    private TextView sort_time;
    private TextView sort_page;
    private FloatingActionButton fab_sort_now;
    private FloatingActionButton fab_delete_session;
    private FloatingActionButton fab_tree;
    private FloatingActionButton fab_sort_view;
    private FloatingActionButton fab_search;
    private RecyclerView session_preview;
    private SessionPreviewAdapter adapter;

    private ProgressDialog pDialog;
    private ProgressDialog pSearchDialog;
    private int chunkTotal = 0;

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
        sort_page = findViewById(R.id.sort_page);
        fab_sort_now = findViewById(R.id.fab_sort_now);
        fab_delete_session = findViewById(R.id.fab_delete_session);
        fab_tree = findViewById(R.id.fab_tree);
        fab_sort_view = findViewById(R.id.fab_sort_view);
        fab_search = findViewById(R.id.fab_search);
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
        pSearchDialog.setMessage(getString(R.string.dialog_message_finding_number));

        session_preview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    fab_sort_now.hide();
                    fab_delete_session.hide();
                    if(unsorted.length() <= Utilities.MAX_TREE_SIZE || isEditable) fab_tree.hide();
                    fab_sort_view.hide();
                    fab_search.hide();
                } else if (dy < 0) {
                    // Scroll Up
                    fab_sort_now.show();
                    fab_delete_session.show();
                    if(unsorted.length() <= Utilities.MAX_TREE_SIZE || isEditable) fab_tree.show();
                    fab_sort_view.show();
                    fab_search.show();
                }
            }
        });

        fab_sort_view.setOnClickListener(v -> {
            if(isAscending){
                manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
                fab_sort_view.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                defaultIndex = unsorted.length()-1;
                chunkPage = session.getChunkPages() - 1;
            }else{
                manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                fab_sort_view.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                defaultIndex = 0;
                chunkPage = 0;
            }
            isAscending = !isAscending;
            update();
        });

        fab_search.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_message_input_search_number));

            EditText edit = new EditText(this);
            edit.setHint("...");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setRawInputType(Configuration.KEYBOARD_12KEY);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            edit.setLayoutParams(params);

            builder.setView(edit);
            builder.setPositiveButton(getString(R.string.action_search), (dialog, which) -> {
                try {
                    pSearchDialog.show();
                    AsyncSearch search = new AsyncSearch(
                            this,
                            intent.getStringExtra("session"),
                            sResult -> {
                                pSearchDialog.dismiss();

                                DialogInterface dialogFor = Template.getDialogFor(this,
                                        Template.DIALOG_SEARCH_RESULT, sResult);
                                if(dialogFor instanceof AlertDialog){
                                    ((AlertDialog) dialogFor).show();
                                }else if(dialogFor instanceof BottomSheetDialog){
                                    ((BottomSheetDialog) dialogFor).show();
                                }else Toast.makeText(this,
                                        getString(R.string.dialog_message_search_result_positive)
                                                .replace("%num%", String.valueOf(sResult.value))
                                                .replace("%amount%", String.valueOf(sResult.amount)),
                                        Toast.LENGTH_LONG).show();
                    });
                    search.execute(Integer.parseInt(edit.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                    pSearchDialog.dismiss();
                }
            });
            builder.setNegativeButton(getString(R.string.action_cancel), null);
            builder.show();
        });

        sort_page.setOnClickListener(v -> {
            CharSequence[] chars = new CharSequence[chunkTotal];
            for(int i = 0; i < chunkTotal; i++){
                chars[i] = String.valueOf(i + 1);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zvolte stránku");
            builder.setSingleChoiceItems(chars, chunkPage,null);
            builder.setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                chunkPage = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                update();
            });
            builder.show();
        });

        update();
    }

    @SuppressLint("SetTextI18n")
    void update(){
        session = numberManager.getSession(intent.getStringExtra("session"));
        chunkTotal = session.getChunkPages();
        sorted = new JSONArray();
        unsorted = new JSONArray();

        List<Integer> list_sorted = numberManager.getSortedSessionList(intent.getStringExtra("session"), chunkPage);
        List<Integer> list_unsorted = numberManager.getUnsortedSessionList(intent.getStringExtra("session"), chunkPage);

        for(int i = 0; i < list_unsorted.size(); i++)
            unsorted.put(list_unsorted.get(i));
        for(int i = 0; i < list_sorted.size(); i++)
            sorted.put(list_sorted.get(i));

        sort_page.setText((chunkPage + 1) + "/" + chunkTotal);
        try{
            instance.setText(
                    getString(R.string.session_title)
                            .replace("%instance%", session.getId())
            );
            total_length.setText(
                    getString(R.string.session_numbers)
                            .replace("%total%", String.valueOf(session.getLength()))
            );
            sort_technology.setText(
                    !session.getAlgorithm().equals("")
                            ? session.getAlgorithm() : getString(R.string.unknown));
            sort_time.setText(Utilities.getTimeFormat(session.getTime()));
            isEditable = session.isEditable();
        }catch (Exception e){
            e.printStackTrace();
            instance.setText(getString(R.string.session_title)
                    .replace("%instance%",""));
            total_length.setText(getString(R.string.session_numbers)
                    .replace("%total%", "0"));
            sort_technology.setText(getString(R.string.unknown));
            sort_time.setText("~sec");
            isEditable = false;
        }

        adapter = new SessionPreviewAdapter(this, unsorted, sorted);
        session_preview.setAdapter(adapter);
        session_preview.setLayoutManager(manager);
        session_preview.scrollToPosition(defaultIndex);

        fab_tree.setVisibility(unsorted.length() <= Utilities.MAX_TREE_SIZE || isEditable ? View.VISIBLE : View.GONE);

        fab_sort_now.setOnClickListener(v -> {
            try {
                JSONArray sorts = Utilities.getSortAlgorithms(this);
                List<String> allowedSorts = new ArrayList<>();

                int length = session.getLength();

                int max_gen = session.getMax();

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
                builder.setTitle(getString(R.string.dialog_message_select_algorithm));
                builder.setSingleChoiceItems(charSequences, 0, null);
                builder.setPositiveButton(getString(R.string.title_home), (dialog, which) -> {
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
            builder.setTitle(getString(R.string.session_title_delete));
            builder.setMessage(getString(R.string.dialog_message_delete_session_long));
            builder.setPositiveButton(getString(R.string.action_delete), (dialog, which) -> {
                numberManager.removeSession(intent.getStringExtra("session"));
                finish();
            });
            builder.setNegativeButton(getString(R.string.action_cancel), null);
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