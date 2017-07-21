package com.zylibrary.util;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Iterator;

@UiThread
public class ActivityStack {

    private final static ArrayDeque<WeakReference<Activity>> activities = new ArrayDeque<WeakReference<Activity>>(32);

    /**
     * 将Activity压入Application栈
     *
     * @param task
     *            将要压入栈的Activity对象
     */
    private static void push(Activity task) {
        activities.push(new WeakReference<Activity>(task));
    }

    /**
     * 获取activity数量
     */
    public static int size() {
        expungeStaleActivities();
        return activities.size();
    }

    /**
     * 获取顶部activity
     *
     * @return
     */
    public static Activity top() {
        expungeStaleActivities();
        return activities.isEmpty() ? null : activities.peek().get();
    }

    /** 获取底部activity */
    public static Activity bottom() {
        expungeStaleActivities();
        return activities.isEmpty() ? null : activities.peekLast().get();
    }

    /** 判断是否是相同的activity */
    private static boolean isMatchActivity(Activity activity, Class<?> clazz) {
        if (activity.getClass() == clazz) return true;
        return false;
    }

    private static Activity findActivity(Class<?> clazz, Iterator<WeakReference<Activity>> iter) {
        while (iter.hasNext()) {
            WeakReference<Activity> ref = iter.next();
            Activity activity = ref.get();
            if (isActivityFinished(activity)) {
                iter.remove();
                continue;
            }
            if (isMatchActivity(activity, clazz)) {
                return activity;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static synchronized <A extends Activity> A top(Class<A> clazz) {
        return (A)findActivity(clazz, activities.iterator());
    }

    @SuppressWarnings("unchecked")
    public static synchronized <A extends Activity> A bottom(Class<A> clazz) {
        return (A)findActivity(clazz, activities.descendingIterator());
    }

    public static void finishTo(Class<?> clazz) {
        expungeStaleActivities();
        for(;;) {
            if (activities.isEmpty()) return;
            WeakReference<Activity> ref = activities.pop();
            Activity activity = ref.get();
            if (activity == null) continue;
            if (activity.isFinishing()) continue;
            if (! isMatchActivity(activity, clazz)) {
                activity.finish();
            } else {
                activities.push(ref);
                return;
            }
        }
    }

    public static void finishActivity(Class<? extends Activity> clazz) {
        Iterator<WeakReference<Activity>> iter = activities.iterator();
        while (iter.hasNext()) {
            WeakReference<Activity> ref = iter.next();
            Activity activity = ref.get();
            if (isActivityFinished(activity)) {
                iter.remove();
                continue;
            }
            if (activity.getClass() == clazz) {
                activity.finish();
                iter.remove();
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public static void finishAllActivity() {
        WeakReference<Activity> ref;
        while((ref = activities.poll()) != null) {
            Activity activity = ref.get();
            if (isActivityFinished(activity)) continue;
            activity.finish();
        }
    }

    private static boolean isActivityFinished(Activity activity) {
        if (activity == null) return true;
        if (activity.isFinishing()) return true;
        if (Build.VERSION.SDK_INT >= 17) {
            if (activity.isDestroyed()) return true;
        }
        return false;
    }

    /** 移除已经被系统回收的activity */
    private static void expungeStaleActivities() {
        Iterator<WeakReference<Activity>> iter = activities.iterator();
        while (iter.hasNext()) {
            WeakReference<Activity> ref = iter.next();
            Activity activity = ref.get();
            if (isActivityFinished(activity)) {
                iter.remove();
            }
        }
    }

    /** 初始化,在application里调用 */
    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    static class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            push(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }
    }

}
