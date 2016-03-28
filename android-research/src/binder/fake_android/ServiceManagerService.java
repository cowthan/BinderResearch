package binder.fake_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceManagerService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return new ServiceManager();
	}
}
