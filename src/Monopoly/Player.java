package Monopoly;

import java.util.ArrayList;

public class Player{
    protected String name;
    protected int money;
    protected int currentLand;
    protected int number;
    protected int sleep;
    protected boolean bankruptcy;
    protected ArrayList<Integer> boughtLands;
    Player(String name,int number){
        this.name=name;
        this.number=number;
        money=20000;
        currentLand=0;
        sleep=0;
        boughtLands=new ArrayList<>();
        bankruptcy=false;
    }
    public void walk(){
        if(currentLand==19){
            gainMoney(3000);
            currentLand=0;
        }
        else
            currentLand++;
    }

    public String getName(){
        return name;
    }

    public int getMoney(){
        return money;
    }

    public int getCurrentLand(){
        return currentLand;
    }

    public int getNumber(){
        return number;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setMoney(int money){
        this.money=money;
    }

    public void setCurrentLand(int currentLand){
        this.currentLand=currentLand;
    }

    public void setNumber(int number){
        this.number=number;
    }

    public void gainMoney(int money){
        this.money+=money;
    }

    public void giveAway(int money){
        this.money-=money;
    }
}
