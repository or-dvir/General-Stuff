package com.ictseurope.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

public class Utils
{
	public static String getRealPathFromURI(Context context, Uri contentURI)
	{
		String result;
		Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null)
		{
			result = contentURI.getPath();
		}

		else
		{
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}

		return result;
	}

	public static void showPermissionDialog(final Context context,
											boolean referToSettings,
											String message,
											DialogInterface.OnClickListener askAgainClickListener,
											DialogInterface.OnClickListener denyClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		String strAskMeAgain = "Ask me again";
		String strDeny = "Deny";
		String strGoToSetting = "Go to settings";

		//user previously denied a permission,
		//but did NOT click "never show again"
		if (referToSettings)
		{
			builder.setMessage(message)
				   .setPositiveButton(strAskMeAgain, askAgainClickListener)
				   .setNegativeButton(strDeny, denyClickListener);
		}

		//user previously clicked "never show again"
		//on some permission
		else
		{
			builder.setMessage(message)
				   .setPositiveButton(strGoToSetting, new DialogInterface.OnClickListener()
				   {
					   @Override
					   public void onClick(DialogInterface dialogInterface, int i)
					   {
						   Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						   Uri uri = Uri.fromParts("package", context.getPackageName(), null);
						   intent.setData(uri);
						   context.startActivity(intent, null);
					   }
				   })
				   .setNegativeButton(strDeny, denyClickListener);
		}

		builder.show();
	}

	public static ArrayList<String> getDeniedPermissions(@NonNull String[] requestedPermissions, @NonNull int[] grantResults)
	{
		ArrayList<String> deniedPermissions = new ArrayList<>(grantResults.length);

		for (int i = 0; i < grantResults.length; i++)
		{
			if(grantResults[i] == PackageManager.PERMISSION_DENIED)
			{
				deniedPermissions.add(requestedPermissions[i]);
			}
		}

		return deniedPermissions;
	}

	public static ArrayList<String> getMissingPermissions(Context context, String[] requiredPermissions)
	{
		ArrayList<String> missingPermissions = new ArrayList<>(requiredPermissions.length);

		for(String permission : requiredPermissions)
		{
			int permissionState = ContextCompat.checkSelfPermission(context,
																	permission);
			if(permissionState == PackageManager.PERMISSION_DENIED)
			{
				missingPermissions.add(permission);
			}
		}

		return missingPermissions;
	}

	/**
	 * displays a simple dialog with a single button which dismisses the dialog when clicked
	 * @param context the context to use
	 * @param title the title of the dialog (if null or empty string, no title will be shown)
	 * @param message the message to display to the user
	 * @param buttonText the text of the button
	 * @param listener a listener for the button click (or null if no further action is needed)
	 */
	public static void makeSimpleDialog(Context context,
										@Nullable String title,
										String message,
										String buttonText,
										@Nullable final iDialogButtonClickedListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if(title != null && title.isEmpty() == false)
		{
			builder.setTitle(title);
		}

		builder.setCancelable(false)
			   .setMessage(message)
			   .setPositiveButton(buttonText, new DialogInterface.OnClickListener()
			   {
				   @Override
				   public void onClick(DialogInterface dialogInterface, int i)
				   {
					   dialogInterface.dismiss();

					   if(listener != null)
					   {
						   listener.onDialogButtonClicked();
					   }
				   }
			   });

		builder.show();
	}

	/**
	 * convenience method for {@makeSimpleDialog} using string resources
	 * @param context the context to use
	 * @param title the title of the dialog (if 0, no title will be shown)
	 * @param message the message to display to the user
	 * @param buttonText the text of the button
	 * @param listener a listener for the button click (or null if not futher action is needed)
	 */
	public static void makeSimpleDialog(Context context,
										@StringRes int title,
										@StringRes int message,
										@StringRes int buttonText,
										@Nullable final iDialogButtonClickedListener listener)
	{
		Resources res = context.getResources();

		if(title == 0)
		{
			makeSimpleDialog(context,
							 null,
							 res.getString(message),
							 res.getString(buttonText),
							 listener);
		}

		else
		{
			makeSimpleDialog(context,
							 res.getString(title),
							 res.getString(message),
							 res.getString(buttonText),
							 listener);
		}
	}

	public static ProgressDialog makeProgressDialog(Context context,
													String message)
	{
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage(message);

		return dialog;
	}
}
