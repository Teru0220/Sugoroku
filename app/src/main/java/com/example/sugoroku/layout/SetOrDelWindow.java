package com.example.sugoroku.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sugoroku.R;

public class SetOrDelWindow extends LinearLayout {
    private TextView textView;
    private Button yes,no;
    private LinearLayout frame;
    private boolean setFlag;

    public SetOrDelWindow(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.set_or_del_window,this);

        this.frame = layout.findViewById(R.id.frame);
        this.textView = layout.findViewById(R.id.textView3);
        this.yes = layout.findViewById(R.id.button2);
        this.no = layout.findViewById(R.id.button3);
    }

    public void delText(){
        textView.setText("削除後、保存しなければデータは完全に消去されます");
        this.setFlag = false;
    }
    public void setText(){
        textView.setText("現在のデータに上書きされますがよろしいですか？");
        this.setFlag = true;
    }
    public Button getYes(){return yes;}
    public Button getNo(){return no;}
    public boolean isSetFlag(){return setFlag;}
    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }
}
