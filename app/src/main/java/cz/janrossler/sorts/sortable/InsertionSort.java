package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;

public class InsertionSort extends Sortable{
    public InsertionSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            for(int i = 0; i < numbers.size() - 1; i++){
                int j = i + 1;
                int tmp = numbers.get(j);
                while(j > 0 && tmp > numbers.get(j-1)){
                    numbers.set(j, numbers.get(j-1));
                    j--;
                }
                numbers.set(j, tmp);
            }

            Collections.reverse(numbers);

            endTime = Calendar.getInstance();
            Log.d("BubbleSort", "Done!");

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
