package com.example.android_research_test_service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import binder.fake_android.IServiceManager;
import binder.fake_android.file.IFileService;

public class MainActivity extends Activity {
	private IServiceManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.bindService(new Intent("com.cowthan.servicemgmr"), new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				Log.i(G.TAG, "打开服务了! " + (arg1 != null));
				//manager = arg1;
				manager = IServiceManager.Stub.asInterface(arg1);
			}
		}, Context.BIND_AUTO_CREATE);
		
		Button btn = (Button) this.findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					IFileService fs = IFileService.Stub.asInterface(manager.getService("file"));
					Log.i(G.TAG, fs.getSDRoot());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	private Object getMyService(String key){
			try {
				if(manager == null) return null;
				
				IBinder mRemote = manager.getService(key);
				if(key.equals("file")){
					FileManager fm = new FileManager((IFileService)mRemote);
					return fm;
				}
				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		return null;
	}
	
}
