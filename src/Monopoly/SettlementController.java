package Monopoly;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class SettlementController{
    public static ImageView img1;
    public static ImageView img2;
    public static ImageView img3;
    public static ImageView img4;
    public static TextField textField;
    protected static ArrayList<ImageView> imgList;
    public  void show(int player){
        imgList.add(img1);
        imgList.add(img2);
        imgList.add(img3);
        imgList.add(img4);
        imgList.get(player).setVisible(true);
        textField.setText("Player"+(player+1)+" Win!");
    }
}
