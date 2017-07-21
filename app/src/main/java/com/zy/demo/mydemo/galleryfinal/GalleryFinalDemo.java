package com.zy.demo.mydemo.galleryfinal;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zy.demo.mydemo.R;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/** 图片选择器 */
public class GalleryFinalDemo extends Activity implements View.OnClickListener, GalleryFinal.OnHanlderResultCallback{

	private static final int REQUEST_CODE_GALLERY = 1; // 单选,多选打开相册
	private static final int REQUEST_CODE_CAMERA = 2; // 使用牌照
	private static final int REQUEST_CODE_CROP = 3; // 使用裁剪
	private static final int REQUEST_CODE_EDIT = 4; // 使用图片编辑

	private Context context;
	private GridView mGridView;
	private List<PhotoInfo> mDataList;
	private MyGridViewAdapter adapter;
	private int mCurrentPhotoInfoNum = -1; // 当前选中的图片编号

	private Button mCropBtn, mEditBtn; // 裁剪,编辑按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_final_demo);
		this.context = this;
		mGridView = (GridView) super.findViewById(R.id.GridView);
		mDataList = new ArrayList<>();
		adapter = new MyGridViewAdapter();
		mGridView.setAdapter(adapter);

		mCropBtn = (Button) super.findViewById(R.id.galleryfinal_crop_btn);
		mEditBtn = (Button) super.findViewById(R.id.galleryfinal_edit_btn);
		mCropBtn.setEnabled(false);
		mEditBtn.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.galleryfinal_gallery_single_btn: // 单选
				GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, this);
				break;
			case R.id.galleryfinal_gallery_muti_btn: // 多选
				GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 8, this);
				break;
			case R.id.galleryfinal_camera_btn: // 拍照
				GalleryFinal.openCamera(REQUEST_CODE_CAMERA, this);
				break;
			case R.id.galleryfinal_crop_btn: // 裁剪
				GalleryFinal.openCrop(REQUEST_CODE_CROP, mDataList.get(mCurrentPhotoInfoNum).getPhotoPath(), this);
				break;
			case R.id.galleryfinal_edit_btn: // 图片编辑
				GalleryFinal.openEdit(REQUEST_CODE_EDIT, mDataList.get(mCurrentPhotoInfoNum).getPhotoPath(), this);
				break;
			default: break;
		}
	}

	@Override
	public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
		switch (requestCode) {
			case REQUEST_CODE_GALLERY:
				for(PhotoInfo photoInfo : resultList) {
					mDataList.add(photoInfo);
				}
				break;
			case REQUEST_CODE_CAMERA:
				for(PhotoInfo photoInfo : resultList) {
					mDataList.add(photoInfo);
				}
				break;
			case REQUEST_CODE_CROP:
				mDataList.set(mCurrentPhotoInfoNum, resultList.get(0));
				break;
			case REQUEST_CODE_EDIT:
				mDataList.set(mCurrentPhotoInfoNum, resultList.get(0));
				break;
			default: break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		switch (requestCode) {
			case REQUEST_CODE_GALLERY: showMsg("相框返回错误:" + errorMsg); break;
			case REQUEST_CODE_CAMERA: showMsg("拍照返回错误:" + errorMsg); break;
			case REQUEST_CODE_CROP: showMsg("裁剪返回错误:" + errorMsg); break;
			case REQUEST_CODE_EDIT: showMsg("图片编辑返回错误:" + errorMsg); break;
			default: break;
		}
	}

	private void showMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_image, null);
				convertView.setLayoutParams(
						new GridView.LayoutParams((int) (parent.getWidth() / mGridView.getNumColumns()),
								(int) (parent.getWidth() / mGridView.getNumColumns())));
				Log.d("TAG", convertView.getMeasuredWidth() + "");
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final PhotoInfo photoInfo = mDataList.get(position);

			x.image().bind(holder.image, photoInfo.getPhotoPath());
			holder.image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					File file = new File(photoInfo.getPhotoPath());
					if (file != null) {
						showMsg("文件大小:" + file.length() / 1024 + "kb");
					}
					if (mCurrentPhotoInfoNum == position) {
						mCurrentPhotoInfoNum = -1;
						mCropBtn.setEnabled(false);
						mEditBtn.setEnabled(false);
					} else {
						mCurrentPhotoInfoNum = position;
						mCropBtn.setEnabled(true);
						mEditBtn.setEnabled(true);
					}
					initImageBg();
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView image;
		}

		private void initImageBg() {
			for (int i = 0; i < mDataList.size(); i++) {
				if (mCurrentPhotoInfoNum == i) {
					mGridView.getChildAt(i).setBackgroundResource(R.drawable.shape_image_bg);
				} else {
					mGridView.getChildAt(i).setBackgroundResource(0);
				}
			}
		}
	}
}
