package com.zy.demo

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.zy.common.route.RouteUtil
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
        adapter.dataList.add(ItemData("自定义view") {
            ToastUtils.showShort("123")
        })
        adapter.dataList.add(ItemData("ChangeColorTextView") {
            RouteUtil.startActivity(packageName, "com.zy.customer.ChangeColorActivity")
        })
        adapter.dataList.add(ItemData("media3") {
            RouteUtil.startActivity(packageName, "com.zy.player.PlayerActivity")
        })
        adapter.dataList.add(ItemData("iflytek") {
            RouteUtil.startActivity(packageName, "com.zy.iflytek.IflytekDemoActivity")
        })
    }
}