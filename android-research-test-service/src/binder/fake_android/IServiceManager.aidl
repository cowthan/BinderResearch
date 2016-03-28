package binder.fake_android;

interface IServiceManager{
	void addService(String key, IBinder binder);
	IBinder getService(String key);
}