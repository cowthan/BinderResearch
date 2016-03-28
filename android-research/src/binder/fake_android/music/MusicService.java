package binder.fake_android.music;

import com.cowthan.global.G;

import android.os.RemoteException;
import android.util.Log;

/**
 * Stub是一个Binder，并且在其transact的实现中，管的是code和具体方法的对应，参数的读取和封装
 * 而在Proxy的实现中，在调用每个方法的实现中，都转换为了使用具体code对trasact的调用
 * Proxy则把参数封装起来，给tracact用
 * 
 * 就这样分工合作
 * 
 * @author qiaoliang
 *
 */
public class MusicService extends IMusicService.Stub{

	@Override
	public boolean start(String path) throws RemoteException {
		Log.i(G.TAG, "start: " + path);
		return true;
	}

	@Override
	public void stop() throws RemoteException {
		Log.i(G.TAG, "stop");
	}

} 
