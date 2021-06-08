package cz.janrossler.sorts.sortable;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cz.janrossler.sorts.utils.NumberManager;

public abstract class Sortable implements SortableList {
    protected boolean isSorting = false;
    protected String session;
    protected NumberManager numberManager;
    protected List<Integer> numbers;
    protected SortingListener sortingListener;
    protected Timer timer;

    public Sortable(Context context, String session){
        this.session = session;
        numberManager = new NumberManager(context);
        numbers = numberManager.getUnsortedSessionList(session);
        timer = new Timer();
    }

    public void setSortingListener(SortingListener listener){
        this.sortingListener = listener;
    }

    public SortingListener getSortingListener(){
        return this.sortingListener;
    }

    public List<Integer> getSortedList() throws Exception {
        if(isSorting) throw new Exception("The numbers are still being sorted.");
        return numbers;
    }

    public interface SortingListener {
        void onSuccessSort(int milliseconds);
        void onFailed(String _message);
    }

    public static class SortException extends Exception{
        String message;
        SortException(String error){
            message = error;
        }
    }

    /**
     * <p>
     *     Třída {@link Timer} zajišťuje zachytávání časů a jejich správu pro další užitečné operace.
     * </p>
     */
    public static class Timer {
        List<Calendar> calendars = new ArrayList<>();

        public Timer(){
            breakpoint();
        }

        /**
         * <p>
         *     Přidává nový bod času pro další zpracovávání.
         * </p>
         */

        public void breakpoint(){
            calendars.add(Calendar.getInstance());
        }

        /**
         * <p>
         *     Slouží pro získání posledního bodu času.
         * </p>
         * @return Vrátí poslední {@link Calendar}, který byl pomocí breakpoint() uložen.
         */
        public Calendar getLast(){
            return calendars.get(calendars.size() - 1);
        }

        /**
         * <p>
         *     Metoda použije poslední dva body času a odečte je v milisekundách.
         * </p>
         * @return Vrátí poslední porovnávání časů v milisekundách.
         */
        public long getLastCompare(){
            if(calendars.size() > 2)
                return calendars.get(calendars.size() - 1).getTime().getTime()
                        - calendars.get(calendars.size() - 2).getTime().getTime();
            return 0L;
        }

        /**
         * <p>
         *     Metoda použije parametry typu {@link Calendar} a odečte je v milisekundách.
         * </p>
         * @param firstCalendar Bod času, který se zaznamenal dříve.
         * @param lastCalendar Bod času, který se zaznamenal později.
         * @return Vrátí porovnávání časů v milisekundách.
         */
        public static long getCompare(@NonNull Calendar firstCalendar, @NonNull Calendar lastCalendar){
            return lastCalendar.getTime().getTime()
                    - firstCalendar.getTime().getTime();
        }

        /**
         * <p>
         *     Metoda porovná a odečte všechny zaznamenané body času a vrátí je v poli.
         * </p>
         * @return Vrátí pole všech zaznamenaných časů v milisekundách.
         */
        public long[] getAllCompares(){
            if(calendars.size() > 2){
                long[] compares = new long[calendars.size() - 1];
                for(int i = 1; i < calendars.size(); i++){
                    compares[i - 1] = Timer.getCompare(calendars.get(i - 1), calendars.get(i));
                }
                return compares;
            }
            return new long[1];
        }

        /**
         * <p>
         *     Metoda vezme první a poslední zaznamenaný bod času a odečte je v milisekundách.
         * </p>
         * @return Vrátí porovnání časů mezi prvním a posledním zaznamenaným bodem času.
         */
        public long getTotalCompare(){
            if(calendars.size() > 2)
                return Timer.getCompare(calendars.get(0), calendars.get(calendars.size() - 1));
            return 0L;
        }
    }

    @Override
    public void swap(int i, int j){
        int s = numbers.get(i);
        numbers.set(i, numbers.get(j));
        numbers.set(j, s);
    }

    @Override
    public boolean isSorted() {
        for (int i = 1; i < numbers.size(); i++)
            if (numbers.get(i) < numbers.get(i - 1))
                return false;
        return true;
    }

    @Override
    public abstract void sortNow() throws Exception;

    @Override
    public void start(){
        timer = new Timer();
        try{
            sortNow();
            timer.breakpoint();

            if(sortingListener != null) {
                if(isSorted())
                    sortingListener.onSuccessSort((int) timer.getTotalCompare());
                else {
                    if(numbers.size() <= 100) Log.i("SortedList", Arrays.toString(numbers.toArray()));
                    throw new SortException("Seznam nebyl úspěšně seřazen.");
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            if (sortingListener != null) sortingListener.onFailed("Třídění proběhlo mimo seznam.");
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            if (sortingListener != null) sortingListener.onFailed("Seznam je příliž velký na třídění.");
        }catch (SortException e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed(e.message);
        }catch (Exception e){
            e.printStackTrace();
            if(sortingListener != null) sortingListener.onFailed("Stala se neočekávaná chyba.");
        }
    }
}
