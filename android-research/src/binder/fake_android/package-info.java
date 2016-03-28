/**
 * 这里是要写一个这样的子系统，以模仿安卓系统管理和维护所有系统服务的方式：
 * ——我们的这个应用打算提供N多个Binder，给外部程序使用，具体包括：
 * 		——music manage, sdcard manage, wifi manage, alarm manage，sensor manage
 * 
 * ——我们提供服务，外部程序直接使用就行
 * 
 * ——为了模仿ServiceManager的统一管理，我们也设计一个ServiceManager类，
 * 这个类本身也是个Service，在安卓中，其binder可以通过BinderInternal.
 * getContextObject()来获得，而这个binder就管理了所有系统服务，
 * getSystemService就是通过它实现的
 * 
 * 
 * 基本机制是这样的，对于安卓系统来说，它提供了N多个Binder来提供系统服务，
 * 如InputManage， SensorManage， WifiManage，AlarmManage等，这些Binder
 * 的对象可以都通过Service传给客户端，但是不如在服务端再开一个Binder，这个
 * Binder就是ServiceManager，它的功能就是管理这所有Binder
 * 所以：
 * ——系统提供一个ServiceManager的Binder对象，肯定是通过某个Service给客户端的，
 * 在客户端通过BinderInternal.getContextObject()来获得这个binder
 * 
 * ——内核中的Binder驱动也维护了一个ServiceManager的binder对象，但是这个不会
 * 单独开一个线程，服务端的那个才开
 * 
 * ——客户端拿到ServiceManager之后，本来可以直接使用了，但是安卓sdk又做了一层
 * 封装，就是这些个Manager类，可以想象，这些Manager是依赖于各自对应的binder
 * 才能工作的，作用就是封装一下binder机制，对用户透明
 * 
 * ——所以:
 * 	 ——ContextImpl.getSystemService(NAME)是去得到这些Manager类，逻辑是：
 * 			if(name == "input-manager"){
 * 				return InputManager.getInstance();  //单例模式
 * 			}
 * 
 * 	 ——而对于sdk的类InputManager，它的getInstance()逻辑如下：
 * 			同步，懒汉式的单例省略，就看如何获得binder对象：
 * 			IBinder b = ServiceManager.getService(Context.INPUT_METHOD_SERVICE);
 * 			IInputMethodManager service = IInputMethodManager.Stub.asInterface(b);
 * 			return new InputMethodManager(service, mainLooper);
 * 			——注意最后一句，用binder构造了客户端的InputMethodManager实例
 * 
 * 	 ——而对于ServiceManager.getService(name)：
 * 			——里面有个缓存机制，用name存各个binder，
 * 			——如果缓存中没有，则：
 * 			return getIServiceManager().getService(name);
 * 
 * 	 ——对于getIServiceManager()：
 * 			return ServiceManagerNative.asInterface(BinderInternal.getContextObject());
 * 
 * 	 ——转了一大圈，最后一切还是取决于：BinderInternal.getContextObject()
 * 
 * 
 * 总结：
 * ——InputMethodManager是客户端API，直接给你使用的，你知道
 * ——InputMethodManager封装了IInputMethodManager，这是IBinder的interface
 * ——这可以看做一种代理关系，类似的Ams的代理就是ActivityManager
 * 
 * 
 * 
 */
package binder.fake_android;