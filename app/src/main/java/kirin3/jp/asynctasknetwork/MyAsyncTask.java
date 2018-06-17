package kirin3.jp.asynctasknetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyAsyncTask extends AsyncTask<Void, Void, String> {

    private Activity       mActivity;
    public  ProgressDialog mProgressDialog;
    /*
     * コンストラクタ
     *
     *  @param activity: 読み出し元Activity
     */
    public MyAsyncTask(Activity activity) {

        // 呼び出し元のアクティビティを変数へセット
        this.mActivity = activity;
    }


    /*
     * 実行前の事前処理
     */
    @Override
    protected void onPreExecute() {

        // プログレスダイアログの生成
        this.mProgressDialog = new ProgressDialog(this.mActivity);

        // プログレスダイアログの設定
        this.mProgressDialog.setMessage("読み込み中...");  // メッセージをセット

        // プログレスダイアログの表示
        this.mProgressDialog.show();

        return;
    }

    /*
     * バックグラウンドで実行する処理
     *
     *  @param params: Activityから受け渡されるデータ
     *  @return onPostExecute()へ受け渡すデータ
     */
    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection con = null;
        URL url = null;
        String urlSt = "http://zipcloud.ibsnet.co.jp/api/search?zipcode=1640003";

        try {
            // URLの作成
            url = new URL(urlSt);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection)url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("POST");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(true);

            // 接続
            con.connect(); // ①

            // 本文の取得
            InputStream in = con.getInputStream();
            String readSt = readInputStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String readInputStream(InputStream in) throws IOException{
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while((st = br.readLine()) != null)
        {
            Log.w( "DEBUG_DATA", "st = " + st);
            sb.append(st);
        }
        try
        {
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Log.w( "DEBUG_DATA", "sb.toString() = " + sb.toString());
        return sb.toString();
    }

    /*
     * メインスレッドで実行する処理
     *
     *  @param param: doInBackground()から受け渡されるデータ
     */
    @Override
    protected void onPostExecute(String result) {

        // 読み出し元Activityに設置してあるTextViewを取得
        TextView textView = (TextView)this.mActivity.findViewById(R.id.textView);

        // TextViewの文字列をセット
        textView.setText(result);

        // プログレスダイアログを閉じる
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }

        return;
    }

    /*
     * キャンセル時の処理
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();

        Log.v("DEBUG_DATA", "onCancelled()");

        if (this.mProgressDialog != null) {

            Log.v("DEBUG_DATA", String.valueOf(this.mProgressDialog.isShowing()));

            // プログレスダイアログ表示中の場合
            if (this.mProgressDialog.isShowing()) {

                // プログレスダイアログを閉じる
                this.mProgressDialog.dismiss();
            }
        }

        return;
    }
}
