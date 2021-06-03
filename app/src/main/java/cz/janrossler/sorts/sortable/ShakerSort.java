package cz.janrossler.sorts.sortable;

import android.content.Context;

import java.util.Collections;

public class ShakerSort extends Sortable{
    public ShakerSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
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
    }
}
