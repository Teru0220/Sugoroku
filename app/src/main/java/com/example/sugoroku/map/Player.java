package com.example.sugoroku.map;

public class Player {
    private String name;
    private int money;
    private boolean clear = false;
    private PlayerIcon icon;

    public Player(String name, int money, PlayerIcon icon){
        setName(name);
        setMoney(money);
        setIcon(icon);
    }

    public String getName(){ return this.name; }
    public void setName(String event){this.name = name;}
    public int getMoney(){ return this.money; }
    public void setMoney(int money){this.money = money;}
    public boolean isClear(){return this.clear;}
    public void setClear(boolean clear){ this.clear = clear;}
    public PlayerIcon getIcon(){return this.icon;}
    public void setIcon(PlayerIcon icon){ this.icon = icon;}
}
