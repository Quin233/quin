package com.schrondinger.quin.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * Created by HP on 2018/1/19.
 */

public class SchrodingerDialog {
    /**
     * 弹出对话框提示信息(有确认回调，有取消回调)
     * @param activity
     * @param title	对话框标题
     * @param message
     */
    public static void alertDialog(Context activity,String title, String message, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2 ){
        new UserDefinedDialog(activity, title, message, onClickListener1, onClickListener2).show();
    }

    /**
     * 弹出对话框提示信息(有确认回调)
     * @param activity
     * @param title	对话框标题
     * @param message
     */
    public static void alertDialog(Context activity, String title, String message,View.OnClickListener onClickListener ){
        new UserDefinedDialog(activity, title, message, onClickListener, null).show();
    }

    /**
     * 弹出对话框提示信息(无回调)
     * @param activity
     * @param title	对话框标题
     * @param message
     */
    public static void alertDialog(Context activity, String title, String message){
        new UserDefinedDialog(activity, title, message, null, null).show();
    }

    /**
     * 确认对话框(有确认回调，有取消回调)
     * @param activity 当前activity
     * @param title	对话框标题
     * @param message	要显示的信息
     * @param okCallback	点击确认的回调函数
     * @param cancelCallback 点击取消的回调函数
     */
    public static void confirmDialog(Context activity, String title, String message, final View.OnClickListener okCallback, final View.OnClickListener cancelCallback){
        new UserDefinedDialog(activity, title, message, okCallback, cancelCallback).show();
    }

    /**
     * 确认对话框(有确认回调，有取消回调)
     * @param activity 当前activity
     * @param title	对话框标题
     * @param message 要显示的信息
     * @param okCallback 点击确认的回调函数
     * @param cancelCallback 点击取消的回调函数
     * @param left
     * @param right
     */
    public static void confirmDialog(Context activity, String title, String message, View.OnClickListener okCallback, View.OnClickListener cancelCallback, String left, String right){
        new UserDefinedDialog(activity, title, message, okCallback, cancelCallback,left,right).show();
    }

    /**
     * 错误警示框（无回调）
     * @param activity 当前activity
     * @param title	对话框标题
     * @param message 要显示的信息
     */
    public static void showError(Activity activity, String title, String message){
        alertDialog(activity, title, message);
    }

    /**
     * 错误警示框（有回调）
     * @param activity 当前activity
     * @param title	对话框标题
     * @param message 要显示的信息
     * @param onClickListener 点击按钮的回调
     */
    public static void showError(Activity activity, String title, String message,View.OnClickListener onClickListener){
        alertDialog(activity,title, message,onClickListener);
    }

    /**
     * 申请权限提示框
     * @param activity 当前activity
     * @param onClickListener 确认按钮的回调
     */
    public static void showPermissionDialog(Activity activity,View.OnClickListener onClickListener){
        new UserPermissionDialog(activity, onClickListener).show();
    }

    public static boolean setComponentField = false;// 设置组件域
    public static int LEFT = 10;// 组件间距离
    public static int RIGHT = 10;// 组件间距离
    public static int TOP = 10;// 组件间距离
    public static int BOTTOM = 10;// 组件间距离
    public static int ZERO = 0;// 组件间距离
    public static int TEXT_PADDING = 5;// 文字离边框距离
    public static int HEIGHT_LINE = 1;// 线条高度
    public static int WIDTH_MIN = 123;
    public static int HEIGHT_MIN = 50;
    public static int HEIGHT_LIST_MIN = 30;
    public static int TEXT_SIZE = 14;// 文字大小
    public static int BUTTON_TEXT_SIZE = 14;// 按钮文字大小
    public static int HEIGHT_TITLE = 40;// 标题栏的高度
    public static int WIDTH_BOTTON = 100;// 按钮宽度
    public static int HEIGHT_BOTTON = 30;// 按钮高度
    public static int linearLayoutLeftWidth = 280;
    public static int COLOR_LINE = Color.rgb(204, 204, 204);// #cccccc 线条颜色
    public static int COLOR_TEXT = Color.BLACK;// 文字颜色
    public static int KEYBOARD_BOTTOM_MARGIN = 80;// 自定义软件盘弹出的时候偏移量
    public static int WEBVIEW_TOP = 300;// 底部带有按钮的webView所占用高度

    public static int BUTTON_INTABLE_PADDING = 50;// 列表中加入按钮行，左右填充大小

    public static int MAP_LIST_WIDTH_LEFT = 90;// 网点地图中列表左侧视图所占用宽度
    public static int MAP_TEXTSIZE = 15;// 网点地图标记文字大小
    public static int MAP_TEXTSIZE_OFFSET_LEFT = 7;// 网点地图标记文字往左偏移量
}
