package blockslot.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        toast((String) Blockslot.invoke(this,"app#get","on click !!!"));
    }


    protected void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @MethodSlot("app#get")
    private String getMessage(String m){
        return "slot message=="+m;
    }
}
