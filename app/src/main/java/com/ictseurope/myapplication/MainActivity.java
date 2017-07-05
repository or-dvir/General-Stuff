package com.ictseurope.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
	private static final String[] mRequiredPermissions = {Manifest.permission.READ_PHONE_STATE,
														  Manifest.permission.CAMERA};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	private void requestPermissions()
	{
		ArrayList<String> missingPermissions = Utils.getMissingPermissions(this,
																		   mRequiredPermissions);
		//we have all the permissions we need
		if(missingPermissions.isEmpty())
		{
			//todo do whatever you need to do
		}

		//some permissions are missing
		else
		{
			ActivityCompat.requestPermissions(this,
											  missingPermissions.toArray(new String[missingPermissions.size()]),
											  REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (grantResults.length > 0)
		{
			if (requestCode == REQUEST_CODE)
			{
				ArrayList<String> deniedPermissions = Utils.getDeniedPermissions(permissions, grantResults);

				//all permissions granted
				if (deniedPermissions.isEmpty())
				{
					//todo do whatever you need to do
				}

				//some permission denied
				else
				{
					boolean referToSettings = false;

					for(String permission : deniedPermissions)
					{
						if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
						{
							referToSettings = true;
							break;
						}
					}

					Utils.showPermissionDialog(this,
											   referToSettings,
											   getString(R.string.allowPermissionMessage),
											   new DialogInterface.OnClickListener()
											   {
												   //askAgainClickListener
												   @Override
												   public void onClick(DialogInterface dialogInterface, int i)
												   {
													   requestPermissions();
												   }
											   },
											   new DialogInterface.OnClickListener()
											   {
												   //denyClickListener
												   @Override
												   public void onClick(DialogInterface dialogInterface, int i)
												   {
													   //todo do whatever you need to do
												   }
											   });
				}
			}
		}
	}
}
