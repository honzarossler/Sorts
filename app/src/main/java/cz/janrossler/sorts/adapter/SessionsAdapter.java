package cz.janrossler.sorts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.janrossler.sorts.R;
import cz.janrossler.sorts.SessionActivity;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.Holder> {
    private Context context;
    private JSONArray sessions;

    public SessionsAdapter(Context context, JSONArray sessions){
        this.context = context;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_session_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsAdapter.Holder holder, int position) {
        try{
            JSONObject item = sessions.getJSONObject(position);

            holder.session_name.setText(item.getString("session"));
            holder.session_detail.setText("Velikost seznamu: %length%".replace("%length%", item.getString("length")));

            holder.session_more.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(context, SessionActivity.class)
                            .putExtra("session", item.getString("session"));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.fillInStackTrace();
            holder.session_name.setText("<UNKNOWN_SESSION_ERROR>");
            holder.session_detail.setText("Bez informací");
        }
    }

    @Override
    public int getItemCount() {
        return sessions.length();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView session_name;
        TextView session_detail;
        FloatingActionButton session_more;

        public Holder(@NonNull View itemView) {
            super(itemView);
            session_name = itemView.findViewById(R.id.session_name);
            session_detail = itemView.findViewById(R.id.detail);
            session_more = itemView.findViewById(R.id.fab_more);
        }
    }
}
