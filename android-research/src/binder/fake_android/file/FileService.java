package binder.fake_android.file;

import android.os.RemoteException;

public class FileService extends IFileService.Stub{

	@Override
	public String getSDRoot() throws RemoteException {
		return "/sdcard/";
	}

}
