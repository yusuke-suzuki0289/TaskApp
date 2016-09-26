package jp.techacademy.yusuke2.suzuki.taskapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {

    private Realm mRealm2;
    private RealmResults<Task> mTaskRealmResults2;
    private EditText mCategoryEdit2;
    private View.OnClickListener mOnSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //ここに検索処理を記載する。
            //参考URL：https://realm.io/jp/docs/java/latest/#section-23

            // Build the query looking at all users:
            mRealm2 = Realm.getDefaultInstance();
            RealmQuery<Task> query = mRealm2.where(Task.class);

            // Add query conditions:
            query.equalTo("category", String.valueOf(mCategoryEdit2));

            // Execute the query:
            RealmResults<Task> mTaskRealmResults2 = query.findAll();
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCategoryEdit2 = (EditText) findViewById(R.id.category_edit_text2);
        findViewById(R.id.search_button).setOnClickListener(mOnSearchClickListener);

    }
}
