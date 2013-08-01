package com.jcertif.android.fragments;


import com.actionbarsherlock.app.SherlockFragment;
import com.jcertif.android.service.RESTService;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * 
 * @author Patrick Bashizi
 *
 */
public abstract class RESTResponderFragment  extends SherlockFragment {
    
    private ResultReceiver mReceiver;
  
    public RESTResponderFragment() {
    	
        mReceiver = new ResultReceiver(new Handler()) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultData != null && resultData.containsKey(RESTService.REST_RESULT)) {
                    onRESTResult(resultCode, resultData.getString(RESTService.REST_RESULT));
                }
                else {
                    onRESTResult(resultCode, null);
                }
            }
            
        };
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    
    public ResultReceiver getResultReceiver() {
        return mReceiver;
    }

    // Implementers of this Fragment will handle the result here.
    abstract public void onRESTResult(int code, String result);
    
    protected void setLoading(boolean state){
    	getSherlockActivity()
		.setSupportProgressBarIndeterminateVisibility(state);
    }
}
