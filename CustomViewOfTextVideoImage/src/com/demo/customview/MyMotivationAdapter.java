package com.demo.customview;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMotivationAdapter extends PagerAdapter {
	Context mActivity;
	int mPosition;
	private ArrayList<CustomViewVo> mMotivationalList;
	
	
	MyMotivationAdapter(Context activity,
			ArrayList<CustomViewVo> motivationalList) {
		this.mActivity = activity;
		this.mMotivationalList = motivationalList;
	}

	@Override
	public int getCount() {
		return mMotivationalList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		if (mMotivationalList.get(mPosition).getType()
				.equals(MainActivity.MOTIVATIONAL_PHOTO)) {
			return view == ((View) object);
		} else if (mMotivationalList.get(mPosition).getType()
				.equals(MainActivity.MOTIVATIONAL_TEXT)) {
			return view == ((View) object);
		} else if (mMotivationalList.get(mPosition).getType()
				.equals(MainActivity.MOTIVATIONAL_VIDEO)) {
			return view == ((View) object);
		}
		return false;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		mPosition = position;
		if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_PHOTO)) {
			ImageView imageView = new ImageView(mActivity);
			int padding = mActivity.getResources().getDimensionPixelSize(
					R.dimen.small_margin);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			 ImageLoader.getInstance().displayImage(
			 "file://" + mMotivationalList.get(position).getText(),
			 imageView);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		} else if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_TEXT)) {
			TextView textView = new TextView(mActivity);
			int padding = mActivity.getResources().getDimensionPixelSize(
					R.dimen.small_margin);
			textView.setPadding(padding, padding, padding, padding);
			textView.setText(mMotivationalList.get(position).getText());
			((ViewPager) container).addView(textView, 0);
			return textView;
		} else if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_VIDEO)) {
			ImageView textView = new ImageView(mActivity);
			int padding = mActivity.getResources().getDimensionPixelSize(
					R.dimen.small_margin);
			textView.setPadding(padding, padding, padding, padding);
			Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
					mMotivationalList.get(position).getText(),
					Thumbnails.MICRO_KIND);
			textView.setImageResource(R.drawable.video);
			textView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			textView.setBackground(new BitmapDrawable(bitmap));
			((ViewPager) container).addView(textView, 0);
			return textView;
		}
		return null;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_PHOTO)) {
			((ViewPager) container).removeView((View)object);
		} else if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_TEXT)) {
			((ViewPager) container).removeView((View) object);
		} else if (mMotivationalList.get(position).getType()
				.equals(MainActivity.MOTIVATIONAL_VIDEO)) {
			((ViewPager) container).removeView((View) object);
		}

	}
}