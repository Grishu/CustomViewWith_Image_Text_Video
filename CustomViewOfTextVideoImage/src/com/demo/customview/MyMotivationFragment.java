package com.demo.customview;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.customview.database.DBParse;

public class MyMotivationFragment extends Fragment implements OnClickListener, OnPageChangeListener {

	private ViewPager mPager;
	boolean isFromDiscover = true;
	ArrayList<CustomViewVo> motivationalList;
	TextView tvSetAsMain;
	TextView tvRemove;
	DBParse dbParse;
	Drawable selectedMotivation, unselectedMotivational;
	private GestureDetectorCompat tapGestureDetector;
	private View mView;
	private Context m_contxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.my_motivation_fragment, container, false);

		m_contxt = MyMotivationFragment.this.getActivity();
		dbParse = new DBParse(m_contxt);

		selectedMotivation = m_contxt.getResources().getDrawable(R.drawable.ic_star);
		unselectedMotivational = m_contxt.getResources().getDrawable(R.drawable.ic_star_unselect);

		tapGestureDetector = new GestureDetectorCompat(m_contxt, new TapGestureListener());

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) mView.findViewById(R.id.pager);
		mPager.setOnPageChangeListener(this);
		mPager.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				tapGestureDetector.onTouchEvent(event);
				return false;
			}
		});

		tvSetAsMain = (TextView) mView.findViewById(R.id.tvSetAsMain);
		tvRemove = (TextView) mView.findViewById(R.id.tvRemove);

		tvSetAsMain.setOnClickListener(this);
		tvRemove.setOnClickListener(this);
		getDataAndUpdateViewPager();

		/**
		 * To show last added items We are checking first that if this item is
		 * coming from DiscoverScreen
		 * 
		 * If yes then we will display first item in view pager If no then we
		 * will display last item in view pager so user can see added item
		 */
		if (!isFromDiscover) {
			mPager.setCurrentItem(motivationalList.size());
			mPager.invalidate();
		}
		isFromDiscover = false;

		changeSetAsMotivationIcon();

		return mView;
	}

	private void getDataAndUpdateViewPager() {

		motivationalList = dbParse.getAllMotivationalList();

		MyMotivationAdapter mPagerAdapter = new MyMotivationAdapter(m_contxt, motivationalList);
		mPager.setAdapter(mPagerAdapter);
		mPagerAdapter.notifyDataSetChanged();
		mPager.invalidate();

		if (motivationalList.size() <= 0) {
			tvSetAsMain.setVisibility(TextView.GONE);
			tvRemove.setVisibility(TextView.GONE);
		} else {
			tvSetAsMain.setVisibility(TextView.VISIBLE);
			tvRemove.setVisibility(TextView.VISIBLE);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.ivRightTop:
		// Constants.callFragment(m_contxt, new AddTxtPicVideoFragment(),
		// true, null);
		// break;
		case R.id.tvRemove:
			final AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(
					m_contxt, android.R.style.Theme_Holo_Light_Dialog_NoActionBar)));
			builder.setMessage("Are you sure to want to remove this motivation?");
			builder.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							int position = mPager.getCurrentItem();

							boolean result = dbParse.removeView(motivationalList.get(
									position).get_id());
							if (result) {
								motivationalList.remove(position);
							}
							getDataAndUpdateViewPager();

						}
					}).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// dialog.cancel();
						}
					});

			AlertDialog alertdialog = builder.create();
			alertdialog.show();

			break;
		case R.id.tvSetAsMain:
			if (isSetAsMain()) {
				dbParse.updateSetViews(motivationalList.get(mPager.getCurrentItem())
						.get_id(), "0");
				changeSetAsMotivationIcon();
			} else {
				dbParse.updateSetViews(motivationalList.get(mPager.getCurrentItem())
						.get_id(), "1");

				changeSetAsMotivationIcon();
			}

			break;

		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		changeSetAsMotivationIcon();
	}

	public void changeSetAsMotivationIcon() {

		if (isSetAsMain()) {
			tvSetAsMain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star, 0, 0, 0);

		} else {
			tvSetAsMain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_unselect, 0, 0,
					0);
		}

		tvSetAsMain.invalidate();
	}

	private boolean isSetAsMain() {
		if (motivationalList.size() > 0) {
			boolean status = dbParse.isSetViewAsFavorite(motivationalList.get(
					mPager.getCurrentItem()).get_id());
			System.out.println("isSetAsMain : - " + status);
			return status;
		}
		return false;

	}

	class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			int mPosition = mPager.getCurrentItem();
			String motivationalType = motivationalList.get(mPosition).getType();
			String motivationalContent = motivationalList.get(mPosition).getText();
			String motivationId = motivationalList.get(mPosition).get_id();
			System.out.println("MotivationalType : " + motivationalType);
			System.out.println("MotivationalContent : " + motivationalContent);
			if (motivationalType.equals(MainActivity.MOTIVATIONAL_TEXT)) {
				Bundle bundle = new Bundle();
				bundle.putString(MainActivity.MOTIVATIONAL_ID, motivationId);
				bundle.putString(MainActivity.IS_UPDATE_MOTIVATIONAL_TEXT, "y");
				bundle.putString(MainActivity.MOTIVATIONAL_TEXT, motivationalContent);
				MainActivity.LoadFragment(new AddMotivationalTextFragment(), true, bundle);

			} else if (motivationalType.equals(MainActivity.MOTIVATIONAL_VIDEO)) {
				Intent intent = new Intent(MyMotivationFragment.this.getActivity(),
						VideoActivity.class);
				intent.putExtra(MainActivity.MOTIVATIONAL_VIDEO, motivationalContent);
				startActivity(intent);
			}

			return super.onSingleTapConfirmed(e);
		}
	}
}
