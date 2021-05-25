package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class CountingSort extends Sortable{
    public CountingSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();
        Log.d("CountingSort", "Starting!");

        int min_key = numbers.get(0);
        int max_key = numbers.get(0);

        try {
            for (int i = 0; i < numbers.size(); i++) {
                if(numbers.get(i) < min_key)
                    min_key = numbers.get(i);
                if(numbers.get(i) > max_key)
                    max_key = numbers.get(i);
            }

            int maxc = max_key - min_key + 1;
            int[] c = new int[maxc];

            for(int i = 0; i < numbers.size(); i++)
                c[numbers.get(i) - min_key] = c[numbers.get(i) - min_key] + 1;

            for(int i = 1; i < c.length; i++)
                c[i] = c[i] + c[i - 1];

            int[] q = new int[numbers.size() + 1];

            for (int i = numbers.size() - 1; i > -1; i--)
            {
                q[c[numbers.get(i) - min_key]] = numbers.get(i);
                c[numbers.get(i) - min_key]--;
            }

            Log.d("CountingSort", "Finishing!");

            for(int i = 0; i < numbers.size(); i++)
            {
                numbers.set(i, q[i]);
            }

            endTime = Calendar.getInstance();
            Log.d("CountingSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }
}
