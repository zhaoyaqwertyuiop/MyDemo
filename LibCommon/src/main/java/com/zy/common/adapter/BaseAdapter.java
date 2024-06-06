package com.zy.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhaoya
 * @Date 2023/8/31 9:57
 * @describe 单itemType
 */
public abstract class BaseAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {
    public final List<T> dataList = new ArrayList<>();
    public OnItemClickListener<T, VB> onItemClickListener;

    @NonNull
    @Override
    public BaseAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VB binding = initViewBinding(parent);
        onViewBindingInflated(binding);
        return new BaseAdapter.BaseViewHolder(binding);
    }

    public void onViewBindingInflated(VB binding) {
    }

    protected VB initViewBinding(@NonNull ViewGroup parent) {
        try {
            ParameterizedType ptClass = (ParameterizedType) this.getClass().getGenericSuperclass(); // 获得当前带有泛型类型类的父类
            Type type = ptClass.getActualTypeArguments()[1]; // 获得运行期的泛型类型
            Class<VB> vClass = (Class<VB>) type;
            Method method = vClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            return (VB) method.invoke(null, LayoutInflater.from(parent.getContext()), parent, false);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.BaseViewHolder holder, int position) {
        initItemClick((VB) holder.binding, holder.binding.getRoot(), dataList.get(position), position);
        convert((VB) holder.binding, dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        VB binding;

        public BaseViewHolder(@NonNull VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public abstract void convert(VB binding, T itemData, int position);

    public interface OnItemClickListener<T, VB> {
        void onItemClick(VB itemBinding, View view, T itemData, int position);
    }
    public void setOnItemClickListener(OnItemClickListener<T, VB> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 当view需要设置click事件给外部时使用此方法
    public void initItemClick(VB binding, View view, T data, int position) {
        view.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(binding, view, data, position);
            }
        });
    }
}
