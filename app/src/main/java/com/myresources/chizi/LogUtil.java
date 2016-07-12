package com.myresources.chizi;
import android.content.Context;
import android.util.Log;

/**
 * Android 日志控制器
 */
public final class LogUtil {

	// 日志输出控制器
	private static boolean IS_LOG = true;

	// 日志级别
	public enum LogLevel {
		DEBUG, INFO, WARN, ERROR
	}

	// 日志输出的级别
	private static LogLevel LOG_LEVEL = LogLevel.ERROR;

	// debug
	public static void debug(Class<?> tag, String msg) {
		if (isLog(1)) {
			Log.d(tag.getSimpleName(), msg);
		}
	}

	public static void debug(Context context, String msg) {
		if (isLog(1)) {
			Log.d(context.getClass().getSimpleName(), msg);
		}
	}

	public static void debug(String tag, String msg) {
		if (isLog(1)) {
			Log.d(tag, msg);
		}
	}

	// info
	public static void info(Class<?> tag, String msg) {
		if (isLog(2)) {
			Log.i(tag.getSimpleName(), msg);
		}
	}

	public static void info(Context context, String msg) {
		if (isLog(2)) {
			Log.i(context.getClass().getSimpleName(), msg);
		}
	}

	public static void info(String tag, String msg) {
		if (isLog(2)) {
			Log.i(tag, msg);
		}
	}

	// warn
	public static void warn(Class<?> tag, String msg) {
		if (isLog(3)) {
			Log.w(tag.getSimpleName(), msg);
		}
	}

	public static void warn(Context context, String msg) {
		if (isLog(3)) {
			Log.w(context.getClass().getSimpleName(), msg);
		}
	}

	public static void warn(String tag, String msg) {
		if (isLog(3)) {
			Log.w(tag, msg);
		}
	}

	// error
	public static void error(Class<?> tag, String msg) {
		if (isLog(4)) {
			Log.e(tag.getSimpleName(), msg);
		}
	}

	public static void error(Context context, String msg) {
		if (isLog(4)) {
			Log.e(context.getClass().getSimpleName(), msg);
		}
	}

	public static void error(String tag, String msg) {
		if (isLog(4)) {
			Log.e(tag, msg);
		}
	}

	private static boolean isLog(int level) {

		if (IS_LOG && level >= LOG_LEVEL_INT) {
			return true;
		}
		return false;
	}

	static {

		switch (LOG_LEVEL) {
		case DEBUG:
			LOG_LEVEL_INT = 1;
			break;
		case INFO:
			LOG_LEVEL_INT = 2;
			break;
		case WARN:
			LOG_LEVEL_INT = 3;
			break;
		case ERROR:
			LOG_LEVEL_INT = 4;
			break;
		}
	}

	private static int LOG_LEVEL_INT = -1;

}
