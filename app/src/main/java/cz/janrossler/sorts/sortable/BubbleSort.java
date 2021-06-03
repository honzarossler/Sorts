package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class BubbleSort extends Sortable{
    public BubbleSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            for (int i = 0; i < numbers.size() - 1; i++) {
                for (int j = 0; j < numbers.size() - i - 1; j++) {
                    if (numbers.get(j + 1) < numbers.get(j)) {
                        swap(j, j + 1);
                    }
                }
            }

            endTime = Calendar.getInstance();
            Log.d("BubbleSort", "Done!");

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
