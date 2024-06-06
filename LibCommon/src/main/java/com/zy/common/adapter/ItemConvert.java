package com.zy.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ItemConvert<T extends MultiBaseAdapter.MultiData, VB extends ViewBinding> implements MultiBaseAdapter.IItemConvert<T, VB> {
    public MultiBaseAdapter adapter;

    public VB initViewBinding(@NonNull ViewGroup parent) {
        try {
            Class clazz = getVBClass();
            Method method = clazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
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
    public Class<T> getTClass() {
        ParameterizedType ptClass = (ParameterizedType) getClass().getGenericSuperclass(); // 获得当前带有泛型类型类的父类
        Type type = ptClass.getActualTypeArguments()[0]; // 获得运行期的泛型类型
        return (Class<T>) type;
    }

    @Override
    public Class<VB> getVBClass() {
        ParameterizedType ptClass = (ParameterizedType) getClass().getGenericSuperclass(); // 获得当前带有泛型类型类的父类
        Type type = ptClass.getActualTypeArguments()[1]; // 获得运行期的泛型类型
        return (Class<VB>) type;
    }

    @Override
    public boolean isCurrentConvert(@NonNull MultiBaseAdapter.MultiData multiData) {
        // T 和 VB 类型都匹配，就是这个convert
        return getTClass() == multiData.getClass() && getVBClass() == multiData.getViewBinding();
    }

    // 当view需要设置click事件给外部时使用此方法
    public void initItemClick(ViewBinding binding, View view, T data, int position) {
        if (adapter.onItemClickListener != null) {
            view.setOnClickListener(v -> {
                adapter.onItemClickListener.onItemClick(binding, view, data, position);
            });
        }
    }
}
