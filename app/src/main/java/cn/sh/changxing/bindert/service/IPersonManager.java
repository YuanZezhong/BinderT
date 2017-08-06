package cn.sh.changxing.bindert.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPersonManager extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends Binder implements IPersonManager {
        private static final String DESCRIPTOR = "cn.sh.changxing.bindert.service.IPersonManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an cn.sh.changxing.bindert.service.IPersonManager interface,
         * generating a proxy if needed.
         */
        public static IPersonManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof IPersonManager))) {
                return ((IPersonManager) iin);
            }
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_join: {
                    data.enforceInterface(DESCRIPTOR);
                    Person person;
                    if ((0 != data.readInt())) {
                        person = Person.CREATOR.createFromParcel(data);
                    } else {
                        person = null;
                    }
                    int result = this.join(person);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
                case TRANSACTION_leave: {
                    data.enforceInterface(DESCRIPTOR);
                    this.leave();
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_setClientCallback: {
                    data.enforceInterface(DESCRIPTOR);
                    IClientCallback callback;
                    callback = IClientCallback.Stub.asInterface(data.readStrongBinder());
                    this.setClientCallback(callback);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements IPersonManager {
            private android.os.IBinder mRemote;

            Proxy(IBinder remote) {
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public int join(Person person) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                int result;
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if ((person != null)) {
                        data.writeInt(1);
                        person.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_join, data, reply, 0);
                    reply.readException();
                    result = reply.readInt();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
                return result;
            }

            @Override
            public void leave() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_leave, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setClientCallback(IClientCallback callback) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
                    mRemote.transact(Stub.TRANSACTION_setClientCallback, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }
        }

        static final int TRANSACTION_join = IBinder.FIRST_CALL_TRANSACTION + 0;
        static final int TRANSACTION_leave = IBinder.FIRST_CALL_TRANSACTION + 1;
        static final int TRANSACTION_setClientCallback = IBinder.FIRST_CALL_TRANSACTION + 2;
    }

    public int join(Person person) throws RemoteException;

    public void leave() throws RemoteException;

    public void setClientCallback(IClientCallback callback) throws RemoteException;
}
