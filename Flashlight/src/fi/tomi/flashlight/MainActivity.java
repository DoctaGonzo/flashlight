package fi.tomi.flashlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	private final Context context = this;
	private Camera camera;
	private Switch flashtoggle;
	private PackageManager pm;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_main);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

		flashtoggle = (Switch) findViewById(R.id.flashtoggle);
		pm = context.getPackageManager();
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

		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			flashtoggle.setChecked(false);
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
			flashtoggle
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean ischecked) {
							if (ischecked) {
								parameters
										.setFlashMode(Parameters.FLASH_MODE_TORCH);
								camera.setParameters(parameters);
								camera.startPreview();
							} else {
								parameters
										.setFlashMode(Parameters.FLASH_MODE_OFF);
								camera.setParameters(parameters);
								camera.stopPreview();
							}
						}
					});
		}

	}

	public void onFlashToggle(View view) {
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			flashtoggle.setChecked(false);
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
			boolean isFlashOn = ((Switch) view).isChecked();
			if (!isFlashOn) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
