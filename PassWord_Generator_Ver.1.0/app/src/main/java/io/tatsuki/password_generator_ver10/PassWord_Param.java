package io.tatsuki.password_generator_ver10;

/**
 * Created by ROBO-E-01 on 2015/09/17.
 */
public class PassWord_Param {

    private String num;                             // 文字種(数字)
    private String upp;                             // 文字種(英大文字)
    private String low;                             // 文字種(英小文字)
    private String sym;                             // 文字種(記号)
    private int digit;                             // 桁数
    private int toggle_count;                     // トグルボタンのフラグ
    private String password;                       // パスワード
    private String memo;                            // メモ
    private String date;                            // 日時
    private String list_value;                     // リスト項目の値

    // 項目値のgetter
    public String getList_value() {
        return this.list_value;
    }
    // 項目値のsetter
    public  void setList_value(String list_value) {
        this.list_value = list_value;
    }
    // 日時のgetter
    public String getDate() {
        return this.date;
    }
    // 日時のsetter
    public void setDate(String date) {
        this.date = date;
    }
    // メモのgetter
    public String getMemo() {
        return this.memo;
    }
    // メモのsetter
    public void setMemo(String memo) {
        this.memo = memo;
    }
    // パスワードのgetter
    public String getPassword() {
        return this.password;
    }
    // パスワードのsetter
    public void setPassword(String password) {
        this.password = password;
    }
    // トグルボタンフラグのgetter
    public int getToggle_count() {
        return toggle_count;
    }
    // トグルボタンフラグのsetter
    public void setToggle_count(int toggle_count) {
        this.toggle_count = toggle_count;
    }
    // 数字getter
    public String getNum() {
        return num;
    }
    // 数字setter
    public void setNum(String num) {
        this.num = num;
    }
    // 英大文字getter
    public String getUpp() {
        return upp;
    }
    // 英大文字setter
    public void setUpp(String upp) {
        this.upp = upp;
    }
    // 英小文字getter
    public String getLow() {
        return low;
    }
    // 英小文字setter
    public void setLow(String low) {
        this.low = low;
    }
    // 記号getter
    public String getSym() {
        return sym;
    }
    // 記号setter
    public void setSym(String sym) {
        this.sym = sym;
    }
    // 桁数getter
    public int getDigit() {
        return digit;
    }
    // 桁数setter
    public void setDigit(int digit) {
        this.digit = digit;
    }

}
