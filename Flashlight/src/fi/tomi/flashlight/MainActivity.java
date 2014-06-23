package fi.tomi.flashlight;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.os.Build;

public class MainActivity extends Activity {

	private final Context context = this;
	private Camera camera;
	private ToggleButton flashtoggle;
	private PackageManager pm = context.getPackageManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		flashtoggle = (ToggleButton) findViewById(R.id.flashtoggle);

		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle("No Camera");
			alertDialog.setMessage("Your device does not support camera.");
			alertDialog.setButton(RESULT_OK, "Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int which) {
							Log.e("err", "Your device does not support camera.");
						}
					});
			alertDialog.show();
		} else {
			camera = Camera.open();
		}
	}
	
	public void onFlashToggle(View view){
		if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle("No Flash");
			alertDialog.setMessage("Your device does not support flash.");
			alertDialog.setButton(RESULT_OK, "Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,
								final int which) {
							Log.e("err", "Your device does not support flash.");
						}
					});
			alertDialog.show();
		} else {
			final Parameters parameters = camera.getParameters();
			boolean isFlashOn = ((ToggleButton) view).isChecked();
			if(!isFlashOn){
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.stopPreview();
			} else {
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				camera.startPreview();
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (camera != null) {
			camera.release();
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

}
