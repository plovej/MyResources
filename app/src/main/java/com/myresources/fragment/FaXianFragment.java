package com.myresources.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myresources.R;
import com.myresources.chizi.NumberUtils;
import com.myresources.chizi.SimpleRulerView;

public class FaXianFragment extends Fragment implements View.OnClickListener {
	private SimpleRulerView rulerView;
	private float value;
	private TextView textView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.faxian_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setUpView();
	}
	private  void setUpView(){
		textView = (TextView) getView().findViewById(R.id.blood_sugar_value);
		rulerView = (SimpleRulerView) getView().findViewById(R.id.ruler_view2);
		getView().findViewById(R.id.blood_sugar_minus).setOnClickListener(this);
		getView().findViewById(R.id.blood_sugar_add).setOnClickListener(this);
		rulerView.setOnValueChangeListener(new SimpleRulerView.OnValueChangeListener() {

			@Override
			public void onChange(SimpleRulerView view, int position, float value) {

				// if (scale != BloodSugarActivity.this.scale) {

				textView.setText(NumberUtils.toString(value));
				FaXianFragment.this.value = value;
				// }
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.blood_sugar_minus:

				value = (float) (value-0.1);
				if (value > 0 ) {

					rulerView.setSelectedValue(value);
					textView.setText(NumberUtils.toString(value));
				}else{
					value = 0;
				}

				break;
			case R.id.blood_sugar_add:

				value = (float) (value+0.1);
				if (value < 33.3F) {

					rulerView.setSelectedValue(value);
					textView.setText(NumberUtils.toString(value));
				}else{

					value = 33.3F;
				}
				break;

		}
	}
}
