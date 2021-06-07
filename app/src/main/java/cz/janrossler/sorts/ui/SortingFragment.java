package cz.janrossler.sorts.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.Calendar;

import cz.janrossler.sorts.R;
import cz.janrossler.sorts.TreeViewActivity;
import cz.janrossler.sorts.adapter.SessionsAdapter;
import cz.janrossler.sorts.utils.NumberManager;
import cz.janrossler.sorts.utils.Utilities;

public class SortingFragment extends Fragment {
    private Activity activity;
    private RecyclerView sort_sessions;
    private LinearLayout layout_empty;

    private NumberManager numberManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        assert activity != null;
        @SuppressLint("InflateParams") View root = activity.getLayoutInflater().inflate(R.layout.fragment_home, null);
        sort_sessions = root.findViewById(R.id.sort_sessions);
        FloatingActionButton add = root.findViewById(R.id.add);
        FloatingActionButton tree = root.findViewById(R.id.tree);
        layout_empty = root.findViewById(R.id.layout_empty);
        ImageView image_animated = root.findViewById(R.id.image_animated);

        Glide.with(activity)
                .load(R.drawable.ic_sorting)
                .placeholder(R.mipmap.ic_launcher)
                .into(image_animated);

        numberManager = new NumberManager(activity);
        update();

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
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                    activity.getText(R.string.action_create),
                    (dialog, which) -> {
                try {
                    int length = Integer.parseInt(edit_length.getText().toString());
                    int min = Integer.parseInt(edit_min.getText().toString());
                    int max = Integer.parseInt(edit_max.getText().toString());

                    int max_length = Utilities.MAX_SESSION_SIZE;
                    boolean very_large = length > max_length
                            || min > max_length
                            || max > max_length;

                    if(length > 0 && max > min && !very_large){
                        numberManager.createSession(session, true);
                        numberManager.createList(session, length, min, max);

                        update();
                    }else if(very_large){
                        Toast.makeText(activity,
                                "Vaše čísla jsou moc velká pro zpracování.",
                                Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(activity,
                            activity.getText(R.string.dialog_message_enter_valid_values),
                            Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                    activity.getText(R.string.action_cancel),
                    (dialog, which) -> alertDialog.dismiss());

            alertDialog.show();
        });

        tree.setOnClickListener(v ->
                startActivity(new Intent(activity, TreeViewActivity.class)));

        return root;
    }

    private void update(){
        JSONArray sessions = numberManager.getAllSessions();

        if(sessions.length() < 1){
            layout_empty.setVisibility(View.VISIBLE);
            sort_sessions.setVisibility(View.GONE);
        }else{
            layout_empty.setVisibility(View.GONE);
            sort_sessions.setVisibility(View.VISIBLE);
        }

        SessionsAdapter adapter = new SessionsAdapter(activity, sessions);
        sort_sessions.setAdapter(adapter);
        sort_sessions.setLayoutManager(new GridLayoutManager(activity, 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }
}