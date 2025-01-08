package com.zy.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.zy.common.util.ScreenMatchUtil
import com.zy.common.route.RouteUtil
import com.zy.common.util.ApkUploadUtil
import com.zy.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val adapter by lazy {
        HomeAdapter().apply {
            setOnItemClickListener { itemBinding, view, itemData, position ->
                itemData.click.invoke()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        binding.recy.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recy.adapter = adapter
    }

    private fun initData() {
        adapter.dataList.add(ItemData("ChangeColorTextView") {
            RouteUtil.startActivity("com.zy.customer.ChangeColorActivity")
        })
        adapter.dataList.add(ItemData("Markdown") {
            RouteUtil.startActivity("com.zy.customer.MarkdownActivity")
        })
        adapter.dataList.add(ItemData("media3") {
            RouteUtil.startActivity("com.zy.player.PlayerActivity")
        })
        adapter.dataList.add(ItemData("iflytek") {
            RouteUtil.startActivity("com.zy.iflytek.IflytekDemoActivity")
        })
        adapter.dataList.add(ItemData("最小宽度限定符") {
            val screenWidthDp = ScreenMatchUtil.getSmallestScreenWidthDp(this)
            val screenWidthDp2 = ScreenMatchUtil.getSmallestScreenWidthDp2(this)
            ToastUtils.showShort("$screenWidthDp dp, $screenWidthDp2 px")
        })
        adapter.dataList.add(ItemData("apk升级") {
            ApkUploadUtil.o.download(this, "https://oss-beijing-sh-internal.openstorage.cn/iptv-oss-prod/common/use_help/app-release.apk", "77c41e7ee58725dc602df3f8704a1b58", 1)
        })
    }
}