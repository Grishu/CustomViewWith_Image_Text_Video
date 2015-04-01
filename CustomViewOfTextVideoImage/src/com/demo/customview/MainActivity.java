package com.demo.customview;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.customview.database.DatabaseSingleton;
import com.customview.database.SQLiteDBProvider;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends Activity {

	private ImageView m_ivView;
	public static final String MOTIVATIONAL_ID = "motivational_id";
	public static final String MOTIVATIONAL_TEXT = "text";
	public static final String MOTIVATIONAL_PHOTO = "photo";
	public static final String MOTIVATIONAL_VIDEO = "video";
	public static final String IS_UPDATE_MOTIVATIONAL_TEXT = "is_update_motivational_text";
	private Context m_cont;
	static FragmentTransaction m_trans;
	public static int myMotivationTransactionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		m_cont = MainActivity.this;
		SQLiteDBProvider m_prov = DatabaseSingleton.getInstance(getApplicationContext());
		initImageLoader(m_cont);
		m_trans = getFragmentManager().beginTransaction();
		m_ivView = (ImageView) findViewById(R.id.ivAddView);

		FragmentTransaction ftConnectChatFragment = getFragmentManager().beginTransaction();
		myMotivationTransactionId = ftConnectChatFragment
				.replace(R.id.changeFragment, new MyMotivationFragment())
				.addToBackStack("GroupChatFragment").commit();

		System.err.println("Id is=====" + myMotivationTransactionId);
		
		m_ivView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LoadFragment(new AddTxtPicVideoFragment(), false, null);
				AddTxtPicVideoFragment m_GroupchatFragmnt = new AddTxtPicVideoFragment();

				FragmentTransaction ftConnectChatFragment = getFragmentManager().beginTransaction();
				ftConnectChatFragment.replace(R.id.changeFragment, m_GroupchatFragmnt)
						.addToBackStack("AddVideoImage").commit();

			}
		});

	}


	public static int LoadFragment(Fragment mFragment, boolean isAddToBackStack, Bundle bundle) {
		// FragmentTransaction ftFindUserFragment = getFragmentManager()
		// .beginTransaction();

		// ftFindUserFragment
		// .replace(R.id.changeFragment, new FindUserFragment())
		// .addToBackStack("FindUser").commit();
		if (bundle != null) {
			mFragment.setArguments(bundle);
		}
		m_trans.replace(R.id.changeFragment, mFragment);
		if (isAddToBackStack) {
			return m_trans.addToBackStack("").commit();
		} else {
			return m_trans.commit();
		}
	}

	// public static int callFragment(Activity mActivity, Fragment mFragment,
	// boolean isAddToBackStack, Bundle bundle) {
	// FragmentManager fragmentManager = mActivity.getFragmentManager();
	// FragmentTransaction fragmentTransaction = fragmentManager
	// .beginTransaction();
	// if (bundle != null) {
	// mFragment.setArguments(bundle);
	// }
	// fragmentTransaction.replace(R.id.changeFragment, mFragment);
	// if (isAddToBackStack) {
	// return fragmentTransaction.addToBackStack("").commit();
	// } else {
	// return fragmentTransaction.commit();
	// }
	//
	// }

//	public static void popFragments(Activity mActivity, int id) {
//
//		FragmentManager fragmentManager = mActivity.getFragmentManager();
//		fragmentManager.popBackStack(id, 0);
//
//	}

	/**
	 * Initialize an ImageLoader class
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
																					// for
																					// release
																					// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
