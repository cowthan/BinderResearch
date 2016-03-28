package com.cowthan.android_research;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import binder.fake_android.ServiceManager;
import binder.fake_android.file.FileService;
import binder.fake_android.music.MusicService;
import binder.fake_android.sensor.SensorService;
import binder.service.client.oringinal.MusicPlayerService;

import com.cowthan.global.G;
/**
 * 所以说Binder和Service的关系是这样的：
 * 1、Service可以单独工作，但是当它要和其他组件交互时，就需要传递
 * 一个对象，这个对象就只能通过Binder来实现
 * 
 * 2、Binder可以直接new一个出来，但没什么意义，要提供给别的进程
 * 服务，就只能通过service提供出去
 * 
 * 
 * @author qiaoliang
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//test_binder_oringinal();
		
		test_service_mgmr();
		ServiceManager sm = new ServiceManager();  //可以直接new一个Binder，但这没有意义，因为Binder就是为了给外部提供服务
		try {
			FileService fs = (FileService) sm.getService("file");
			Log.i(G.TAG, fs.getSDRoot());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 看下面这个方法，就发现使用binder需要解决两个大问题：
	 * 1、客户端如何获得服务端对象
	 * 2、客户端和服务端必须事先约定好两件事：
	 * ——服务端不同的方法对应不同的标志，即code
	 * ——服务端方法的参数放在Parcel中的顺序，及返回结果中的顺序
	 * 
	 * 还有其他发现：
	 * 1、服务端提供的方法，传入的参数和返回的类型必须都是Parcel的
	 * 
	 * 解决第一个问题：
	 * ——先考虑为何使用Binder，是为了提供全局的服务，使任何程序都能访问到，
	 * 所以提供binder对象就是操作系统的问题了，安卓采用的是service，
	 * 		——实际上binder和service并不是必须绑定的，不用service也可以
	 * 		使用binder，具体来说：
	 * 		——可以仅使用Binder类来扩展系统服务，最后把自己的实例add给ServiceManager，通过
	 * 			Context.getSystemService()来得到
	 * 		——对于应用程序自己提供的自定义服务，必须使用service了
	 * ——安卓的Service提供了：
	 * 		——startService(Intent intent)
	 * 		——bindService(Intent intent, ServiceConnection conn, int flag)
	 * 		如你所知，绑定之后就可以使用服务中返回的Binder对象了
	 * 
	 * ==Service端：
	 * public class MusicService extends Service{
	 * 		
	 * 		....
	 * 		void onBind(){
	 * 			return new MusicPlayerService();
	 * 		}
	 * 
	 * }
	 * 
	 * ==Client端：
	 * class MyServiceConnection implements ServiceConnection{
	 * 		....
	 * 		public void onServiceConnected(ComponentName name, IBinder binder){
	 * 			//如你所知，binder就是Service返回的binder
	 * 		}
	 * }
	 * 
	 * 具体过程：
	 * 1、Service正常启动之后，Ams会远程调用 ActivityThread中的ApplicationThread
	 * 对象（Ams.scheduleBindService(applicationThread, binder)），onBinder()返
	 * 回的binder的引用就会传给ApplicationThread
	 * 2、ApplicationThread会回调onServiceConnected接口
	 * 
	 * 
	 * 解决第二个问题：aidl
	 * 
	 * 
	 */
	private void test_binder_oringinal() {
		
		//===得到binder
		IBinder remote = null;
		remote = new MusicPlayerService();
		
		//===构造输入参数
		String filePath = "/sdcard/xxxx/aaa.mp3";
		int code = 1000;
		Parcel data = Parcel.obtain();
		data.writeInterfaceToken("MusicPlayerService");
		data.writeString(filePath);
		
		//===准备接受返回结果：
		Parcel reply = Parcel.obtain();
		
		//===通过接口调用：
		try {
			remote.transact(code, data, reply, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//===分析返回结果：
		IBinder binder = reply.readStrongBinder();
		Log.i(G.TAG, binder.toString());
		
		//===收尾
		reply.recycle();
		data.recycle();
	}

	public void test_service_mgmr(){
		ServiceManager sm = new ServiceManager();
		try {
			sm.addService("music", new MusicService());
			sm.addService("file", new FileService());
			sm.addService("sensor", new SensorService());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
