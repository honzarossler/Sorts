package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import cz.janrossler.sorts.utils.Utilities;

public class GravitySort extends Sortable{
    public GravitySort(Context context, String session) {
        super(context, session);
    }

    /**
     * <p>
     *     Tento sort je experimentální. V reálném světě dokáže být rychlý O(1), ale v programování
     *     to může být v nejlepším případě O(n<sup>2</sup>), jinak může být rychlost O(n!).
     * </p>
     */

    @Override
    public void start() {
        startTime = Calendar.getInstance();

        try {
            int max = numbers.get(0);
            if(Utilities.MAX_GRAVITY_ALLOC >= numbers.size() * 8 * max){
                for(int i = 1; i < numbers.size(); i++)
                    if(numbers.get(i) > max)
                        max = numbers.get(i);

                //Set up abacus
                char[][] grid = new char[numbers.size()][max];
                int[] levelcount = new int[max];
                for(int i = 0; i < max; i++)
                {
                    levelcount[i] = 0;
                    for(int j = 0; j < numbers.size(); j++)
                        grid[j][i] = '_';
                }

                //Drop the beads
                for(int i = 0; i < numbers.size(); i++)
                {
                    int num = numbers.get(i);
                    for(int j = 0; num > 0; j++)
                    {
                        grid[levelcount[j]++][j] = '*';
                        num--;
                    }
                }

                //Count the beads
                for(int i = 0; i < numbers.size(); i++)
                {
                    int putt = 0;
                    for(int j = 0; j < max && grid[numbers.size() - 1 - i][j] == '*';j++)
                        putt++;
                    numbers.set(i, putt);
                }

                endTime = Calendar.getInstance();
                Log.d("GravitySort", "Done!");

                if(sortingListener != null) {
                    int time = (int) ((endTime.getTime().getTime() - startTime.getTime().getTime()) / 1000);
                    sortingListener.onSuccessSort(time);
                }
            }else{
                if(sortingListener != null) sortingListener.onFailed("Objekt je příliš veliký.");
            }
        }catch (Exception | OutOfMemoryError e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.getMessage());
        }
    }
}
