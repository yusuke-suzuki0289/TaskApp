package jp.techacademy.yusuke2.suzuki.taskapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {

    private EditText mCategoryEdit2;
    private View.OnClickListener mOnSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //ここに検索処理を記載する。
            //参考URL：https://realm.io/jp/docs/java/latest/#section-23

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
