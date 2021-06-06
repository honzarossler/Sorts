package cz.janrossler.sorts.sortable;

import android.content.Context;

import java.util.Collections;

public class ShellSort extends Sortable{
    public ShellSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
        int gap = numbers.size() / 2;
        while(gap > 0){
            for (int i = 0; i < numbers.size() - gap; i++){
                int j = i + gap;
                int tmp = numbers.get(j);
                while (j >= gap && tmp > numbers.get(j - gap)){
                    numbers.set(j, numbers.get(j - gap));
                    j -= gap;
                }
                numbers.set(j, tmp);
            }

            if(gap == 2) gap--;
            else gap /= 2.2;
        }
        Collections.reverse(numbers);
    }
}
