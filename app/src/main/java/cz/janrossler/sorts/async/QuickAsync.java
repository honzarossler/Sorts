package cz.janrossler.sorts.async;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.NumberManager;
import cz.janrossler.sorts.SortingService;
import cz.janrossler.sorts.sortable.QuickSort;
import cz.janrossler.sorts.sortable.Sortable;

public class QuickAsync extends AsyncTask<String, String, String> {
    private Context context;
    public SortingService.Callbacks callbacks;

    public QuickAsync(Context context){
        this.context = context;
    }

    protected String doInBackground(@NonNull String... sessions) {
        QuickSort sort = new QuickSort(context, sessions[0]);
        sort.setSortingListener(new Sortable.SortingListener() {
            @Override
            public void onSuccessSort(int seconds) {
                List<Integer> list = new ArrayList<>();
                try {
                    list = sort.getSortedList();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONArray array = new JSONArray();

                for(int i = 0; i < list.size(); i++)
                    array.put(list.get(i));

                NumberManager numberManager = new NumberManager(context);
                try {
                    numberManager.saveSortingToSession(sessions[0], Sortable.QUICK, seconds, array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                publishProgress();
            }

            @Override
            public void onFailed(String _message) {
                publishProgress();
            }
        });
        sort.start();

        return null;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        if(callbacks != null) callbacks.updateClient();
    }
}
