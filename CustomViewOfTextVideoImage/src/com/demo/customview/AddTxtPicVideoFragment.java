package com.demo.customview;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.SyncStateContract.Constants;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.customview.database.DatabaseSingleton;
import com.customview.database.SQLiteDBProvider;

public class AddTxtPicVideoFragment extends Fragment implements OnClickListener {
	private Button tvAddMotivationalText, tvAddMotivationalImage, tvAddMotivationalVideo;
	final CharSequence[] items = { "Take a Photo", "Choose from Gallery" };
	public static final int REQUEST_CAMERA = 0;
	public static final int SELECT_FILE = 1;
	final private int CAPTURE_IMAGE = 2;
	private String imgPath;
	private static final int SELECT_VIDEO = 3;
	private View mView;
	private Context m_context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mView = inflater.inflate(R.layout.my_motivation_what_would_like_add, container, false);
		m_context = AddTxtPicVideoFragment.this.getActivity();

		tvAddMotivationalText = (Button) mView.findViewById(R.id.tvAddMotivationalText);
		tvAddMotivationalImage = (Button) mView.findViewById(R.id.tvAddMotivationalImage);
		tvAddMotivationalVideo = (Button) mView.findViewById(R.id.tvAddMotivationalVideo);

		tvAddMotivationalText.setOnClickListener(this);
		tvAddMotivationalImage.setOnClickListener(this);
		tvAddMotivationalVideo.setOnClickListener(this);
		return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvAddMotivationalText:
			Bundle bundle = new Bundle();
			bundle.putString(MainActivity.IS_UPDATE_MOTIVATIONAL_TEXT, "");
			AddMotivationalTextFragment m_GroupchatFragmnt = new AddMotivationalTextFragment();

			FragmentTransaction ftConnectChatFragment = getFragmentManager().beginTransaction();
			ftConnectChatFragment.replace(R.id.changeFragment, m_GroupchatFragmnt).addToBackStack("Text").commit();
			// MainActivity.LoadFragment(new AddMotivationalTextFragment(),
			// false,
			// bundle);
			break;
		case R.id.tvAddMotivationalImage:
			ChooseImageDialog();
			break;
		case R.id.tvAddMotivationalVideo:
			Intent videoIntent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
			videoIntent.setType("video/*");
			startActivityForResult(Intent.createChooser(videoIntent, "Select Video"), SELECT_VIDEO);
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String imgPath = null;
		super.onActivityResult(requestCode, resultCode, data);
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToWrite();
		System.out.println("Request code  = " + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == CAPTURE_IMAGE) {
				imgPath = getImagePath();
				if (imgPath != null) {
					m_provider.insertMotivationalDetail(MainActivity.MOTIVATIONAL_PHOTO, imgPath);
				} else {
					Toast.makeText(m_context, "Failed to get camera image", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				imgPath = getPath(selectedImageUri);
				if (imgPath != null) {
					m_provider.insertMotivationalDetail(MainActivity.MOTIVATIONAL_PHOTO, imgPath);
				} else {
					Toast.makeText(m_context, "Failed to get gallery image", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (requestCode == SELECT_VIDEO) {
				System.out.println("SELECT_VIDEO");
				imgPath = getPath(data.getData());
				System.out.println("Video Path  = " + imgPath);
				if (imgPath != null) {
					m_provider.insertMotivationalDetail(MainActivity.MOTIVATIONAL_VIDEO, imgPath);
				} else {
					Toast.makeText(m_context, "Failed to get video", Toast.LENGTH_SHORT).show();
				}
			}

		}
		getActivity().onBackPressed();
	}

	public String getPath(Uri uri) {
		try {
			String[] projection = { MediaColumns.DATA };
			Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Select profile image from gallery of capture through camera.
	 */
	private void ChooseImageDialog() {
		AlertDialog.Builder photoBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
				m_context, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Choose a photo")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (items[which].equals("Take a Photo")) {

							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
							startActivityForResult(intent, CAPTURE_IMAGE);

							// startActivityForResult(intent, REQUEST_CAMERA);
						} else if (items[which].equals("Choose from Gallery")) {
							Intent intent = new Intent(Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							intent.setType("image/*");
							startActivityForResult(Intent.createChooser(intent, "Select File"),
									SELECT_FILE);
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		photoBuilder.create().show();
	}

	public Uri setImageUri() {
		File directory = new File(Environment.getExternalStorageDirectory() + File.separator
				+ getResources().getString(R.string.app_name));
		directory.mkdir();

		File file = new File(directory, "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	public String getImagePath() {
		return imgPath;
	}

	public String getVideoPath(Uri uri) {
		System.out.println("AddTxtPicVideoFragment :: getVideoPath : " + uri);
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
}
