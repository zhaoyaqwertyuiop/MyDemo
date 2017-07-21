package com.zy.demo.mydemo.sharesdk.onekeyshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.zy.demo.mydemo.R;

import cn.sharesdk.framework.ShareSDK;

public class ShareSDKDemo extends Activity implements View.OnClickListener{

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_sdkdemo);

		// 初始化ShareSDK
		ShareSDK.initSDK(this);

		this.context = this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.share_btn: // 分享
				OnekeyShare oks = new OnekeyShare();

				//ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
				oks.setTheme(OnekeyShareTheme.CLASSIC);
				// 令编辑页面显示为Dialog模式
				oks.setDialogMode();
				// 在自动授权时可以禁用SSO方式
				oks.disableSSOWhenAuthorize();
				//oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
				oks.setTitle("ShareSDK--Title");
				oks.setTitleUrl("http://mob.com");
				oks.setText("ShareSDK--文本");
				//oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
				oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
				oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
				//oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
				oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
				oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
				oks.setSiteUrl("http://mob.com");//QZone分享参数
				oks.setVenueName("ShareSDK");
				oks.setVenueDescription("This is a beautiful place!");
				// 将快捷分享的操作结果将通过OneKeyShareCallback回调
				//oks.setCallback(new OneKeyShareCallback());
				// 去自定义不同平台的字段内容
				//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
				// 在九宫格设置自定义的图标
				Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
				String label = "ShareSDK";
				View.OnClickListener listener = new View.OnClickListener() {
					public void onClick(View v) {

					}
				};
				oks.setCustomerLogo(logo, label, listener);

				// 为EditPage设置一个背景的View
				//oks.setEditPageBackground(getPage());
				// 隐藏九宫格中的新浪微博
				// oks.addHiddenPlatform(SinaWeibo.NAME);

				// String[] AVATARS = {
				// 		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
				// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
				// 		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
				// 		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
				// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
				// 		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
				// oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

				// 启动分享
				oks.show(context);
				break;
			default:
				break;
		}
	}

}
