# BinderResearch
模仿系统ServiceManager工作机制，在一个应用中管理一组Binder，通过ServiceManager对其他应用提供服务


-----------------------
android-research是服务器端，装到手机上后，会启动一个ServiceManagerService服务，通过此服务，可以获得一个ServiceManager的binder对象，
而这个binder中，管理着其他一系列binder，如文件服务，传感器服务，音乐服务



adnroid-reearch-test-service是客户端，通过ServiceManagerService访问了文件服务，其实就是返回一个字符串，具体看log，tag是android research