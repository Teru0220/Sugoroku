package com.example.sugoroku.map;

public class Player {
    private String name;
    private int money;
    private boolean goal = false;
    private PlayerIcon icon;
    private int imgNumber;
    private boolean cpu = false;

    public Player(String name, int money, PlayerIcon icon,int imgNumber){
        setName(name);
        setMoney(money);
        setIcon(icon);
        setImgNumber(imgNumber);
    }

    public String getName(){ return this.name; }
    public void setName(String name){this.name = name;}
    public int getMoney(){ return this.money; }
    public void setMoney(int money){this.money = money;}
    public boolean isGoal(){return this.goal;}
    public void setGoal(boolean goal){ this.goal = goal;}
    public PlayerIcon getIcon(){return this.icon;}
    public void setIcon(PlayerIcon icon){ this.icon = icon;}
    public int getImgNumber(){return this.imgNumber;}
    public void setImgNumber(int imgNumber){ this.imgNumber = imgNumber;}
    public boolean isCpu(){ return cpu; }
    public void setCpu(){this.cpu = true;}
}
