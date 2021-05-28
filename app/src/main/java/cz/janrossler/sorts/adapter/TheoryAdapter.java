package cz.janrossler.sorts.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.janrossler.sorts.R;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.Holder> {
    private Context context;
    private JSONArray theories;
    final Markwon markwon;

    public TheoryAdapter(Context context, JSONArray theories){
        this.context = context;
        this.theories = theories;
        markwon = Markwon.builder(context)
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(JLatexMathPlugin.create(20 * context.getResources().getDisplayMetrics().density, builder -> {
                    builder.inlinesEnabled(true);
                }))
                .build();
    }

    @NonNull
    @Override
    public TheoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.theory, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new TheoryAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheoryAdapter.Holder holder, int position) {
        try{
            JSONObject item = theories.getJSONObject(position);

            if(item.has("text")){
                holder._text.setVisibility(View.VISIBLE);
                markwon.setMarkdown(holder._text, item.getString("text"));
            }else holder._text.setVisibility(View.GONE);

            if(item.has("image")){
                holder._image.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Uri.parse("file:///android_asset/"+item.getString("image")))
                        .into(holder._image);
            }else holder._image.setVisibility(View.GONE);
        }catch (Exception e){
            e.fillInStackTrace();
            holder._text.setVisibility(View.GONE);
            holder._image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return theories.length();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView _text;
        ImageView _image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            _text = itemView.findViewById(R.id._text);
            _image = itemView.findViewById(R.id._image);
        }
    }
}
