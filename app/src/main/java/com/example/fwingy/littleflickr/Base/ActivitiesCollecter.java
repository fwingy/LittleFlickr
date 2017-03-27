

package com.example.fwingy.littleflickr.Base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity管理类
 * Created by fwingy on 2017/2/10.
 */

public class ActivitiesCollecter {
    public static List<Activity> activities = new ArrayList<>();

    /**
     * 在avtivity创建时（onCreate）调用
     * @param activity
     */
    public static void addActivities(Activity activity) {
        activities.add(activity);
    }

    /**
     * 在avtivity摧毁时（onDestroy）调用
     * @param activity
     */
    public static void removeActivities(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 调用以随时退出程序
     */
    public static void finishAllActivities() {
        for (Activity activity : activities
             ) {if (!activity.isFinishing()) {
            activity.finish();
        }

        }
    }
}
