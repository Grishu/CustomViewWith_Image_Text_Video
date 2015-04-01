package com.demo.customview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.customview.database.DatabaseSingleton;
import com.customview.database.SQLiteDBProvider;

public class AddMotivationalTextFragment extends Fragment implements OnClickListener {

	private EditText m_etMotivText;
	boolean isUpdateMotivational = false;
	String keyMotivationalId;
	private View mView;
	private Context m_contxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.motivational_add_text_screen, container, false);
		m_contxt = AddMotivationalTextFragment.this.getActivity();
		m_etMotivText = (EditText) mView.findViewById(R.id.edtTxtMotivationalAddTextScreen);
		Button btnYes = (Button) mView.findViewById(R.id.btnYes);
		btnYes.setOnClickListener(this);
		Button btnNo = (Button) mView.findViewById(R.id.btnNo);
		btnNo.setOnClickListener(this);

		Bundle bundle = getArguments();
		if (bundle != null) {
			String keyContent = bundle.getString(MainActivity.IS_UPDATE_MOTIVATIONAL_TEXT);
			keyMotivationalId = bundle.getString(MainActivity.MOTIVATIONAL_ID);

			String motivationalText = bundle.getString(MainActivity.MOTIVATIONAL_TEXT);
			System.out.println("AddMotivationalTextFragment :: onCreateView : MotivationalText - "
					+ motivationalText);
			m_etMotivText.setText(motivationalText);
			if (keyContent.equals("y")) {
				isUpdateMotivational = true;
			} else {
				isUpdateMotivational = false;
			}
		}
		return mView;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnYes:
			String info = m_etMotivText.getText().toString();
			if (info.equals("")) {
				Toast.makeText(m_contxt, "Please write something first", Toast.LENGTH_SHORT).show();
			} else {
				SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_contxt);
				m_provider.openToWrite();
				if (isUpdateMotivational) {
					m_provider.updateMotivationalDetail(keyMotivationalId, info);
				} else {

					m_provider.insertMotivationalDetail(MainActivity.MOTIVATIONAL_TEXT, info);
				}

				System.out
						.println("AddMotivationalTextFragment :: onClick : isUpdatedMotivational = "
								+ isUpdateMotivational);

				// MainActivity.popFragments(m_contxt,MainActivity.myMotivationTransactionId);
				 getFragmentManager().popBackStack(MainActivity.myMotivationTransactionId,0);
//				FragmentManager fragmentManager = getActivity().getFragmentManager();
//				fragmentManager.popBackStack("Text", 0);
			}
			break;
		case R.id.btnNo:

			break;
		}
	}

	@Override
	public void onPause() {
		InputMethodManager imm = (InputMethodManager) m_contxt
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(m_etMotivText.getWindowToken(), 0);
		super.onPause();
	}
}
