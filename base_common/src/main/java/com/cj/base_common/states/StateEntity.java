package com.cj.base_common.states;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author:chris - jason
 * Date:2019/3/29.
 * Package:com.cj.common.states
 * 多布局页面状态实体
 */
public class StateEntity {


    //要显示的布局字典值
    @IntDef({StateCode.SUCCESS_LAYOUT, StateCode.EMPTY_LAYOUT, StateCode.PLACEHOLDER_LAYOUT, StateCode.TIMEOUT_LAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateCode {
        int SUCCESS_LAYOUT = 1;
        int EMPTY_LAYOUT = 2;
        int PLACEHOLDER_LAYOUT = 3;
        int TIMEOUT_LAYOUT = 4;
    }

    private int state;
    private int drawableRes;
    private String message;


    public StateEntity(int state) {
        this.state = state;
    }

    public StateEntity(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public StateEntity(int state, @DrawableRes int drawableRes, String message) {
        this.state = state;
        this.drawableRes = drawableRes;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
