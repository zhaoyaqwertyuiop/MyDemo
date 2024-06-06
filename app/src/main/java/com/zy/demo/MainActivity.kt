package com.zy.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.blankj.utilcode.util.ToastUtils
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
        adapter.dataList.add(ItemData("12312312312") {
            ToastUtils.showShort("456")
        })
    }
}