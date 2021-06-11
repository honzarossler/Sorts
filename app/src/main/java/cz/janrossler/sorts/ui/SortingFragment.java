package cz.janrossler.sorts.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

@SuppressWarnings("deprecation")
public class SortingFragment extends Fragment {
    private Activity activity;
    private RecyclerView sort_sessions;
    private LinearLayout layout_empty;

    private NumberManager numberManager;
    private ProgressDialog pDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        assert activity != null;
        View root = View.inflate(activity, R.layout.fragment_home, null);
        sort_sessions = root.findViewById(R.id.sort_sessions);
        FloatingActionButton add = root.findViewById(R.id.add);
        FloatingActionButton tree = root.findViewById(R.id.tree);
        layout_empty = root.findViewById(R.id.layout_empty);
        ImageView image_animated = root.findViewById(R.id.image_animated);

        Glide.with(activity)
                .load(R.drawable.ic_sorting)
                .placeholder(R.mipmap.ic_launcher)
                .into(image_animated);

        pDialog = new ProgressDialog(activity);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage("Vytvářím novou instanci ...");

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

                    AsyncCreateSession asyncCreateSession = new AsyncCreateSession(numberManager, () -> {
                        pDialog.dismiss();
                        update();
                    });

                    pDialog.show();
                    asyncCreateSession.length = length;
                    asyncCreateSession.min = min;
                    asyncCreateSession.max = max;
                    asyncCreateSession.execute(session);
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

    @SuppressWarnings("unused")
    private static class AsyncCreateSession extends AsyncTask<String, String, Void>{
        NumberManager manager;
        OnAsync async;
        int length = 1;
        int min = 0;
        int max = 1;

        public AsyncCreateSession(NumberManager manager, OnAsync async){
            this.manager = manager;
            this.async = async;
        }

        public void setLength(int length){
            this.length = length;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public void setMax(int max) {
            this.max = max;
        }

        @Override
        protected Void doInBackground(@NonNull String... sessions) {
            for (String session : sessions) manager.createList(session, length, min, max);

            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(async != null) async.onDone();
            super.onProgressUpdate(values);
        }

        public interface OnAsync {
            void onDone();
        }
    }
}