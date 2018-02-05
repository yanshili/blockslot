package blockslot.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import blockslot.Blockslot;
import blockslot.annotations.MethodSlot;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main)
    protected TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.tv_main)
    protected void onTvMainClick(){
        toast((String) Blockslot.invokeS("app#get","on clicks !!!"));
        Test test=Blockslot.newInstance("test#constructor",10);
        Log.e("MainActivity","test newInstance times=="+test.getTimes());
        int t=20;
        test.setTimes(5);
        Blockslot.invoke(test, "test#setTimes", t);
        Log.e("MainActivity","test invoke times=="+t);
        int times=Blockslot.invoke(test,"test#getTimes");
        Log.e("MainActivity","test invoke times=="+times);
    }


    protected void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @MethodSlot("app#get")
    public static String getMessage(String m){
        return "slot message=="+m;
    }
}
