package com.example.android_research_test_service;

import android.os.IBinder;
import android.os.RemoteException;
import binder.fake_android.file.IFileService;

public class FileManager implements IFileService{
	IFileService mRemote;

	public FileManager(IFileService mRemote) {
		this.mRemote = mRemote;
	}
	
	@Override
	public IBinder asBinder() {
		return (IBinder) mRemote;
	}

	@Override
	public String getSDRoot() throws RemoteException {
		return mRemote.getSDRoot();
	}
	
	
}
