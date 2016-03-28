package binder.fake_android.sensor;

public class SensorService extends ISensorService.Stub{
	
	@Override
	public float[] getAcceleratorData(){
		return new float[]{0f,1f,2f};
	}
}
