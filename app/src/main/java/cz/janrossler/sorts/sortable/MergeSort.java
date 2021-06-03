package cz.janrossler.sorts.sortable;

import android.content.Context;

import androidx.annotation.NonNull;

public class MergeSort extends Sortable{
    public MergeSort(Context context, String session) {
        super(context, session);
    }

    @Override
    public void start() throws Exception {
        int[] list = new int[numbers.size()];

        for(int i = 0; i < numbers.size(); i++)
            list[i] = numbers.get(i);

        sort(list);

        for(int j = 0; j < list.length; j++)
            numbers.set(j, list[j]);
    }

    public void merging(@NonNull int[] list, @NonNull int[] left, int[] right){
        int i = 0;
        int j = 0;

        while ((i < left.length) && (j < right.length)) {
            if (left[i] < right[j]) {
                list[i + j] = left[i];
                i++;
            }
            else {
                list[i + j] = right[j];
                j++;
            }
        }

        if (i < left.length) {
            while (i < left.length) {
                list[i + j] = left[i];
                i++;
            }
        }
        else {
            while (j < right.length) {
                list[i + j] = right[j];
                j++;
            }
        }
    }

    public void sort(@NonNull int[] list) {
        if (list.length <= 1)
            return;

        int center = list.length / 2;
        int[] left = new int[center];

        System.arraycopy(list, 0, left, 0, center);

        int[] right = new int[list.length - center];

        if (list.length - center >= 0)
            System.arraycopy(list, center, right, 0, list.length - center);

        sort(left);
        sort(right);
        merging(list, left, right);
    }
}
