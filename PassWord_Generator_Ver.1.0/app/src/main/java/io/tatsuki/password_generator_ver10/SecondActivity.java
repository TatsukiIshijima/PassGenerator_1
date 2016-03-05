package io.tatsuki.password_generator_ver10;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by TI on 2015/09/22.
 */
public class SecondActivity extends Activity {

    final PassWord_Param param = new PassWord_Param();
    static final int CONTEXT_MENU1_ID = 0;
    static final int CONTEXT_MENU2_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // リストビューのインスタンス
        final ListView list1 = (ListView) findViewById(R.id.list1);
        // リストのアダプター(layoutはリストのためのファイル)
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.row_textview1);
        // 検索Buttonのインスタンス
        final Button s_button = (Button) findViewById(R.id.s_button);
        // 検索Boxのインスタンス
        final EditText editText = (EditText)findViewById(R.id.sec_edit_text);

        // 検索ワードなしの状態でリスト表示
        try {
            //for (int i = 0; i < loadData(editText.getText().toString()).size(); i++) {
            for (int i = 0; i < loadData("").size(); i++) {
                adapter.add(loadData("").get(i));
            }
            list1.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // リスト項目が長押された時
        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                // ここでリストビューの値を取得しなければならない
                String item = (String) listView.getItemAtPosition(position);
                String[] items = item.split("\n");
                // パスワードの値のセット
                param.setPassword(items[0]);
                //Toast.makeText(getApplicationContext(), param.getPassword() + " clicked",Toast.LENGTH_SHORT).show();
                //Viewに追加する場合、registerForContextMenu(View);が必要
                registerForContextMenu(listView);
                return false;
            }
        });

        // 検索ボタンが押された時
        s_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンにフォーカスを移動させる
                //s_button.setFocusable(true);
                //s_button.setFocusableInTouchMode(true);
                //s_button.requestFocus();
                // リストを初期化
                adapter.clear();
                try {
                    for (int i = 0; i < loadData(editText.getText().toString()).size(); i++) {
                        adapter.add(loadData(editText.getText().toString()).get(i));
                    }
                    list1.setAdapter(adapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        /*
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    // 処理を行う
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add("パスワード生成＆保存へ");
        menu.add("パスワード全表示");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // リストビューのインスタンス
        final ListView pass_list = (ListView) findViewById(R.id.list1);
        // リストのアダプター(layoutはリストのためのファイル)
        final ArrayAdapter<String> pass_adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.row_textview1);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            this.moveTaskToBack(true);
        }
        if ("パスワード生成＆保存へ".equals(item.getTitle())) {
            Intent intent = new Intent();
            intent.setClassName("io.tatsuki.password_generator_ver10", "io.tatsuki.password_generator_ver10.MainActivity");
            startActivity(intent);
        }

        if ("パスワード全表示".equals(item.getTitle())) {
            // 検索ワードなしの状態でリスト表示
            try {
                //for (int i = 0; i < loadData(editText.getText().toString()).size(); i++) {
                for (int i = 0; i < loadData("").size(); i++) {
                    pass_adapter.add(loadData("").get(i));
                }
                pass_list.setAdapter(pass_adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // データベースから検索をかける
    private ArrayList<String> loadData(String data) throws IOException {
        MyDatabasehelper helper = new MyDatabasehelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //String query = "select * from " + MyDatabasehelper.TABLE_NAME + " where " + MyDatabasehelper.PASSWORD + " like '%" + data + "%';";
        String query = "select * from " + MyDatabasehelper.TABLE_NAME + " where " + MyDatabasehelper.PASSWORD + " like '%" + data + "%'" + " order by " + MyDatabasehelper.ID + " desc;";
        ArrayList<String> result = new ArrayList<String>();
        if (db != null) {
            Cursor c = db.rawQuery(query, null);

            while(c.moveToNext()) {
                int p = c.getColumnIndex(MyDatabasehelper.PASSWORD);
                int m = c.getColumnIndex(MyDatabasehelper.MEMO);
                int d = c.getColumnIndex(MyDatabasehelper.DATETIME);
                String pass = c.getString(p);
                String memo = c.getString(m);
                String date = c.getString(d);

                String row = pass + "\n" + "メモ:" + memo + "\n" + "保存日:" + date;

                result.add(row);
            }
        }
        return result;
    }

    // データベースのデータを削除
    private void deleteData(String password) throws IOException {
        MyDatabasehelper helper = new MyDatabasehelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(MyDatabasehelper.TABLE_NAME, MyDatabasehelper.PASSWORD + " like '%" + password + "%'", null);
    }

    // コンテキストメニュー
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //コンテキストメニューの設定
        menu.setHeaderTitle("削除しますか？");
        //Menu.add(int groupId, int itemId, int order, CharSequence title)
        menu.add(0, CONTEXT_MENU1_ID, 0, "Yes");
        menu.add(0, CONTEXT_MENU2_ID, 0, "No");
    }

    // コンテキストメニューが押された時の処理
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // リストのアダプター(layoutはリストのためのファイル)
        final ArrayAdapter<String>con_adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.row_textview1);
        // リストビューのインスタンス
        final ListView con_list = (ListView) findViewById(R.id.list1);

        switch (item.getItemId()) {
            case CONTEXT_MENU1_ID:
                //削除＝Yesの押下時の操作
                try {
                    deleteData(param.getPassword());
                    Toast.makeText(getApplicationContext(), param.getPassword() + "を削除しました。", Toast.LENGTH_SHORT).show();
                    // 検索ワードなしの状態でリスト表示
                    try {
                        for (int i = 0; i < loadData("").size(); i++) {
                            con_adapter.add(loadData("").get(i));
                        }
                        con_list.setAdapter(con_adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case CONTEXT_MENU2_ID:
                //削除＝Noの押下時の操作
                //Toast.makeText(getApplicationContext() ,"NO", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
