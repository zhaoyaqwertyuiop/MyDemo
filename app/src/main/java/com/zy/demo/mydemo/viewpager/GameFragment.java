package com.zy.demo.mydemo.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

public class GameFragment extends Fragment {
	private TextView textView;
	private String text = "游戏";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.gamefragment, null);
		this.textView = (TextView) view.findViewById(R.id.text);
		textView.setText(text);
		return view;
	}

	public void setText(String text) {
		this.text = text;
	}
}
