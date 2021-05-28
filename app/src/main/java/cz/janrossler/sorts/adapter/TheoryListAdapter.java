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
import cz.janrossler.sorts.TheoryActivity;

public class TheoryListAdapter extends RecyclerView.Adapter<TheoryListAdapter.Holder> {
    private Context context;
    private JSONArray theories;

    public TheoryListAdapter(Context context, JSONArray theories){
        this.context = context;
        this.theories = theories;
    }

    @NonNull
    @Override
    public TheoryListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_theory_sort, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TheoryListAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheoryListAdapter.Holder holder, int position) {
        try{
            JSONObject item = theories.getJSONObject(position);

            holder._name.setText(item.getString("name"));

            holder.session_more.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(context, TheoryActivity.class)
                            .putExtra("theory", item.getString("theory"));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.fillInStackTrace();
            holder._name.setText("<UNKNOWN_THEORY_ERROR>");
        }
    }

    @Override
    public int getItemCount() {
        return theories.length();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView _name;
        FloatingActionButton session_more;

        public Holder(@NonNull View itemView) {
            super(itemView);
            _name = itemView.findViewById(R.id._name);
            session_more = itemView.findViewById(R.id.fab_more);
        }
    }
}
