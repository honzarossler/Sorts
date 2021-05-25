package cz.janrossler.sorts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import cz.janrossler.sorts.R;

public class SessionPreviewAdapter extends RecyclerView.Adapter<SessionPreviewAdapter.Holder> {
    private JSONArray unsorted;
    private JSONArray sorted;
    private Context context;

    public SessionPreviewAdapter(Context context, JSONArray unsorted){
        this.context = context;
        this.unsorted = unsorted;
        this.sorted = new JSONArray();
    }

    public SessionPreviewAdapter(Context context, JSONArray unsorted, JSONArray sorted){
        this.context = context;
        this.unsorted = unsorted;
        this.sorted = sorted;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_session_preview, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new SessionPreviewAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try{
            holder.unsorted.setText(
                    unsorted.length() > position
                            ? String.valueOf(unsorted.getInt(position)) : "");
            holder.sorted.setText(
                    sorted.length() > position
                            ? String.valueOf(sorted.getInt(position)) : "");

            holder.unsorted.setBackgroundColor(Color.parseColor(position % 2 > 0 ? "#F7F7F7" : "#E3E3E3"));
            holder.sorted.setBackgroundColor(Color.parseColor(position % 2 > 0 ? "#F7F7F7" : "#E3E3E3"));
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(sorted.length(), unsorted.length());
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView sorted;
        TextView unsorted;

        public Holder(@NonNull View itemView) {
            super(itemView);

            sorted = itemView.findViewById(R.id.sorted);
            unsorted = itemView.findViewById(R.id.unsorted);
        }
    }
}
