package com.myresources.util;


import android.app.ProgressDialog;
import android.content.Context;

/**
 * 进度提示工具,如正在下载等
 */
public class ProgressDialogUtil {

	private static ProgressDialog progressDialog;

	/** 弹出默认的加载提示 **/
	public static ProgressDialog showDefaultProgerss(Context context,
			String message) {

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();

		if (message != null) {
			progressDialog = getProgressDialog(context, null, message);
		} else {
			progressDialog = getProgressDialog(context, null, "正在加载,请稍候...");
		}

		if (!progressDialog.isShowing()) {
			progressDialog.show();

		}
		return progressDialog;
	}


	public static void closeDefalutProgerss() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}


	private static ProgressDialog getProgressDialog(Context context,
			String title, String content) {

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		if (title != null) {
			dialog.setTitle(title);
		}

		if (content != null) {
			dialog.setMessage(content);
		}
		return dialog;
	}

}
