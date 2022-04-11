package Monopoly;

public class Land{

    protected int number;
    protected boolean isPurchased;
    protected boolean canPurchase;
    protected int houseRank;
    protected double rate;
    protected int owner;
    protected int price;
    protected String ownerName;
    protected String landName;

    Land(int number,String landName,double rate,int price,boolean canPurchase){
        this.number=number;
        this.isPurchased=false;
        this.landName=landName;
        this.rate=rate;
        this.houseRank=0;
        this.price=price;
        this.canPurchase=canPurchase;
        this.owner=-1;
    }

    public int getNumber(){
        return number;
    }

    public boolean isPurchased(){
        return isPurchased;
    }

    public int getHouseRank(){
        return houseRank;
    }

    public double getRate(){
        return rate;
    }

    public int getOwner(){
        return owner;
    }

    public int getPrice(){
        return price;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public String getLandName(){
        return landName;
    }

    public boolean isCanPurchase(){
        return canPurchase;
    }

    public int getLevelUpCost(){
        return price/10*(houseRank+1);
    }

    public int getToll(){
        return (int)(price/10*(1+rate+(double)(houseRank/10)));
    }

    public void setCanPurchase(boolean canPurchase){
        this.canPurchase=canPurchase;
    }

    public void setNumber(int number){
        this.number=number;
    }

    public void setPurchased(boolean purchased){
        isPurchased=purchased;
    }

    public void setHouseRank(int houseRank){
        this.houseRank=houseRank;
    }

    public void setRate(int rate){
        this.rate=rate;
    }

    public void setOwner(int owner){
        this.owner=owner;
    }

    public void setPrice(int price){
        this.price=price;
    }

    public void setOwnerName(String ownerName){
        this.ownerName=ownerName;
    }

    public void setLandName(String landName){
        this.landName=landName;
    }
}
