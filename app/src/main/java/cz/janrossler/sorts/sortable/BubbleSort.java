package cz.janrossler.sorts.sortable;

import android.content.Context;

public class BubbleSort extends Sortable{
    public BubbleSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void sortNow() throws Exception {
        for (int i = 0; i < numbers.size() - 1; i++) {
            for (int j = 0; j < numbers.size() - i - 1; j++) {
                if (numbers.get(j + 1) < numbers.get(j)) {
                    swap(j, j + 1);
                }
            }
        }
    }
}
