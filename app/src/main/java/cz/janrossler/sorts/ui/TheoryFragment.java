package cz.janrossler.sorts.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import cz.janrossler.sorts.R;
import cz.janrossler.sorts.adapter.TheoryListAdapter;
import cz.janrossler.sorts.utils.TheoryReader;

public class TheoryFragment extends Fragment {
    private Activity activity;
    private RecyclerView theories_list;

    private TheoryReader reader;
    private JSONArray theories;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        assert activity != null;

        reader = new TheoryReader(activity);
        theories = reader.getAllTheories();
        TheoryListAdapter adapter = new TheoryListAdapter(activity, theories);

        View root = activity.getLayoutInflater().inflate(R.layout.fragment_dashboard, null);
        theories_list = root.findViewById(R.id.theories);

        theories_list.setLayoutManager(new LinearLayoutManager(activity));
        theories_list.setAdapter(adapter);

        return root;
    }
}