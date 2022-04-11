package Monopoly;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SelectCharacterController{
    int playerNumber=2;
    public Button addPlayer=new Button();
    public Button deletePlayer=new Button();
    public Button startGame=new Button();
    public ImageView player3Icon=new ImageView();
    public ImageView player4Icon=new ImageView();
    public ImageView player3Name=new ImageView();
    public ImageView player4Name=new ImageView();
    public TextField player1TextField=new TextField();
    public TextField player2TextField=new TextField();
    public TextField player3TextField=new TextField();
    public TextField player4TextField=new TextField();

    public void clickAdd(ActionEvent actionEvent){
        if(playerNumber==2){
            player3Icon.setVisible(true);
            player3Name.setVisible(true);
            player3TextField.setVisible(true);
            playerNumber++;
        }else if(playerNumber==3){
            player4Icon.setVisible(true);
            player4Name.setVisible(true);
            player4TextField.setVisible(true);
            playerNumber++;
        }
    }

    public void clickDelete(ActionEvent actionEvent){
        if(playerNumber==4){
            player4Icon.setVisible(false);
            player4Name.setVisible(false);
            player4TextField.setVisible(false);
            playerNumber--;
        }else if(playerNumber==3){
            player3Icon.setVisible(false);
            player3Name.setVisible(false);
            player3TextField.setVisible(false);
            playerNumber--;
        }
    }

    public void clickStart(ActionEvent actionEvent) throws Exception{
        String[] names=new String[playerNumber];
        if(playerNumber==2){
            names[0]=player1TextField.getText();
            names[1]=player2TextField.getText();
            if(!names[0].equals("")&&!names[1].equals(""))
                Main.gameStart(playerNumber,names);
        }else if(playerNumber==3){
            names[0]=player1TextField.getText();
            names[1]=player2TextField.getText();
            names[2]=player3TextField.getText();
            if(!names[0].equals("")&&!names[1].equals("")&&!names[2].equals(""))
                Main.gameStart(playerNumber,names);
        }else {
            names[0]=player1TextField.getText();
            names[1]=player2TextField.getText();
            names[2]=player3TextField.getText();
            names[3]=player4TextField.getText();
            if(!names[0].equals("")&&!names[1].equals("")&&!names[2].equals("")&&!names[3].equals(""))
                Main.gameStart(playerNumber,names);
        }
    }
}
