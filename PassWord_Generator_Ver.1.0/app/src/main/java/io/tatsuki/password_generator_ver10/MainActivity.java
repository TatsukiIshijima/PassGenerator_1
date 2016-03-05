package io.tatsuki.password_generator_ver10;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class MainActivity extends Activity  {

    Random random = new Random();
    final PassWord_Param param = new PassWord_Param();

    final String number = "0123456789";
    final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String lower = "abcdefghijklmnopqrstuvwxyz";
    final String symbol = "!#$%&()=-_~^+@*?[]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 作成Buttonのインスタンス
        final Button c_button = (Button) findViewById(R.id.create_button);
        // 登録Buttonのインスタンス
        final Button r_button = (Button) findViewById(R.id.regist_button);
        // 桁数Spinnerのインスタンス
        final Spinner spinner = (Spinner) findViewById(R.id.digit_spinner);
        // テキストボックスのインスタンス
        final TextView textView = (TextView) findViewById(R.id.textview1);
        // テキストボックスに下線を引く
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // トグルボタンのインスタンス
        final ToggleButton num_tbutton = (ToggleButton)findViewById(R.id.num_tbutton);
        final ToggleButton upp_tbutton = (ToggleButton)findViewById(R.id.upp_tbutton);
        final ToggleButton low_tbutton = (ToggleButton)findViewById(R.id.low_tbutton);
        final ToggleButton sym_tbutton = (ToggleButton)findViewById(R.id.sym_tbutton);
        // メモテキストのインスタンス
        final EditText memoText = (EditText)findViewById(R.id.memo_text);

        // トグルボタンの初期値
        num_tbutton.setChecked(true);
        upp_tbutton.setChecked(true);
        low_tbutton.setChecked(true);
        sym_tbutton.setChecked(true);

        r_button.setEnabled(false);

        // パラメータセット
        param.setNum(number);
        param.setUpp(upper);
        param.setLow(lower);
        param.setSym(symbol);

        // アダプターに項目を追加(パスワードの桁数)
        String[] items = {"4","6","8","12","16"};
        // アダプターの設定(spinner_list)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_list, items);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        // アダプターをspinnerにセット
        spinner.setAdapter(adapter);
        // 桁数Spinnerが選択されたとき
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 選択された桁数を取得
                String digit = (String)spinner.getSelectedItem();
                // パラメータにセット
                param.setDigit(Integer.parseInt(digit));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 作成ボタンが押されたとき
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r_button.setEnabled(true);
                // トグルボタンのどれかが押されているとき
                if (num_tbutton.isChecked() || upp_tbutton.isChecked() || low_tbutton.isChecked() || sym_tbutton.isChecked()) {

                    // 文字種格納用Buffer
                    StringBuffer buffer = new StringBuffer();
                    // 生成したパスワード格納用String
                    String password = new String();

                    // 数字トグルボタンが押されたとき
                    if (num_tbutton.isChecked()) {
                        param.setNum(number);
                        buffer.append(param.getNum());
                    }
                    // 英大文字トグルボタンが押されたとき
                    if (upp_tbutton.isChecked()) {
                        param.setUpp(upper);
                        buffer.append(param.getUpp());
                    }
                    // 英小文字トグルボタンが押されたとき
                    if (low_tbutton.isChecked()) {
                        param.setLow(lower);
                        buffer.append(param.getLow());
                    }
                    // 記号トグルボタンが押されたとき
                    if (sym_tbutton.isChecked()) {
                        param.setSym(symbol);
                        buffer.append(param.getSym());
                    }
                    // パスワード生成
                    for (int k=0; k<param.getDigit(); k++) {
                        password += buffer.charAt((int)(Math.random()*buffer.length()));
                    }
                    // パスワードのセット
                    param.setPassword(password);
                    // テキストボックスに表示
                    textView.setText(param.getPassword());
                } else {
                    r_button.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"文字種を選択してください。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 登録ボタンが押された時
        r_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // メモ書きのセット
                param.setMemo(memoText.getText().toString());

                try {
                    if (saveData(param.getPassword(), param.getMemo()) != -1) {
                        Toast.makeText(getApplicationContext(),"保存しました。", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // インテントで画面遷移
                Intent intent = new Intent();
                intent.setClassName("io.tatsuki.password_generator_ver10", "io.tatsuki.password_generator_ver10.SecondActivity");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add("パスワード検索＆一覧へ");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            this.moveTaskToBack(true);
        }
        if ("パスワード検索＆一覧へ".equals(item.getTitle())) {
            Intent intent = new Intent();
            intent.setClassName("io.tatsuki.password_generator_ver10", "io.tatsuki.password_generator_ver10.SecondActivity");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // データベース保存
    private long saveData(String password ,String memo) throws IOException {
        //String time = Calendar.getInstance().getTime().toString();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String time = sdf.format(date);
        MyDatabasehelper helper = new MyDatabasehelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabasehelper.PASSWORD, password);
        values.put(MyDatabasehelper.MEMO, memo);
        values.put(MyDatabasehelper.DATETIME, time);
        long result = -1;
        if (db != null) {
            result = db.insert(MyDatabasehelper.TABLE_NAME, null, values);
        }
        return result;
    }
}