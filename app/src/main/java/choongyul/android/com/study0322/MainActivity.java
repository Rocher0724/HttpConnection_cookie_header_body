package choongyul.android.com.study0322;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SingletonRequestQueue srq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 어플리케이션 컨텍스트를 사용하면 생명주기를 어플리케이션과 같이하게된다.
        // 많은 발리통신을 할때 사용하는것이 좋다.
        srq = SingletonRequestQueue.getInstance(getApplicationContext());
    }
}
