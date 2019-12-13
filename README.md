# advmeds-bluetooth-lib
advmeds-bluetooth-lib

使用方法:

1.Add it in your root build.gradle at the end of repositories:


    allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
        }
    }
    
2.Add the dependency

	dependencies {
	        implementation 'com.github.advmeds-service:advmeds-bluetooth-lib:version'
	}
  
3.Create and Use

	{
      //initial
  
	    BaseBtDevice baseBtDevice = BaseBtDeviceFactory.createBtDevice("DeviceName");
      
        baseBtDevice.setCallBack(BaseBtCallBack)
      
        //when found bluetooth device
      
        baseBtDevice.startConnect(Context, BluetoothDevice);
	}
