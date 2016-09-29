package jp.techacademy.yusuke2.suzuki.taskapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.RealmQuery;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    public final static String EXTRA_TASK = "jp.techacademy.yusuke2.suzuki.taskapp.TASK";

    private Realm mRealm;
    private RealmResults<Task> mTaskRealmResults;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            reloadListView();
        }
    };
    private ListView mListView;
    private TaskAdapter mTaskAdapter;

    //背景タップ制御対応（後日質問予定）
//    // キーボード表示を制御するためのオブジェクト
//    InputMethodManager inputMethodManager;
//    // 背景のレイアウト
//    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //背景タップ制御対応（後日質問予定）
//        setContentView(R.layout.activity_main);
//        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mainLayout = (CoordinatorLayout) findViewById(R.id.content_main);

        //タスク追加ボタンの定義
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });

        //EditText定義
        EditText edittext = (EditText)findViewById(R.id.editText);
        edittext.addTextChangedListener(this);

//        //インテントは使用しないため、コメントアウト
//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent2);
//            }
//        });

        // Realmのデータの削除
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfig);
//        mRealm = Realm.getInstance(realmConfig);

        //mRealmの初期状態を取得
        mRealm = Realm.getDefaultInstance();
        //Task.classの全件を取得
        mTaskRealmResults = mRealm.where(Task.class).findAll();
        //Task.classの全件をdataで降順ソート
        mTaskRealmResults.sort("date", Sort.DESCENDING);
        //Realmに反映された最新の内容を受け取るためにRealmResultsが更新されるたびに呼びだされる
        mRealm.addChangeListener(mRealmListener);

        // ListViewの設定
        mTaskAdapter = new TaskAdapter(MainActivity.this);
        mListView = (ListView) findViewById(R.id.listView1);

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                Task task = (Task) parent.getAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                intent.putExtra(EXTRA_TASK, task);

                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する

                final Task task = (Task) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("削除");
                builder.setMessage(task.getTitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Task> results = mRealm.where(Task.class).equalTo("id", task.getId()).findAll();

                        mRealm.beginTransaction();
                        results.clear();
                        mRealm.commitTransaction();

                        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class);
                        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                MainActivity.this,
                                task.getId(),
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(resultPendingIntent);

                        reloadListView();
                    }
                });
                builder.setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        reloadListView();

    }

        public void reloadListView() {

        ArrayList<Task> taskArrayList = new ArrayList<>();

        for (int i = 0; i < mTaskRealmResults.size(); i++) {
            Task task = new Task();

            task.setId(mTaskRealmResults.get(i).getId());
            task.setTitle(mTaskRealmResults.get(i).getTitle());
            task.setContents(mTaskRealmResults.get(i).getContents());
            task.setDate(mTaskRealmResults.get(i).getDate());
            //カテゴリ追加
            task.setCategory(mTaskRealmResults.get(i).getCategory());
            taskArrayList.add(task);
        }

        mTaskAdapter.setTaskArrayList(taskArrayList);
        mListView.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        //操作前のEtidTextの状態を取得する
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //操作中のEtidTextの状態を取得する
    }

//    @Override
    public void afterTextChanged(Editable s) {
        //操作後のEtidTextの状態を取得する

        //nullの場合は全件表示する
        if ("".equals(String.valueOf(s))) {
            RealmQuery<Task> query = mRealm.where(Task.class);
            mTaskRealmResults = query.findAll();
            reloadListView();

        //絞りこまれた場合カテゴリでRealmを検索する。
        }else {
            RealmQuery<Task> query = mRealm.where(Task.class);
            query.equalTo("category", String.valueOf(s));
            mTaskRealmResults = query.findAll();
            reloadListView();
        }
    }

    //背景タップ制御対応（後日質問予定）
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    // 画面タップ時の処理
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//// キーボードを隠す
//        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//// 背景にフォーカスを移す
//        mainLayout.requestFocus();
//
//        return true;
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }


}