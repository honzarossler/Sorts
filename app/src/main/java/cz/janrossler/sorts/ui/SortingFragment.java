package cz.janrossler.sorts.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import cz.janrossler.sorts.NumberManager;
import cz.janrossler.sorts.R;
import cz.janrossler.sorts.adapter.SessionsAdapter;

public class SortingFragment extends Fragment {
    private Activity activity;
    private ActionBar actionBar;
    private FloatingActionButton add;
    private RecyclerView sort_sessions;

    private NumberManager numberManager;
    private SessionsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View root = activity.getLayoutInflater().inflate(R.layout.fragment_home, null);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        sort_sessions = root.findViewById(R.id.sort_sessions);
        add = root.findViewById(R.id.add);

        numberManager = new NumberManager(activity);
        adapter = new SessionsAdapter(activity, numberManager.getAllSessions());
        sort_sessions.setAdapter(adapter);
        sort_sessions.setLayoutManager(new LinearLayoutManager(activity));

        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        add.setOnClickListener(v -> {
            final AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = View.inflate(activity, R.layout.dialog_new_sorting, null);

            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int mi = calendar.get(Calendar.MINUTE);
            String session = y + "-" + m + "-" + d + " " + h + "-" + mi;

            EditText edit_session_id = view.findViewById(R.id.session_id);
            EditText edit_length = view.findViewById(R.id.length);
            EditText edit_min = view.findViewById(R.id.min);
            EditText edit_max = view.findViewById(R.id.max);
            edit_session_id.setText(session);

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Vytvořit", (dialog, which) -> {
                try {
                    int length = Integer.parseInt(edit_length.getText().toString());
                    int min = Integer.parseInt(edit_min.getText().toString());
                    int max = Integer.parseInt(edit_max.getText().toString());

                    if(length > 0 && max > min){
                        numberManager.createSession(session, true);
                        numberManager.createList(session, length, min, max);

                        adapter = new SessionsAdapter(activity, numberManager.getAllSessions());
                        sort_sessions.setAdapter(adapter);
                        sort_sessions.setLayoutManager(new LinearLayoutManager(activity));

                        alertDialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(activity, "Zadejte hodnoty", Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Zrušit", (dialog, which) -> alertDialog.dismiss());

            alertDialog.show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter = new SessionsAdapter(activity, numberManager.getAllSessions());
        sort_sessions.setAdapter(adapter);
        sort_sessions.setLayoutManager(new LinearLayoutManager(activity));
    }
}