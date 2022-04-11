package Monopoly;

import java.util.Random;

public class Dice{
    public static int rollDice(){
        Random random=new Random();
        while(true){
            int tempInt=random.nextInt(6);
            if(tempInt!=0)
                return tempInt;
        }
    }
}
