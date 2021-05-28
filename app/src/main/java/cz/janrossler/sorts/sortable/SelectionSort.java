package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class SelectionSort extends Sortable{
    public SelectionSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int size = numbers.size();

            for (int i = 0; i < size - 1; i++) {
                int min_index = i;
                for (int j = i + 1; j < size; j++) {
                    if (numbers.get(j) < numbers.get(min_index))
                        min_index = j;
                }

                int nSwipe = numbers.get(min_index);
                numbers.set(min_index, numbers.get(i));
                numbers.set(i, nSwipe);
            }

            endTime = Calendar.getInstance();
            Log.d("SelectionSort", "Done!");

            if(sortingListener != null) {
                int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                sortingListener.onSuccessSort(time);
            }
        }catch (Exception e){
            e.fillInStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }
}
