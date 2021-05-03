package com.example.sugoroku.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sugoroku.layout.MakeMapMenuWindow;
import com.example.sugoroku.layout.PlaySettingWindow;
import com.example.sugoroku.make_map.MakeMapActivity;
import com.example.sugoroku.map.MapActivity;
import com.example.sugoroku.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    Context context;//intent用コンテキスト
    private ArrayList<MenuData> list;
    MakeMapMenuWindow makeMapMenuWindow;
    PlaySettingWindow playSettingWindow;
    View view;
    int masuTotal;

    public MenuAdapter(ArrayList<MenuData> list, Context context, MakeMapMenuWindow makeMapMenuWindow, PlaySettingWindow playSettingWindow){
        this.context = context;
        this.list = list;
        this.makeMapMenuWindow = makeMapMenuWindow;
        this.playSettingWindow = playSettingWindow;
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
        //settingモード用に新規作成項目を追加
        if(MenuListActivity.mode.equals("setting") && position == list.size()){
            holder.title.setText("新しいマップ");
            masuTotal = 26;
            String s = " ";
            holder.masuTotal.setText(s);
            if(masuTotal == 26){
                holder.imageView.setImageResource(R.drawable.map24);
            }
        }else {
            holder.title.setText(list.get(position).getTableName());
            masuTotal = list.get(position).getMasuTotal();
            String s = "総マス数：" + masuTotal;
            holder.masuTotal.setText(s);
            if (masuTotal == 26) {
                holder.imageView.setImageResource(R.drawable.map24);
            }
        }
        //settingモード用とPlayモード用で設定を変更
        if(MenuListActivity.mode.equals("play")) {
            holder.base.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSettingWindow.visible();
                    Button button = playSettingWindow.getButton();
                    Spinner spinner = playSettingWindow.getSpinner();
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int playCPU = 0;
                            if (spinner.getSelectedItem().toString().equals("プレイヤー 3人、CPU 1人")) {
                                playCPU = 1;
                            } else if (spinner.getSelectedItem().toString().equals("プレイヤー 2人、CPU 2人")) {
                                playCPU = 2;
                            } else if (spinner.getSelectedItem().toString().equals("プレイヤー 1人、CPU 3人")) {
                                playCPU = 3;
                            }
                            playSettingWindow.invisible();

                            Intent map26 = new Intent(context, MapActivity.class);
                            map26.putExtra("mapName", list.get(position).getTableName());
                            map26.putExtra("masuTotal", masuTotal);
                            map26.putExtra("playCPU", playCPU);
                            map26.putExtra("playerName", playSettingWindow.getName());
                            context.startActivity(map26);
                        }
                    });
                }
            });
            //settingモード用のクリックイベント
        }else {
            holder.base.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == list.size()) {
                        makeMapMenuWindow.visible();
                        Button button = makeMapMenuWindow.getButton();
                        Spinner spinner = makeMapMenuWindow.getSpinner();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mapName = makeMapMenuWindow.getMapName();
                                int masuTotal = Integer.parseInt(spinner.getSelectedItem().toString());
                                makeMapMenuWindow.invisible();
                                Intent map26set = new Intent(context, MakeMapActivity.class);
                                map26set.putExtra("mapName", mapName);
                                map26set.putExtra("masuTotal", masuTotal);
                                map26set.putExtra("read",false);
                                context.startActivity(map26set);
                            }
                        });
                    }else {
                        String mapName = list.get(position).getTableName();
                        int masuTotal = list.get(position).getMasuTotal();
                        makeMapMenuWindow.invisible();
                        Intent map26set = new Intent(context, MakeMapActivity.class);
                        map26set.putExtra("mapName", mapName);
                        map26set.putExtra("masuTotal", masuTotal);
                        map26set.putExtra("read",true);
                        context.startActivity(map26set);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(MenuListActivity.mode.equals("play")) {
            return list.size();
        }else {
            return list.size() +1;
        }
    }
}
