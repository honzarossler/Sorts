package cz.janrossler.sorts.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.janrossler.sorts.NumberManager;
import cz.janrossler.sorts.SortingService;
import cz.janrossler.sorts.sortable.BogoSort;
import cz.janrossler.sorts.sortable.Sortable;

public class BogoAsync extends AsyncTask<String, String, String> {
    private Context context;
    public static ProgressDialog pDialog;
    public SortingService.Callbacks callbacks;
    private boolean isDone = false;

    public BogoAsync(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        //super.onPreExecute();
        if(pDialog != null) {
            pDialog.setMessage("Třídím čísla ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    protected String doInBackground(@NonNull String... sessions) {
        BogoSort sort = new BogoSort(context, sessions[0]);
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
                    numberManager.saveSortingToSession(sessions[0], Sortable.BOGO, seconds, array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isDone = true;
                publishProgress();
            }

            @Override
            public void onFailed(String _message) {
                isDone = true;
                publishProgress();
            }
        });
        sort.start();

        return null;
    }

    protected void onPostExecute(String str) {
        if (isDone && pDialog != null) pDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        if(callbacks != null) callbacks.updateClient();
    }
}
