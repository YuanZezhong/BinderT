package cn.sh.changxing.bindert.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.sh.changxing.bindert.R;
import cn.sh.changxing.bindert.service.IClientCallback;
import cn.sh.changxing.bindert.service.IPersonManager;
import cn.sh.changxing.bindert.service.MyService;
import cn.sh.changxing.bindert.service.Person;
import cn.sh.changxing.yuanyi.logger.LoggerFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn;
    private ServiceConnection serviceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoggerFactory.getDefault().beginMethod();
            iPersonManager = IPersonManager.Stub.asInterface(service);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LoggerFactory.getDefault().beginMethod();
            iPersonManager = null;
        }
    };;

    private IPersonManager iPersonManager;
    private Button join;
    private Button leave;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        registerListener();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn_binder_service);
        join = ((Button) findViewById(R.id.btn_join));
        leave = ((Button) findViewById(R.id.btn_leave));
        register = ((Button) findViewById(R.id.btn_register));
    }

    private void registerListener() {
        btn.setOnClickListener(this);
        join.setOnClickListener(this);
        leave.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    public void bindService(){
       bindService(new Intent(this, MyService.class),serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_binder_service:
                bindService();
                break;
            case R.id.btn_join:
                Person person = new Person();
                person.setName("Yuan");
                person.setAge(2);
                try {
                    iPersonManager.join(person);
                } catch (RemoteException e) {
                    LoggerFactory.getDefault().e(e);
                }
                break;
            case R.id.btn_leave:
                try {
                    iPersonManager.leave();
                } catch (RemoteException e) {
                    LoggerFactory.getDefault().e(e);
                }
                break;
            case R.id.btn_register:
                try {
                    iPersonManager.setClientCallback(new MyCallback());
                } catch (RemoteException e) {
                    LoggerFactory.getDefault().e(e);
                }
                break;
        }
    }

    public class MyCallback extends IClientCallback.Stub {

        @Override
        public void onEvent(int eventId) throws RemoteException {
            LoggerFactory.getDefault().beginMethod();
            LoggerFactory.getDefault().d("onEvent called and eventId is {}, thread is {}", eventId, Thread.currentThread().getName());
            LoggerFactory.getDefault().endMethod();
        }
    }
}
