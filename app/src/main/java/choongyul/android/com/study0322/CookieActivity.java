package choongyul.android.com.study0322;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CookieActivity extends AppCompatActivity {
    EditText editUrl;
    Button btn;
    TextView cookieShower;
    Handler mHandler;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie);
        editUrl = (EditText) findViewById(R.id.editUrl);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetwork();
            }
        });

        cookieShower = (TextView) findViewById(R.id.cookieShower);
        mHandler = new Handler(Looper.getMainLooper());
        imageView = (ImageView) findViewById(R.id.image);
    }

    private void doNetwork() {
        new Thread(new mRunnable()).start();
    }
    private class mRunnable implements Runnable{

        @Override
        public void run() {
            CookieManager cMan = new CookieManager();
            //모든 쿠키를 수용한다
            cMan.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            URL url = null;
            HttpURLConnection con = null;
            int responConde = 0;
            String responseMessage = null;
            CookieHandler.setDefault(cMan);
            //URL CONNECTION
            try {
                String urll = editUrl.getText().toString();
                if( !urll.startsWith("http") ) {
                    urll = "http://" + urll;
                }

                url = new URL(urll);
                con = (HttpURLConnection) url.openConnection();

                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                con.setDoOutput(true);
                con.setDoInput(true);

                con.connect();
                final Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
                con.getHeaderFields().keySet().iterator();
                for( String s : con.getHeaderFields().keySet() ) {
                    Log.e(s, con.getHeaderFields().get(s).toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        editUrl.setText("");
                        Toast.makeText(CookieActivity.this, "올바른 URL을 입력해 주십시오", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(CookieActivity.this, "URL 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            try {
                responConde = con.getResponseCode();
                responseMessage= con.getResponseMessage();
            } catch(IOException e){
                e.printStackTrace();
                mHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(CookieActivity.this, "URL 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if(responConde != HttpURLConnection.HTTP_OK){
                con.disconnect();
                return;
            }
// 비트맵 펙토리에 디코드 스트림을 이미지로 바꿀수 있는 게 있다. (디코드스트림?)

//            BufferedInputStream bfis = null;
//            BufferedReader bfReader = null;
//            try {
//                byte[] buffer = new byte[30];
//                bfReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
//                bfis = new BufferedInputStream(con.getInputStream(),30);
//                String line = null;
//                int line1 = 0;
//                while( (line1 = bfis.read(buffer)) != -1){
//                    StringBuffer sb = new StringBuffer();
//                    for(byte a : buffer){
//                        sb.append(a);
//                    }
//                    Log.e("Body", sb.toString());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            CookieStore cStore = cMan.getCookieStore();
            final List<HttpCookie> cookies = cStore.getCookies();
            StringBuffer sBuffer = new StringBuffer();
            for(HttpCookie c : cookies){ // 쿠키는 스트링 리스트로 온다.
                Log.w("COOKIE",c.toString());
                sBuffer.append(c.toString());
            }
            final String s = sBuffer.toString();
            cookieShower.post(new Runnable() {
                @Override
                public void run() {
                    cookieShower.setText(s);
                    // 여기다가 버퍼가지고 body 받아온거 띄워볼거임.

                }
            });
        }
    }
}
