package cz.janrossler.sorts.sortable;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort extends Sortable{
    public BucketSort(Context context, String session) {
        super(context, session);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sortNow() throws Exception {
        int bucketCount = numbers.size();

        if(bucketCount <= 1) return;

        int high = numbers.get(0);
        int low = numbers.get(0);

        for(int i = 0; i < numbers.size(); i++){
            if(numbers.get(i) > high) high = numbers.get(i);
            if(numbers.get(i) < low) low = numbers.get(i);
        }

        double interval = ((double)(high - low + 1))/bucketCount;
        List<Integer>[] buckets = new ArrayList[bucketCount];
        for (int i = 0; i < bucketCount; i++){
            buckets[i] = new ArrayList<>();
        }

        for(int i = 0; i < numbers.size(); i++){
            buckets[(int)((numbers.get(i) - low)/interval)].add(numbers.get(i));
        }

        int pointer = 0;
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int j = 0; j < bucket.size(); j++) {
                numbers.set(pointer, bucket.get(j));
                pointer++;
            }
        }
    }
}
