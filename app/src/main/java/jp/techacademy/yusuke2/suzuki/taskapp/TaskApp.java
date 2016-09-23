package jp.techacademy.yusuke2.suzuki.taskapp;

/**
 * Created by yusus on 2016/09/21.
 */

//Realmは1データベースにつき、1ファイル。今回はデフォルトのデータベースを使用。


import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TaskApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}