package org.techtown.androidmysql.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.techtown.androidmysql.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;


public class login extends AppCompatActivity {



    //접속정보와 태그명 지정
    private static String IP_ADDRESS = "15.164.218.247";
    private static String TAG = "logintest";

    //뷰 객체 생성
    EditText idtext,passwordtext,resulttext;//이메일 ,비밀번호, 비밀번호를 확인하기 위한 ecittext
    Button loginbutton,guestloginbutton,signbutton; //로그인, 게스트로그인, 회원가입 버튼

    //변수 객체 생성
    String email,password; //아이디, 암호, 암호재입력 정보 String


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


/*        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }*/


        출처: https://kwon8999.tistory.com/entry/KAKAO-Developers-애플리케이션-만들기안드로이드 [Kwon's developer]

        idtext=(EditText)findViewById(R.id.id); //이메일
        passwordtext=(EditText)findViewById(R.id.password); //암호
        resulttext=(EditText)findViewById(R.id.result); //결과내용 반영 (중요)

        loginbutton=(Button)findViewById(R.id.loginbutton); //로그인 버튼
        guestloginbutton=(Button)findViewById(R.id.guestloginbutton); //게스트 로그인 버튼
        signbutton=(Button)findViewById(R.id.signbutton); //회원가입 버튼



        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = idtext.getText().toString(); //아이디
                password = passwordtext.getText().toString(); //비밀번호

                login.InsertData task = new login.InsertData(); //AsynkTask를 이어 받는 InsertData를 실행한다.
                task.execute("http://" + IP_ADDRESS + "/newloginaction.php",email,password); //생성된 task를 실행. 여기에서 String입력값들을 차례대로 넣어 준다. (주소,이름,국가)

                //버튼을 누를 때마다 기존 내용 초
                // 기화
                idtext.setText("");
                passwordtext.setText("");
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog; //대기 프로그래스바를 생성

        @Override //Task시작전프로그래스바 객체를 화면에 표시
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(login.this,
                    "Please Wait", null, true, true);
        }


        @Override //Task실행후에 대한 처리로 : 결과값을 받아오고, 이를 mTextViewResult에 담는다. 그리고 로그로 출력.
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            resulttext.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override //실행되는 중에 백그라운드로 post값에 다담을 값을 넣는다.
        protected String doInBackground(String... params) {

            //들어가는 값 설정 이메일, 암호, 암호재입력값.
            String email = (String)params[1];
            Log.d(TAG, "POST  - " + email);

            String password = (String)params[2];
            Log.d(TAG, "POST  - " + password);

            String serverURL = (String)params[0];
            Log.d(TAG, "POST  - " + serverURL);
            String postParameters = "email=" + email + "&password=" + password;


            try {

                URL url = new URL(serverURL); //url객체 생성
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //연결 객체 생성??


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST"); //POST값으로 전달
                httpURLConnection.connect(); //연결.


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //UTF-8형태로 전송.
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream(); //연결이 성공되면 inputstream에 httpURLConnection에 담긴 값을 넣는다.
                }
                else{
                    inputStream = httpURLConnection.getErrorStream(); //연결이 안되면 inputstream에 에러값을 넣는다.
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){ //해당 라인이 null이 안될때까지 StringBuilder객체에 읽어들인다.
                    sb.append(line); //읽어온 라인을 계속해서 추가
                    Log.d(TAG, "sb while" + sb.toString());
                }


                bufferedReader.close();


                return sb.toString(); //sb를 문자열값으로 변환하여 반환.


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
