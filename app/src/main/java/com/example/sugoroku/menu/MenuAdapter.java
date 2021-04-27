package com.example.sugoroku.menu;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sugoroku.map.MapActivity;
import com.example.sugoroku.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    Context context;//intent用コンテキスト
    private ArrayList<MenuData> list;
    View view;

    public MenuAdapter(ArrayList<MenuData> list,Context context){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override//新しいViewを作成する。レイアウトマネージャーに起動される
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //どこのアクティビティで使用するか指定。その後（使用するレイアウトファイル、表示するview,？）を指定。
        LayoutInflater inflater = LayoutInflater.from(context);//コンテキストにレイアウトファイルを入れる。
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new MenuViewHolder(view);
    }

    @Override//Viewの内容を交換する。レイアウトマネージャーに起動される
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        //holder.imageView.setImageDrawable();
        holder.title.setText(list.get(position).getTableName());
        int masuTotal = list.get(position).getMasuTotal();
        String s = "総マス数：" + masuTotal;
        holder.masuTotal.setText(s);
        if(masuTotal == 26){
            holder.imageView.setImageResource(R.drawable.map24);
        }
        holder.base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =
                        (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_view, null);
                popupView.setLayoutParams(new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                PopupWindow popupWindow = new PopupWindow(context);
                popupWindow.setContentView(popupView);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);
                Button button = popupView.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        Intent map26 = new Intent(context, MapActivity.class);
                        map26.putExtra("mapName",list.get(position).getTableName());
                        map26.putExtra("masuTotal",masuTotal);
                        context.startActivity(map26);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
