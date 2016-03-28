package binder.fake_android;

import java.util.HashMap;
import java.util.Map;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 注意：
 * 其他的binder是给外界提供服务，但必须通过一个接口把自己的实例送出去
 * 
 * 而ServiceManager作为对外界的唯一接口，也得是个Binder，还得是个Service
 * 
 * @author qiaoliang
 *
 */
public class ServiceManager extends IServiceManager.Stub{

	private static Map<String, IBinder> map = new HashMap<String, IBinder>(); 
	
	@Override
	public void addService(String key, IBinder binder) throws RemoteException {
		map.put(key, binder);
	}

	@Override
	public IBinder getService(String key) throws RemoteException {
		return map.get(key);
	}
	
}
