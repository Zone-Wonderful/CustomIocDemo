package com.zone.ioc;

import android.app.Activity;
import android.view.View;

/**
 * @Date: 2017/4/1.
 * @Author: Zone-Wonderful
 * @Description: View 的findViewById的辅助类
 */
public class ViewFinder {

    private Activity mActivity;
    private View mView;


    public ViewFinder(Activity activity) {
        this.mActivity = activity;

    }

    public ViewFinder(View view) {
        this.mView = view;
    }
    public  View findViewById(int viewId){
        return  mActivity!=null?mActivity.findViewById(viewId):mView.findViewById(viewId);

    }
}
