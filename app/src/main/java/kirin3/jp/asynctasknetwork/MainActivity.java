package kirin3.jp.asynctasknetwork;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAsyncTask mMyAsyncTask;

                // Activityに設置してあるTextViewを取得
                TextView textView = (TextView)findViewById(R.id.textView);

                // TextViewの文字列をセット
                textView.setText("読み込み開始...");

                // 非同期(スレッド)処理クラスの生成
                mMyAsyncTask = new MyAsyncTask(mActivity);

                // 非同期(スレッド)処理の実行
                mMyAsyncTask.execute();
            }
        });

    }
}
