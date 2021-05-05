package com.example.sugoroku;


public class TestSQL {
    private static String money = "所持金が";
    private static String upMoney = "ふえた";
    private static String downMoney = "へった";
    private static String upMoneyEvent ="お金をひろった";
    private static String downMoneyEvent ="お金をおとした";


    public static String[] a = {"スタート",
            upMoneyEvent,
            "元気がありあまっている",
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            "忘れ物をした",
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            "落とし物をした",
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            upMoneyEvent,
            downMoneyEvent,
            "ゴール"};

    public static String[] b = {"　",
            money +"100" +upMoney,
            "2マス進む",
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+ upMoney,
            "5マス戻る",
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            "6マス戻る",
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney,
            money +"100"+upMoney,
            money +"-100"+downMoney
            ,"　"};


    public static int[] c = {0,100,2,100,-100,100,-100,100,  -5,100,-100,100,-100,100,-100,100,  -6,100,-100,100,-100,100,-100,100,  -100,0};
    public static int[] d = {0,0,2,0,0,0,0,0,  1,0,0,0,0,0,0,0,  1,0,0,0,0,0,0,0,  0,0};
    //北西南東の順に2357で表し進行可能な方角をかけ合わせる
    public static int[] upNextNumber ={-1,0,1,2,3,4,5,6,  -1,  10,11,12,13,14,15,16,-1,  -1,  -1,18,19,20,21,22,23,24};
    public static int[] leftNextNumber ={-1,-1,-1,-1,-1,-1,-1,-1,  7,  8,-1,-1,-1,-1,-1,-1,-1,  16,  17,-1,-1,-1,-1,-1,-1,-1};
    public static int[] downNextNumber ={1,2,3,4,5,6,7,-1,  -1,  -1,9,10,11,12,13,14,15,  -1,  19,20,21,22,23,24,25,-1};
    public static int[] rightNextNumber ={-1,-1,-1,-1,-1,-1,-1,8,  9,  -1,-1,-1,-1,-1,-1,-1,17,  18,  -1,-1,-1,-1,-1,-1,-1,-1};

}
