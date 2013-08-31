package org.leoche.slimeball;

import tv.ouya.console.api.CancelIgnoringOuyaResponseListener;
import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainActivity extends AndroidApplication {

	private static final String DEVELOPER_ID = "63b8ff93-14a4-4c3a-9879-bc70e46d3df3";
	public static String UUID;
	public SlimeBall slime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		OuyaController.init(this);
		OuyaFacade.getInstance().init(this, DEVELOPER_ID);
		slime = new SlimeBall(this,isOnline(),getMac());
		getUuid();
		super.onCreate(savedInstanceState);
		initialize(slime, false);
	}
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	public String getMac(){
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}
	@Override
	protected void onDestroy(){
		OuyaFacade.getInstance().shutdown();
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {}
	public void getUuid(){
		CancelIgnoringOuyaResponseListener<String> gamerUuidListener =
		        new CancelIgnoringOuyaResponseListener<String>() {
		            @Override
		            public void onSuccess(String result) {
		            	slime.setUuid(result);
		            }

		            @Override
		            public void onFailure(int errorCode, String errorMessage, Bundle errorBundle) {}
		};
		OuyaFacade.getInstance().requestGamerUuid(gamerUuidListener);
	}
}
