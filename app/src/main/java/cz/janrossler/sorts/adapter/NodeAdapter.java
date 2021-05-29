package cz.janrossler.sorts.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.R;
import cz.janrossler.sorts.utils.Node;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.Holder> {
    private List<Node> nodes;
    private Context context;

    public NodeAdapter(Context context, List<Node> nodes){
        this.context = context;
        this.nodes = nodes;
    }

    @NonNull
    @Override
    public NodeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_node, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new NodeAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeAdapter.Holder holder, int position) {
        Node root = nodes.get(position);
        List<Node> inside_nodes = new ArrayList<>();
        if(root.left != null) inside_nodes.add(root.left);
        if(root.right != null) inside_nodes.add(root.right);

        holder.value.setText(String.valueOf(root.getValue()));
        holder.bar_bottom.setVisibility(position + 1 == getItemCount() ? View.GONE : View.VISIBLE);
        holder.bar_inside.setVisibility(inside_nodes.size() > 0 ? View.VISIBLE : View.GONE);

        NodeAdapter adapter = new NodeAdapter(context, inside_nodes);
        holder.recycler_inside.setLayoutManager(new GridLayoutManager(context, 1));
        holder.recycler_inside.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return nodes.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView value;
        View bar_bottom;
        View bar_inside;
        RecyclerView recycler_inside;
        public Holder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.value);
            bar_bottom = itemView.findViewById(R.id.bar_bottom);
            bar_inside = itemView.findViewById(R.id.bar_inside);
            recycler_inside = itemView.findViewById(R.id.recycler_inside);
        }
    }
}
