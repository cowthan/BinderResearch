package binder.service.client.oringinal;

import com.cowthan.global.G;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class MusicPlayerService extends Binder{
	
	/**
	 * 本方法被客户端调用，
	 * code： 想要调用的方法的代码，需要约定
	 * data: 方法的参数以这种形式传过来，如何读取，需要约定
	 * reply:返回值，需要约定
	 * flag：IPC调用模式，0表示服务器端执行完后返回一定数据，1表示不返回
	 */
	@Override
	protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
			throws RemoteException {

		switch(code){
		case 1000:
			//===调用start
			data.enforceInterface("MusicPlayerService"); //某种校验，与writeInterfaceToken()对应
			String filePath = data.readString(); //读出第一个string
			start(filePath); // 调用本地方法
			break;
		case 1001:
			//===调用stop
			stop();
			break;
		
		}
		
		return super.onTransact(code, data, reply, flags);
	}
	
	//========== 业务逻辑方法 ===========//
	public void start(String path){
		Log.i(G.TAG, "music start: " + path);
	}
	
	public void stop(){
		System.out.println("music stop");
	}
}
