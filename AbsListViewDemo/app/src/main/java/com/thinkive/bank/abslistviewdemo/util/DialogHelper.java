package com.thinkive.bank.abslistviewdemo.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


/**
 * @author: sq
 * @date: 2017/7/26
 * @corporation: 深圳市思迪信息科技有限公司
 * @description: dialog工具类
 */
public class DialogHelper {

    private static ProgressDialog waitDialog = null;

    /**
     * 获取一个dialog
     *
     * @param context 上下文
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /**
     * 显示一个耗时等待对话框
     *
     * @param context 上下文
     * @param message 提示信息
     */
    public static void showWaitDialog(final Context context, String title, String message) {
        if (canShowDialog(context)) {
            if (waitDialog != null) {
                waitDialog.cancel();
                waitDialog.dismiss();
            }
            waitDialog = new ProgressDialog(context);
            waitDialog.setCancelable(false);
            waitDialog.setIndeterminate(true);//进度条采用不明确显示进度的‘模糊模式’

            waitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                    }
                    return false;
                }
            });

            if (!TextUtils.isEmpty(title)) {
                waitDialog.setTitle(title);
            }

            if (!TextUtils.isEmpty(message)) {
                waitDialog.setMessage(message);
            }
            waitDialog.show();
        }

    }

    /**
     * 隐藏一个耗时等待对话框
     */
    public static void hiddenWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }

        waitDialog = null;
    }

    /**
     * 显示普通单按钮对话框
     *
     * @param context         上下文
     * @param title           标题
     * @param message         提示信息
     * @param onClickListener 确定按钮的监听器
     */
    public static void showMessageDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("确定", onClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示普通单按钮对话框
     *
     * @param context  上下文
     * @param iconId   图标         -1标识不显示图标
     * @param title    标题 (必填)
     * @param message  显示内容 (必填)
     * @param btnName  按钮名称 (必填)
     * @param listener 监听器
     */
    public static void showMessageDialog(Context context, int iconId, String title, String message,
                                         String btnName, DialogInterface.OnClickListener listener) {
        if (canShowDialog(context)) {
            try {
                AlertDialog.Builder builder = getDialog(context);
                if (-1 != iconId) {
                    builder.setIcon(iconId);
                }
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(btnName, listener);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示普通双按钮对话框
     *
     * @param context               上下文
     * @param title                 标题
     * @param message               提示信息
     * @param onOkClickListener     确定按钮的监听器
     * @param onCancleClickListener 取消按钮的监听器
     */
    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("确定", onOkClickListener);
            builder.setNegativeButton("取消", onCancleClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示普通双按钮对话框
     *
     * @param context           上下文
     * @param iconId            图标         -1标识不显示图标
     * @param title             标题
     * @param message           提示信息
     * @param btnPositiveName   按钮名称 (必填)
     * @param listener_Positive 确定按钮的监听器
     * @param btnNegativeName   按钮名称 (必填)
     * @param listener_Negative 取消按钮的监听器
     * @return
     */
    public static void showConfirmDialog(Context context, int iconId, String title, String message, String btnPositiveName,
                                         DialogInterface.OnClickListener listener_Positive, String btnNegativeName,
                                         DialogInterface.OnClickListener listener_Negative) {
        if (canShowDialog(context)) {
            try {
                AlertDialog.Builder builder = getDialog(context);
                if (-1 != iconId) {
                    builder.setIcon(iconId);
                }
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(btnPositiveName, listener_Positive);
                builder.setNegativeButton(btnNegativeName, listener_Negative);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示列表对话框
     *
     * @param context             上下文
     * @param title               标题
     * @param arrays              所需要显示的列表内容
     * @param onItemClickListener item选中的监听器
     */
    public static void showListDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onItemClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setItems(arrays, onItemClickListener);
            builder.setPositiveButton("确定", null);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }


    /**
     * 显示单选对话框
     *
     * @param context             上下文
     * @param title               标题
     * @param arrays              所需要显示的列表内容
     * @param selectIndex         默认选中item的位置，从0开始。如果设置为-1，则无选中项
     * @param onItemClickListener item选中的监听器
     * @param onOkClickListener   确定按钮的监听器
     */
    public static void showSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onItemClickListener, DialogInterface.OnClickListener onOkClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setSingleChoiceItems(arrays, selectIndex, onItemClickListener);
            builder.setPositiveButton("确定", onOkClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示单选对话框
     *
     * @param context     上下文
     * @param iconId      图标      -1标识不显示图标
     * @param title       标题
     * @param itemsId     所需要显示的列表内容资源ID
     * @param selectIndex 默认选中item的位置，从0开始。如果设置为-1，则无选中项
     * @param listener    item选中的监听器
     * @param btnName     按钮名称
     * @param listener2   确定按钮的监听器
     */
    public static void showSingleChoiceDialog(Context context, int iconId, String title, int itemsId, int selectIndex,
                                              DialogInterface.OnClickListener listener, String btnName,
                                              DialogInterface.OnClickListener listener2) {
        if (canShowDialog(context)) {
            try {
                AlertDialog.Builder builder = getDialog(context);
                if (-1 != iconId) {
                    builder.setIcon(iconId);
                }
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setSingleChoiceItems(itemsId, selectIndex, listener);
                builder.setPositiveButton(btnName, listener2);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示单选对话框
     *
     * @param context               上下文
     * @param title                 标题
     * @param arrays                所需要显示的列表内容
     * @param selectIndex           默认选中item的位置，从0开始。如果设置为-1，则无选中项
     * @param onItemClickListener   item选中的监听器
     * @param onOkClickListener     确定按钮的监听器
     * @param onCancleClickListener 取消按钮的监听器
     */
    public static void showSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onItemClickListener, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setSingleChoiceItems(arrays, selectIndex, onItemClickListener);
            builder.setPositiveButton("确定", onOkClickListener);
            builder.setNegativeButton("取消", onCancleClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示多选对话框
     *
     * @param context                    上下文
     * @param title                      标题
     * @param arrays                     所需要显示的列表内容
     * @param selectFlag                 长度跟arrays字符串长度一致，保存有哪些item处于选中状态，true表示选中，false表示未选中，可以传入null表示都未选中
     * @param onMultiChoiceClickListener item选中的监听器
     * @param onOkClickListener          确定按钮的监听器
     */
    public static void showMultiChoiceDialog(Context context, String title, String[] arrays, boolean[] selectFlag, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener, DialogInterface.OnClickListener onOkClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setMultiChoiceItems(arrays, selectFlag, onMultiChoiceClickListener);
            builder.setPositiveButton("确定", onOkClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示多选对话框
     *
     * @param context   上下文
     * @param iconId    图标      -1标识不显示图标
     * @param title     标题
     * @param itemsId   所需要显示的列表内容资源ID
     * @param flags     长度跟列表内容arrays字符串长度一致，保存有哪些item处于选中状态，true表示选中，false表示未选中，可以传入null表示都未选中
     * @param listener  item选中的监听器
     * @param btnName   按钮名称
     * @param listener2 确定按钮的监听器
     */
    public static void showMultiChoiceDialog(Context context, int iconId, String title, int itemsId,
                                             boolean[] flags, DialogInterface.OnMultiChoiceClickListener listener,
                                             String btnName, DialogInterface.OnClickListener listener2) {
        if (canShowDialog(context)) {
            try {
                AlertDialog.Builder builder = getDialog(context);
                if (-1 != iconId) {
                    builder.setIcon(iconId);
                }
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setMultiChoiceItems(itemsId, flags, listener);
                builder.setPositiveButton(btnName, listener2);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示多选对话框
     *
     * @param context                    上下文
     * @param title                      标题
     * @param arrays                     所需要显示的列表内容
     * @param selectFlag                 长度跟arrays字符串长度一致，保存有哪些item处于选中状态，true表示选中，false表示未选中，可以传入null表示都未选中
     * @param onMultiChoiceClickListener item选中的监听器
     * @param onOkClickListener          确定按钮的监听器
     * @param onCancleClickListener      取消按钮的监听器
     */
    public static void showMultiChoiceDialog(Context context, String title, String[] arrays, boolean[] selectFlag, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        if (canShowDialog(context)) {
            AlertDialog.Builder builder = getDialog(context);
            builder.setMultiChoiceItems(arrays, selectFlag, onMultiChoiceClickListener);
            builder.setPositiveButton("确定", onOkClickListener);
            builder.setNegativeButton("取消", onCancleClickListener);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.show();
        }

    }

    /**
     * 显示日期对话框
     *
     * @param context
     * @param v
     * @return
     */
    public static void showDateDialog(Context context, final View v) {
        if (canShowDialog(context)) {
            try {
                Calendar calender = Calendar.getInstance();
                Dialog dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (v instanceof TextView) {
                                    ((TextView) v).setText(year + "年"
                                            + (monthOfYear + 1) + "月"
                                            + dayOfMonth + "日");
                                }
                                if (v instanceof EditText) {
                                    ((EditText) v).setText(year + "年"
                                            + (monthOfYear + 1) + "月"
                                            + dayOfMonth + "日");
                                }
                            }
                        }, calender.get(Calendar.YEAR),
                        calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当判断当前手机没有网络时使用
     *
     * @param context 上下文
     * @param iconId  图标         -1标识不显示图标
     * @param title   标题
     */
    public static void showNoNetWorkDialog(final Context context, int iconId, String title) {
        if (canShowDialog(context)) {
            try {
                AlertDialog.Builder builder = getDialog(context);
                if (-1 != iconId) {
                    builder.setIcon(iconId);
                }
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setMessage("当前无网络")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 跳转到系统的网络设置界面
                                Intent intent = new Intent();
                                intent.setClassName("com.android.settings",
                                        "com.android.settings.WirelessSettings");
                                context.startActivity(intent);

                            }
                        }).setNegativeButton("知道了 ", null);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否能够显示对话框
     *
     * @param context
     * @return
     */
    public static boolean canShowDialog(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isFinishing();
        } else {
            return false;
        }
    }

}
