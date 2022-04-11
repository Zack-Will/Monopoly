package Monopoly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    protected static MainGameController mainGameController;
    protected static ArrayList<Player> players;
    protected static ArrayList<Land> lands;
    protected static Stage mainStage;
    protected static int playerNumber;
    protected static int round;
    protected static int currentPlayer;
    protected static int alivePlayer;
    protected static MediaPlayer mediaPlayer;
    protected static Media media;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String musicURL=getClass().getResource("musics/background.mp3").toString();
        media=new Media(musicURL);
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0.3);
        players=new ArrayList<>();
        lands=new ArrayList<>();
        mainStage=new Stage();
        round=1;
        currentPlayer=0;
        Parent interfaceRoot=FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene interfaceScene=new Scene(interfaceRoot,1600,900);
        mainStage.setTitle("Monopoly");
        mainStage.setScene(interfaceScene);
        mainStage.setResizable(false);
        mainStage.show();
    }
    public static void changeToSelect() throws Exception{
        Parent selectRoot=FXMLLoader.load(Main.class.getResource("SelectCharacter.fxml"));
        mainStage.setScene(new Scene(selectRoot,1600,900));
        mainStage.setResizable(false);
    }
    public static void gameStart(int playerNumber,String[] names) throws Exception{
        Main.playerNumber=playerNumber;
        Main.alivePlayer=playerNumber;
        for(int i=0;i<playerNumber;i++)
            players.add(new Player(names[i],i));
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("mainGame.fxml"));
        Parent mainGameRoot=loader.load();
        mainGameController=loader.getController();
        Scene mainGameScene=new Scene(mainGameRoot,1600,900);
        mainStage.setScene(mainGameScene);
        mainStage.setResizable(false);
    }
    public static void playerTurn(int dice) throws Exception{
        if(round==1){
            nextTurn();
            return;
        }
        Land currentLand=lands.get(players.get(currentPlayer).getCurrentLand());
        if(currentLand.isCanPurchase()){
            if(currentLand.isPurchased){
                if(currentLand.getOwner()==currentPlayer){
                    updateLand(currentLand);
                }else{
                    payToll(currentLand);
                }
            }else{
                buyLand(currentLand);
            }
        }else{
            specialLand(currentLand);
        }
        nextTurn();
    }
    public static void nextTurn() throws IOException{
        if(alivePlayer==1)
            settlement();
        else {
            while(true){
                currentPlayer++;
                if(currentPlayer==playerNumber){
                    currentPlayer=0;
                    round++;
                }
                if(players.get(currentPlayer).sleep!=0){
                    players.get(currentPlayer).sleep--;
                    mainGameController.appendText(players.get(currentPlayer).getName()+"仍在沉睡中");
                    mainGameController.appendText("剩余"+players.get(currentPlayer).sleep+"回合");
                    continue;
                }
                if(!players.get(currentPlayer).bankruptcy)
                    break;
            }
        }
    }
    public static void walk(int dice){
        for(int i=0;i<dice;i++){
            mainGameController.walk(currentPlayer,players.get(currentPlayer).getCurrentLand());
            players.get(currentPlayer).walk();
        }
    }
    public static void pay(int currentPlayer,int targetPlayer,int money){
        players.get(currentPlayer).giveAway(money);
        players.get(targetPlayer).gainMoney(money);
    }
    public static void buyLand(Land currentLand) throws Exception{
        mainGameController.appendText("是否购买该地块?\n(售价:"+currentLand.getPrice()+")");
        if(mainGameController.getChoice()){
            if(players.get(currentPlayer).getMoney()<currentLand.getPrice())
                mainGameController.appendText("资金不足!");
            else{
                currentLand.setOwner(currentPlayer);
                currentLand.setPurchased(true);
                currentLand.setOwnerName(players.get(currentPlayer).getName());
                players.get(currentPlayer).setMoney(players.get(currentPlayer).getMoney()-currentLand.getPrice());
                mainGameController.refreshMoney();
                players.get(currentPlayer).boughtLands.add(currentLand.getNumber());
                mainGameController.appendText("购买成功");
            }
        }
    }
    public static void updateLand(Land currentLand) throws Exception{
        mainGameController.appendText("是否升级土地?\n(花费:"+currentLand.getLevelUpCost()+")");
        if(mainGameController.getChoice()){
            if(players.get(currentPlayer).getMoney()<currentLand.getLevelUpCost())
                mainGameController.appendText("资金不足!");
            else if(currentLand.getHouseRank()==5)
                mainGameController.appendText("已升级至满级");
            else{
                players.get(currentPlayer).setMoney(players.get(currentPlayer).getMoney()-currentLand.getLevelUpCost());
                currentLand.houseRank++;
                mainGameController.refreshMoney();
                mainGameController.appendText("升级成功\n(目前等级:"+currentLand.getHouseRank()+"级)");
            }
        }
    }
    public static void payToll(Land currentLand){
        if(players.get(currentPlayer).getMoney()<currentLand.getToll()){
            mainGameController.appendText("需要降级地块支付过路费");
            while(players.get(currentPlayer).getMoney()<currentLand.getToll()){
                if(!sellRank(currentPlayer))
                    break;
            }
        }
        if(players.get(currentPlayer).getMoney()<currentLand.getToll()){
            mainGameController.appendText("您的资金不够支付路费\n已破产");
            pay(currentPlayer,currentLand.getOwner(),players.get(currentPlayer).getMoney());
            players.get(currentPlayer).bankruptcy=true;
            return;
        }
        pay(currentPlayer,currentLand.getOwner(),currentLand.getToll());
        mainGameController.appendText("向"+currentLand.getOwnerName()+"支付了"+currentLand.getToll());
        mainGameController.refreshMoney();
    }
    public static int payMoney(int player,int money){
        if(players.get(player).getMoney()<money){
            mainGameController.appendText("需要降级地块付款");
            while((players.get(player).getMoney()<money)){
                if(!sellRank(player))
                    break;
            }
        }
        if(players.get(player).getMoney()<money){
            mainGameController.appendText("您的资金不够支付\n已破产");
            players.get(currentPlayer).bankruptcy=true;
            return players.get(player).getMoney();
        }
        players.get(player).giveAway(money);
        mainGameController.appendText("支付了"+money+"元");
        mainGameController.refreshMoney();
        return money;
    }
    public static boolean sellRank(int playerID){
        int topRankLandID=-1;
        int topRank=-1;
        if(players.get(playerID).boughtLands.size()==0)
            return false;
        for(int i=0;i<players.get(playerID).boughtLands.size();i++){
            int landID=players.get(playerID).boughtLands.get(i);
            if(lands.get(i).getHouseRank()>topRank){
                topRankLandID=i;
                topRank=lands.get(i).getHouseRank();
            }
        }
        if(topRank==0)
            return false;
        else{
            lands.get(topRankLandID).houseRank--;
            players.get(playerID).gainMoney(100);
            mainGameController.appendText("出售了"+lands.get(topRankLandID).getLandName()+"的房屋等级");
            mainGameController.appendText("当前等级:"+(topRank-1));
            return true;
        }
    }
    public static void specialLand(Land currentLand) throws Exception{
        int currentLandID=currentLand.getNumber();
        if(currentLandID==3){
            //TODO chance
            int choice=Dice.rollDice();
            switch(choice){
                case 1:
                    mainGameController.appendText("路边捡到100元钱");
                    players.get(currentPlayer).gainMoney(100);
                    break;
                case 2:
                    mainGameController.appendText("彩票中奖1000元");
                    players.get(currentPlayer).gainMoney(1000);
                    break;
                case 3:
                    mainGameController.appendText("助人为乐奖励200元");
                    players.get(currentPlayer).gainMoney(200);
                    break;
                case 4:
                    mainGameController.appendText("路上被狗咬住院一回合");
                    players.get(currentPlayer).sleep++;
                    break;
                case 5:
                    mainGameController.appendText("施舍流浪汉100元");
                    payMoney(currentPlayer,100);
                    break;
                case 6:
                    mainGameController.appendText("被诈骗500元");
                    payMoney(currentPlayer,500);
                    break;

            }
        }
        if(currentLandID==5){
            if(players.get(currentPlayer).getMoney()>=500){
                mainGameController.appendText("罚款500元");
                players.get(currentPlayer).giveAway(500);
            }
            else{
                mainGameController.appendText("罚款"+players.get(currentPlayer).getMoney()+"元");
                players.get(currentPlayer).giveAway(players.get(currentPlayer).getMoney());
            }
            mainGameController.refreshMoney();
            walk(10);
            players.get(currentPlayer).sleep+=5;
        }
        if(currentLandID==10){
            mainGameController.appendText("是否在此停歇1回合?");
            if(mainGameController.getChoice()){
                players.get(currentPlayer).sleep++;
            }
        }
        if(currentLandID==13){
            //TODO fate
            int choice=Dice.rollDice();
            switch(choice){
                case 1:
                    mainGameController.appendText("全球地震\n所有地皮等级减一");
                    for(int i=0;i<20;i++){
                        if(lands.get(i).getHouseRank()>0)
                            lands.get(i).houseRank--;
                    }
                    break;
                case 2:
                    mainGameController.appendText("经济危机\n所有玩家失去500元");
                    for(int i=0;i<playerNumber;i++){
                        if(!players.get(i).bankruptcy)
                            payMoney(i,500);
                    }
                    break;
                case 3:
                    mainGameController.appendText("股票大涨\n所有玩家得到500元");
                    for(int i=0;i<playerNumber;i++){
                        if(!players.get(i).bankruptcy)
                            players.get(i).gainMoney(500);
                    }
                    break;
                case 4:
                    mainGameController.appendText("房产升值\n个人地皮等级加一");
                    for(int i=0;i<players.get(currentPlayer).boughtLands.size();i++){
                        if(lands.get(players.get(currentPlayer).boughtLands.get(i)).houseRank!=5)
                            lands.get(players.get(currentPlayer).boughtLands.get(i)).houseRank++;
                    }
                    break;
                case 5:
                    mainGameController.appendText("出车祸住进医院\n休息两回合");
                    players.get(currentPlayer).sleep+=2;
                    break;
                case 6:
                    mainGameController.appendText("额外进行一回合");
                    if(currentPlayer==0)
                        currentPlayer=playerNumber-1;
                    else
                        currentPlayer--;
                    break;

            }
        }
    }
    public static void initialize(){
        Land l0=new Land(0,"起始点",0,3000,false);
        lands.add(l0);
        Land l1=new Land(1,"英国",0.2,1500,true);
        lands.add(l1);
        Land l2=new Land(2,"日本",0.25,1800,true);
        lands.add(l2);
        Land l3=new Land(3,"机会",0,0,false);
        lands.add(l3);
        Land l4=new Land(4,"美国",0.3,2000,true);
        lands.add(l4);
        Land l5=new Land(5,"警察局",0,500,false);
        lands.add(l5);
        Land l6=new Land(6,"加拿大",0.15,1700,true);
        lands.add(l6);
        Land l7=new Land(7,"水力集团",0.3,2200,true);
        lands.add(l7);
        Land l8=new Land(8,"新加坡",0.15,1500,true);
        lands.add(l8);
        Land l9=new Land(9,"瑞士",0.2,1800,true);
        lands.add(l9);
        Land l10=new Land(10,"停车场",0,500,false);
        lands.add(l10);
        Land l11=new Land(11,"阿根廷",0.12,1300,true);
        lands.add(l11);
        Land l12=new Land(12,"巴西",0.1,1200,true);
        lands.add(l12);
        Land l13=new Land(13,"命运",0,0,false);
        lands.add(l13);
        Land l14=new Land(14,"法国",0.2,1600,true);
        lands.add(l14);
        Land l15=new Land(15,"监狱",0,0,false);
        lands.add(l15);
        Land l16=new Land(16,"中国",0.25,2500,true);
        lands.add(l16);
        Land l17=new Land(17,"电力集团",0.3,2200,true);
        lands.add(l17);
        Land l18=new Land(18,"德国",0.22,2100,true);
        lands.add(l18);
        Land l19=new Land(19,"韩国",0.15,1600,true);
        lands.add(l19);
    }
    public static void settlement() throws IOException{
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("Settlement.fxml"));
        Parent settlementRoot=loader.load();
        Scene settlementScene=new Scene(settlementRoot,1600,900);
        mainStage.setScene(settlementScene);
        mainStage.setResizable(false);
        SettlementController settlementController=loader.getController();
        for(int i=0;i<playerNumber;i++){
            if(!players.get(i).bankruptcy){
                settlementController.show(i);
                break;
            }
        }
    }
    public static int getCurrentPlayer(){
        return currentPlayer;
    }
    public static void setCurrentPlayer(int currentPlayer){
        Main.currentPlayer=currentPlayer;
    }
    public static int getPlayerNumber(){
        return playerNumber;
    }
    public static String getPlayerName(){
        return players.get(currentPlayer).getName();
    }
    public static int getPlayerMoney(){
        return players.get(currentPlayer).getMoney();
    }
    public static int getRound(){
        return round;
    }
    public static void setRound(int round){
        Main.round=round;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

