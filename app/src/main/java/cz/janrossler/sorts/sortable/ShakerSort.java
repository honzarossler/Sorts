package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;

public class ShakerSort extends Sortable{
    public ShakerSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            for(int i = 0; i < numbers.size() / 2; i++){
                boolean swapped = false;

                for(int j = i; j < numbers.size() - i - 1; j++){
                    if(numbers.get(j) < numbers.get(j + 1)){
                        swap(j, j + 1);
                        swapped = true;
                    }
                }

                for(int j = numbers.size() - i - 1; j > i; j--){
                    if(numbers.get(j) > numbers.get(j - 1)){
                        swap(j, j - 1);
                        swapped = true;
                    }
                }

                if(!swapped) break;
            }

            Collections.reverse(numbers);

            endTime = Calendar.getInstance();
            Log.d("ShakerSort", "Done!");

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
