package cn.sh.changxing.bindert.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.IntDef;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sh.changxing.yuanyi.logger.LoggerFactory;

public class MyService extends Service {
    private MyBinder myBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().endMethod();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().endMethod();
    }

    @Override
    public void onCreate() {
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().endMethod();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int eventId = 0;
            @Override
            public void run() {
                myBinder.dispatchEvent(eventId);
                eventId++;
            }
        }, 2000, 3000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().endMethod();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoggerFactory.getDefault().beginMethod();
        LoggerFactory.getDefault().endMethod();
    }

    public class MyBinder extends IPersonManager.Stub {
        private RemoteCallbackList<IClientCallback> callbacks = new RemoteCallbackList<>();

        @Override
        public int join(Person person) throws RemoteException {
            LoggerFactory.getDefault().beginMethod();
            LoggerFactory.getDefault().d("thread is {}", Thread.currentThread().getName());
            LoggerFactory.getDefault().d("person is {}", person);
            LoggerFactory.getDefault().endMethod();
            return 0;
        }

        @Override
        public void leave() throws RemoteException {
            LoggerFactory.getDefault().beginMethod();
            LoggerFactory.getDefault().d("thread is {}", Thread.currentThread().getName());
            LoggerFactory.getDefault().endMethod();
        }

        @Override
        public void setClientCallback(IClientCallback callback) throws RemoteException {
            LoggerFactory.getDefault().beginMethod();
            callbacks.register(callback);
            LoggerFactory.getDefault().endMethod();
        }

        private void dispatchEvent(int eventId) {
            callbacks.beginBroadcast();
            for (int i = 0; i < callbacks.getRegisteredCallbackCount(); i++) {
                IClientCallback callback = callbacks.getBroadcastItem(i);
                try {
                    callback.onEvent(eventId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            callbacks.finishBroadcast();
        }
    }
}
