package Monopoly;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class MainGameController{
    public ImageView bigIcon1=new ImageView();
    public ImageView bigIcon2=new ImageView();
    public ImageView bigIcon3=new ImageView();
    public ImageView bigIcon4=new ImageView();
    public ImageView smallIcon1=new ImageView();
    public ImageView smallIcon2=new ImageView();
    public ImageView smallIcon3=new ImageView();
    public ImageView smallIcon4=new ImageView();
    public Button rollDice=new Button();
    public Button start=new Button();
    protected static ArrayList<ImageView> mapIcons=new ArrayList<>();
    protected static ArrayList<ImageView> headIcons=new ArrayList<>();
    public TextField dateTextField=new TextField();
    public TextArea outPutTextArea=new TextArea();
    public TextField nameField=new TextField();
    public TextField moneyField=new TextField();
    public Button choiceButtonY=new Button();
    public Button choiceButtonN=new Button();

    public void startButtonClick(ActionEvent actionEvent){
        Main.initialize();
        start.setVisible(false);
        start.setDisable(true);
        rollDice.setVisible(true);
        rollDice.setDisable(false);
        int playerNumber=Main.getPlayerNumber();
        if(playerNumber==2){
            headIcons.add(bigIcon1);
            headIcons.add(bigIcon2);
            mapIcons.add(smallIcon1);
            mapIcons.add(smallIcon2);
            smallIcon1.setVisible(true);
            smallIcon2.setVisible(true);
        }else if(playerNumber==3){
            headIcons.add(bigIcon1);
            headIcons.add(bigIcon2);
            headIcons.add(bigIcon3);
            mapIcons.add(smallIcon1);
            mapIcons.add(smallIcon2);
            mapIcons.add(smallIcon3);
            smallIcon1.setVisible(true);
            smallIcon2.setVisible(true);
            smallIcon3.setVisible(true);
        }else {
            headIcons.add(bigIcon1);
            headIcons.add(bigIcon2);
            headIcons.add(bigIcon3);
            headIcons.add(bigIcon4);
            mapIcons.add(smallIcon1);
            mapIcons.add(smallIcon2);
            mapIcons.add(smallIcon3);
            mapIcons.add(smallIcon4);
            smallIcon1.setVisible(true);
            smallIcon2.setVisible(true);
            smallIcon3.setVisible(true);
            smallIcon4.setVisible(true);
        }
    }
    public void rollDiceClick(ActionEvent actionEvent) throws InterruptedException{
        int dice=Dice.rollDice();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    if(Main.getCurrentPlayer()!=0){
                        headIcons.get(Main.getCurrentPlayer()-1).setVisible(false);
                        headIcons.get(Main.getCurrentPlayer()).setVisible(true);
                    }
                    else{
                        headIcons.get(0).setVisible(true);
                        headIcons.get(Main.getPlayerNumber()-1).setVisible(false);
                    }
                    dateTextField.setText("Round: "+Main.getRound());
                    nameField.setText("Name: "+Main.getPlayerName());
                    moneyField.setText("Money: "+Main.getPlayerMoney());
                    rollDice.setDisable(true);
                    outPutTextArea.appendText(Main.getPlayerName()+"掷出了: "+dice+"点\n");
                    Thread.currentThread().sleep(500);
                    Main.walk(dice);
                    Main.playerTurn(dice);
                    Thread.currentThread().sleep(1000);
                    rollDice.setDisable(false);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        //TODO walk by dice
    }
    public void walk(int playerID,int position){
        try{
            Thread.currentThread().sleep(500);
            if(position==0||position==1||position==2||position==3||position==4){
                mapIcons.get(playerID).setLayoutX(mapIcons.get(playerID).getLayoutX()+200);
            }
            else if(position==5||position==6||position==7||position==8||position==9){
                mapIcons.get(playerID).setLayoutY(mapIcons.get(playerID).getLayoutY()+150);
            }
            else if(position==10||position==11||position==12||position==13||position==14){
                mapIcons.get(playerID).setLayoutX(mapIcons.get(playerID).getLayoutX()-200);
            }
            else{
                mapIcons.get(playerID).setLayoutY(mapIcons.get(playerID).getLayoutY()-150);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void appendText(String text){
        outPutTextArea.appendText(text+"\n");
    }
    public boolean getChoice() throws InterruptedException{
        final boolean[] clickButton={false};
        choiceButtonY.setDisable(false);
        choiceButtonY.setVisible(true);
        choiceButtonN.setDisable(false);
        choiceButtonN.setVisible(true);
        final boolean[] flag=new boolean[1];
        choiceButtonY.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                clickButton[0]=true;
                flag[0]=true;
            }
        });
        choiceButtonN.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                clickButton[0]=true;
                flag[0]=false;
            }
        });
        while(true){
            Thread.currentThread().sleep(3000);
            if(clickButton[0]){
                choiceButtonY.setDisable(true);
                choiceButtonY.setVisible(false);
                choiceButtonN.setDisable(true);
                choiceButtonN.setVisible(false);
                return flag[0];
            }
        }
    }
    public void refreshMoney(){
        moneyField.setText("Money: "+Main.getPlayerMoney());
    }
}
