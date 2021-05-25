package cz.janrossler.sorts;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cz.janrossler.sorts.sortable.AsyncSorting;

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
                AsyncSorting asyncSorting = new AsyncSorting(this);
                asyncSorting.callbacks = activity;
                asyncSorting.sortAlgorithm = intent.getStringExtra("use-sort");
                asyncSorting.execute(intent.getStringExtra("session"));
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