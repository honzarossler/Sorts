package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class RadixSort extends Sortable{
    public RadixSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int size = numbers.size();
            int max = numbers.get(0);
            for (int i = 1; i < size; i++)
                if (numbers.get(i) > max)
                    max = numbers.get(i);

            for (int place = 1; 0 < (max / place); place *= 10){
                int[] output = new int[size + 1];

                for (int i = 1; i < size; i++) {
                    if (numbers.get(i) > max)
                        max = numbers.get(i);
                }

                int[] count = new int[max + 1];

                for (int i = 0; i < max; ++i)
                    count[i] = 0;

                // Calculate count of elements
                for (int i = 0; i < size; i++)
                    count[(numbers.get(i) / place) % 10]++;

                // Calculate cumulative count
                for (int i = 1; i < 10; i++)
                    count[i] += count[i - 1];

                // Place the elements in sorted order
                for (int i = size - 1; i >= 0; i--) {
                    output[count[(numbers.get(i) / place) % 10] - 1] = numbers.get(i);
                    count[(numbers.get(i) / place) % 10]--;
                }

                for (int i = 0; i < size; i++)
                    numbers.set(i, output[i]);
            }

            endTime = Calendar.getInstance();
            Log.d("RadixSort", "Done!");

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
