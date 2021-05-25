package cz.janrossler.sorts;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cz.janrossler.sorts.async.BogoAsync;
import cz.janrossler.sorts.async.BubbleAsync;
import cz.janrossler.sorts.async.CountingAsync;
import cz.janrossler.sorts.async.MergeAsync;
import cz.janrossler.sorts.async.QuickAsync;
import cz.janrossler.sorts.async.RadixAsync;
import cz.janrossler.sorts.sortable.Sortable;

public class SortingService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private Callbacks activity;

    public SortingService() { }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if(intent.hasExtra("use-sort") && intent.hasExtra("session")){
                switch (intent.getStringExtra("use-sort")){
                    case Sortable.BUBBLE:
                        BubbleAsync bubbleAsync = new BubbleAsync(this);
                        bubbleAsync.callbacks = activity;
                        bubbleAsync.execute(intent.getStringExtra("session"));
                        break;
                    case Sortable.COUNTING:
                        CountingAsync countingAsync = new CountingAsync(this);
                        countingAsync.callbacks = activity;
                        countingAsync.execute(intent.getStringExtra("session"));
                        break;
                    case Sortable.QUICK:
                        QuickAsync quickAsync = new QuickAsync(this);
                        quickAsync.callbacks = activity;
                        quickAsync.execute(intent.getStringExtra("session"));
                        break;
                    case Sortable.MERGE:
                        MergeAsync mergeAsync = new MergeAsync(this);
                        mergeAsync.callbacks = activity;
                        mergeAsync.execute(intent.getStringExtra("session"));
                        break;
                    case Sortable.BOGO:
                        BogoAsync bogoAsync = new BogoAsync(this);
                        bogoAsync.callbacks = activity;
                        bogoAsync.execute(intent.getStringExtra("session"));
                        break;
                    case Sortable.RADIX:
                        RadixAsync radixAsync = new RadixAsync(this);
                        radixAsync.callbacks = activity;
                        radixAsync.execute(intent.getStringExtra("session"));
                        break;
                    default:
                        Log.d("SortingService", "Unknown sort " + intent.getStringExtra("use-sort"));
                        break;
                }
            }
        }

        return START_NOT_STICKY;
    }

    public class LocalBinder extends Binder {
        public SortingService getServiceInstance(){
            return SortingService.this;
        }
    }

    public void registerClient(Activity activity){
        this.activity = (Callbacks) activity;
    }

    public interface Callbacks{
        void updateClient();
    }
}