package com.zy.common.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhaoya
 * @Date 2023/9/5 9:57
 * @describe 多itemType
 */
public abstract class MultiBaseAdapter extends RecyclerView.Adapter<MultiBaseAdapter.BaseViewHolder> {
    public final List<MultiData> dataList = new ArrayList<>();

    private final List<IItemConvert> convertList = new ArrayList<>();

    public BaseAdapter.OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public MultiBaseAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IItemConvert itemConvert = findConvert(viewType);
        if (itemConvert == null) {
            throw new RuntimeException("itemConvert=null,请确保调用了addItemType添加对应的itemConvert");
        }
        return new MultiBaseAdapter.BaseViewHolder(itemConvert.initViewBinding(parent));
    }

    private IItemConvert findConvert(int viewType) {
        for (MultiData multiData: dataList) {
            if (multiData.getViewBinding().hashCode() == viewType) {
                for (IItemConvert itemConvert: convertList) {
                    if (itemConvert.isCurrentConvert(multiData)){
                        return itemConvert;
                    }
                }
            }
        }
        return null;
    }

    public void addItemType(ItemConvert convert) {
        convertList.add(convert);
        convert.adapter = this;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiBaseAdapter.BaseViewHolder holder, int position) {
        for (IItemConvert itemConvert: convertList) {
            if (itemConvert.isCurrentConvert(dataList.get(position))) {
                if (itemConvert instanceof ItemConvert) {
                    ((ItemConvert)itemConvert).initItemClick(holder.binding, holder.binding.getRoot(), dataList.get(position), position);
                }
                itemConvert.convert(holder.binding, dataList.get(position), position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewBinding().hashCode();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewBinding binding;

        public BaseViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface MultiData {
        // 这里把viewbing类的hashCode作为itemtype
        Class<? extends ViewBinding> getViewBinding();
    }

    //    public interface IItemConvert<T extends MultiBaseAdapter.IMulti, VB extends ViewBinding> {
    public interface IItemConvert<T extends MultiBaseAdapter.MultiData, VB extends ViewBinding> {
        void convert(VB binding, T data, int position);

        VB initViewBinding(@NonNull ViewGroup parent);

        // 默认通过数据的class的hashcode匹配
        Class<T> getTClass();
        Class<VB> getVBClass();

        boolean isCurrentConvert(@NonNull MultiData multiData);
    }

    public interface OnItemClickListener {
        void onItemClick(ViewBinding itemBinding, View view, MultiData itemData, int position);
    }

    public void setOnItemClickListener(BaseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

