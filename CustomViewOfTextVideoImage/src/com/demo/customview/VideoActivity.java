package com.demo.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_video);
		VideoView videoView = (VideoView)findViewById(R.id.videoView);
		Intent intent = getIntent();
		String videoPath = intent.getStringExtra(MainActivity.MOTIVATIONAL_VIDEO);
			System.out.println("Video Path - "+videoPath);
		videoView.setVideoPath(videoPath);
		videoView.setMediaController(new MediaController(this));
		videoView.start();
		super.onCreate(savedInstanceState);
	}

}
