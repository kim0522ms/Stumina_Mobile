package com.example.kms.stumina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_FAILED = 3;

    private String userid;
    private String userpasswd;
    private String autologin;
    private SharedPreferences autoLoginInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        autoLoginInfo = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        userid  = autoLoginInfo.getString("userid","");
        userpasswd = autoLoginInfo.getString("userpasswd","");
        autologin = autoLoginInfo.getString("auto","");

        Button.OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                EditText inputbox_id = (EditText)findViewById(R.id.inputbox_id);
                userid = inputbox_id.getText().toString();
                EditText inputbox_pw = (EditText)findViewById(R.id.inputbox_passwd);
                userpasswd = inputbox_pw.getText().toString();

                if (inputbox_id.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("아이디를 입력해 주세요!!");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.show();
                    return;
                }
                else if (inputbox_pw.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("패스워드를 입력해 주세요!!");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.show();
                    return;
                }
                else
                {
                    //ProgressDialog asyncDialog = ProgressDialog.show(LoginActivity.this, "로그인", "로그인 중입니다....", true, true);
                    tryLogin(userid,userpasswd);
                    //asyncDialog.dismiss();
                }
            }
        };

        findViewById(R.id.button_login).setOnClickListener(mClickListener);

        if (userid != null && userpasswd != null && autologin.equals("true"))
        {
            //ProgressDialog asyncDialog = ProgressDialog.show(LoginActivity.this, "로그인", "로그인 중입니다....", true, true);
            tryLogin(userid,userpasswd);
           // asyncDialog.dismiss();
        }
    }

    private void tryLogin(String userid, String userpasswd){
        try {
            String loginResult =  new ConnectTask().execute(MainActivity.server_url + "/mobile/login","user_id="+userid + "&user_pw=" + userpasswd).get();

            if (loginResult != null)
            {
                System.out.println(loginResult);

                JSONObject returnedJobj = new JSONObject(loginResult);

                Object temp = returnedJobj.get("user_idx");
                if (temp != null && !(temp.toString().equals("NO_MEMBER"))) {

                    UserInfo userInfo = new UserInfo();

                    userInfo.setUser_idx(returnedJobj.get("user_idx").toString());
                    userInfo.setUser_area(returnedJobj.get("user_area").toString());
                    userInfo.setUser_belong(returnedJobj.get("user_belong").toString());
                    userInfo.setUser_jobno(returnedJobj.get("user_jobno").toString());
                    userInfo.setUser_name(returnedJobj.get("user_name").toString());
                    userInfo.setUser_phone(returnedJobj.get("user_phone").toString());
                    userInfo.setUser_sex(returnedJobj.get("user_sex").toString());

                    MainActivity.userInfo = userInfo;

                    Intent intent = new Intent();
                    intent.putExtra("user_idx", userInfo.getUser_idx());
                    setResult(LOGIN_SUCCESS, intent);

                    if (((CheckBox)findViewById(R.id.checkbox_autologin)).isChecked())
                    {
                        SharedPreferences.Editor editor = autoLoginInfo.edit();
                        editor.putString("userid",userid);
                        editor.putString("userpasswd",userpasswd);
                        editor.putString("auto", "true");
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "자동 로그인 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    return;
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("로그인에 실패했습니다!");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    builder.show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error");
                builder.setMessage("서버와의 연결에 실패했습니다!\n인터넷 접속 상태를 확인해 주세요!");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
