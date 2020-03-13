/**
* This class serves as a JNI interface. It contains
* one native method 'C_GetLocalTime' that computes
* and return an object containing local time. 
* @author wmabebe
*/
public class JNIClass {
	public native Object C_GetLocalTime(Object cmdObj);

	static{
		System.loadLibrary("jniclass");
	}

}
