package com.zy.demo

import com.zy.common.adapter.BaseAdapter
import com.zy.demo.databinding.ItemMainBinding

class HomeAdapter: BaseAdapter<ItemData, ItemMainBinding>() {
    override fun convert(binding: ItemMainBinding, itemData: ItemData, position: Int) {
        binding.itemBtn.setText(itemData.name)
        initItemClick(binding, binding.itemBtn, itemData, position)
    }
}