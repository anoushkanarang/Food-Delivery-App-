import java.util.*;

interface Discount{
    public double getdiscount(double bv);
    public void setDiscount(int d);
}
interface User{
    public void getnames();
    public void getdetails(int i);
}
interface deliveryCharge{
    public int getdeliverycharge();
}
interface rewardPoints{
    public int getpoints(double bv);
}
class Restaurant implements Discount, rewardPoints{
    private String name;
    private String address;
    private int numofOrders;
    private int discount;
    private String category;
    private HashMap<Integer, foodItem> flist;
    private int rewards;
    Restaurant(String name, String address, String category){
        this.name= name;
        this.address=address;
        this.category=category;
        flist = new HashMap<>();
    }
    @Override
    public void setDiscount(int d) {
        this.discount =d;
    }
    @Override
    public double getdiscount(double billvalue) {
        return this.discount;
    }
    public void setname(String n) {
        this.name =n;
    }
    public String getname(){
        return this.name;
    }
    public void setaddress(String a) {
        this.address =a;
    }
    protected String getaddress(){
        return this.address;
    }
    protected void setnorders(int n) {
        this.numofOrders=n;
    }
    protected int getnorders() {
        return this.numofOrders;
    }
    public void addfooditems(foodItem f) {
        this.flist.put(f.getID(),f);
    }
    protected HashMap<Integer, foodItem> getfoodItems() {
        return this.flist;
    }
    public String getcategory(){
        return this.category;
    }
    public int getrewards(){
        return this.rewards;
    }
    public void setrewards(int r){
        this.rewards =r;
    }
    @Override
    public int getpoints(double bv){
        this.rewards+=(((int)(bv/100))*5);
        return (((int)(bv/100))*5);
    }
}
class regularRestaurant extends Restaurant{
    private final String category;
    regularRestaurant(String n, String a){
        super(n,a,"REGULAR");
        category = "REGULAR";
        this.setname(n);
        this.setaddress(a);
    }
    @Override
    public String getcategory() {
        return this.category;
    }
}
class authenticRestaurant extends Restaurant implements rewardPoints{
    private final String category;
    authenticRestaurant(String n, String a){
        super(n,a,"AUTHENTIC");
        category = "AUTHENTIC";
        this.setname(n);
        this.setaddress(a);
    }
    @Override
    public String getcategory() {
        return this.category;
    }
    @Override
    public double getdiscount(double billvalue) {
        double tbv = (super.getdiscount(1)* billvalue)/100.0;
        if (tbv>100){
            tbv-=50;
        }
        return tbv;
    }

    @Override
    public int getpoints(double bv){
        int r = (((int)(bv/200))*25);
        this.setrewards(this.getrewards()+r);
        return r;
    }

}
class fastfoodRestaurant extends Restaurant implements rewardPoints{
    private final String category;
    fastfoodRestaurant(String n, String a){
        super(n,a,"FAST FOOD");
        category = "FAST FOOD";
        this.setname(n);
        this.setaddress(a);
    }

    @Override
    public double getdiscount(double billvalue) {
        double tbv = (super.getdiscount(1)* billvalue)/100.0;
        return tbv;
    }

    @Override
    public String getcategory() {
        return this.category;
    }

    @Override
    public int getpoints(double bv){
        int r =((int)(bv/150))*10;
        this.setrewards(this.getrewards()+r);
        return r;
    }
}
class restaurantOwner extends Zotato implements User {
    private HashMap<String, Restaurant> rmap;
    private ArrayList<String> listOfKeys;

    restaurantOwner(HashMap<String, Restaurant> r, boolean br){
        this.rmap = r;
        this.listOfKeys = new ArrayList<String>(r.keySet());
        getnames();
        int num = app.scn.nextInt();
        if (br)
        { System.out.println("WELCOME to "+listOfKeys.get(num-1));
            new RestaurantQueries(r.get(listOfKeys.get(num-1)));}
        else {
            Restaurant rt = r.get(listOfKeys.get(num-1));
            System.out.println(rt.getname()+" "+rt.getaddress()+" "+rt.getnorders());
        }
    }

    @Override
    public void getnames() {
        for (int i=0; i<listOfKeys.size();i++) {
            System.out.println((i+1)+" "+listOfKeys.get(i)+ "("+ rmap.get(listOfKeys.get(i)).getcategory()+")");
        }
    }
    @Override
    public void getdetails(int i) {
        String name = listOfKeys.get(i);
        Restaurant r = getrsts().get(name);
        System.out.println("NAME: "+ r.getname()+ " ADDRESS: "+ r.getaddress()+" "+ "CATEGORY: "+ r.getcategory()+" Number of orders taken: "+ r.getnorders());
    }
}
class Customer implements Discount, User, deliveryCharge{
    private String name;
    private String address;
    private int discount;
    private String status;
    private HashMap<String, Customer> ct;
    private ArrayList<String> listOfKeys;
    private Wallet w;
    private cart c;
    private ArrayList<cart> recent;

    Customer(HashMap<String, Customer> c, HashMap<String,Restaurant>r, boolean br, Zotato z){
        this.ct =c;
        listOfKeys = new ArrayList<String>(ct.keySet());
        getnames();
        int num = app.scn.nextInt();
        if (br){
            System.out.println("WELCOME "+listOfKeys.get(num-1));
            System.out.println("Customer Menu");
            new CustomerQueries(c.get(listOfKeys.get(num-1)),r, z);
        }else{
            Customer ct = c.get(listOfKeys.get(num-1));
            System.out.println(ct.getname()+" ("+ ct.getstatus()+") "+ ct.getaddress()+" "+ ct.getWallet().getbalance());
        }
    }
    Customer(String name, String address, String status){
        this.name= name;
        this.address=address;
        this.status = status;
        this.w = new Wallet();
        this.c = new cart(this);
        this.recent = new ArrayList<>();
    }

    @Override
    public void getnames() {
        for (int i=0; i<listOfKeys.size();i++) {
            System.out.println((i+1)+" "+listOfKeys.get(i) + "("+ct.get(listOfKeys.get(i)).getstatus()+")");
        }
    }

    @Override
    public void getdetails(int i) {
        // TODO Auto-generated method stub
        String name = listOfKeys.get(i);
        Customer c = ct.get(name);
        System.out.println("NAME: "+ c.getname()+ " ADDRESS: "+ c.getaddress()+" "+ "STATUS: "+ c.getstatus()+"Account balance: INR ");
    }
    public String getstatus(){
        return this.status;
    }
    public String getname(){
        return this.name;
    }
    public void setname(String n){
        this.name =n;
    }
    public String getaddress(){
        return this.address;
    }
    public void setaddress(String a){
        this.address = a;;
    }
    @Override
    public void setDiscount(int d) {
        this.discount =d;
    }
    @Override
    public double getdiscount(double bv) {
        return this.discount;
    }
    public Wallet getWallet(){
        return this.w;
    }
    public cart getcart(){
        return this.c;
    }
    public void setcart(cart c){
        this.c = c;
    }
    public int getdeliverycharge(){
        return 0;
    }
    public ArrayList<cart> getorders(){
        return this.recent;
    }


}
class regularCustomer extends Customer implements deliveryCharge{
    private final int deliveryCharge;
    private final String status;
    regularCustomer(String n, String a){
        super(n,a,"REGUALR");
        deliveryCharge = 40;
        status = "REGULAR";
        this.setname(n);
        this.setaddress(a);
    }
    @Override
    public String getstatus(){
        return this.status;
    }
    @Override
    public int getdeliverycharge(){
        return this.deliveryCharge;
    }
    @Override
    public double getdiscount(double billvalue){
        return 0;
    }
}
class eliteCustomer extends Customer implements deliveryCharge{
    private final int deliveryCharge;
    private final String status;
    eliteCustomer(String n, String a){
        super(n,a,"ELITE");
        deliveryCharge=0;
        status = "ELITE";
        this.setname(n);
        this.setaddress(a);
    }

    @Override
    public String getstatus(){
        return this.status;
    }

    @Override
    public int getdeliverycharge(){
        return this.deliveryCharge;
    }

    @Override
    public double getdiscount(double billvalue){
        if (billvalue>200){
            return 50;
        }
        return 0;
    }
}
class splCustomer extends Customer implements deliveryCharge{
    private final int deliveryCharge;
    private final String status;
    splCustomer(String n, String a){
        super(n,a,"SPECIAL");
        deliveryCharge= 20;
        status = "SPECIAL";
        this.setname(n);
        this.setaddress(a);
    }

    @Override
    public String getstatus(){
        return this.status;
    }

    @Override
    public int getdeliverycharge(){
        return this.deliveryCharge;
    }

    @Override
    public double getdiscount(double billvalue){
        if (billvalue>200){
            return 25;
        }
        return 0;
    }
}
class foodItem implements Discount{
    private String name;
    private int price;
    private int quantity;
    private int discountpercent;
    private String category;
    private final int id;

    foodItem(int i){
        this.id = i;
    }
    foodItem(String n, int p, int q, int dp, String c,int i){
        this.name=n;
        this.price=p;
        this.quantity=q;
        this.discountpercent=dp;
        this.category=c;
        this.id =i;
    }

    public int getID() {
        return this.id;
    }

    public String getname(){
        return this.name;
    }

    public void setname(String n){
        this.name=n;
    }

    public int getprice(){
        return this.price;
    }

    public void setprice(int p){
        this.price =p;
    }

    public int getqnt(){
        return this.quantity;
    }

    public void setqnt(int q){
        this.quantity =q;
    }
    @Override
    public double getdiscount(double bv){
        return this.discountpercent;
    }
    @Override
    public void setDiscount(int d){
        this.discountpercent =d;
    }

    public String getcategory(){
        return this.category;
    }

    public void setcategory(String c){
        this.category=c;
    }

}
class Zotato{

    private HashMap<String, Restaurant> rts;
    private HashMap<String, Customer> cts;
    private double restexpenses;
    private double deliverycharges;

    Zotato(){
        rts = new HashMap<>();
        cts = new HashMap<>();
        // Adding Restaurants
        rts.put("Wang's", new fastfoodRestaurant("Wang's","Sector-11,Dwarks"));
        rts.put("Shah",new authenticRestaurant("Shah", "Janak Puri"));
        rts.put("The Chinese",new authenticRestaurant("The Chinese", "Noida"));
        rts.put("Ravi's",new regularRestaurant("Ravi's", "Dwarka"));
        rts.put("Paradise",new regularRestaurant("Paradise", "Chanakyapuri"));
        // Adding Customers
        cts.put("Kim",new regularCustomer("Kim","D-23,JanakPuri"));
        cts.put("Jim",new regularCustomer("Jim","A-91,Pitampura"));
        cts.put("Ram",new eliteCustomer("Ram","C-23,Vasant Kunj"));
        cts.put("Sam",new eliteCustomer("Sam","D-12,Kalkaji"));
        cts.put("Tim",new splCustomer("Tim","D-23,DDA Flats,Dwarka"));
    }
    public HashMap<String, Restaurant> getrsts(){
        return this.rts;
    }
    public HashMap<String, Customer> getcts(){
        return this.cts;
    }
    public double getcharges(){
        return Math.round(this.restexpenses *100)/100.0;
    }
    public void setcharges(double amt){
        this.restexpenses = amt;
    }
    public double getdcharges(){
        return Math.round(this.deliverycharges *100)/100.0;
    }
    public void setdcharges(double amt){
        this.deliverycharges = amt;
    }
}
class Wallet{
    private double rewards;
    private double balance;
    Wallet(){
        this.rewards=0;
        this.balance= 1000;
    }
    public double getrewards(){
        return this.rewards;
    }
    public double getbalance(){
        return this.balance;
    }
    public void setrewards(double d){
        this.rewards =d;
    }
    public void setbalance(double d){
        this.balance=d;
    }
}
class cart{
    private ArrayList<Order> o;
    private Customer c;
    private Restaurant r;

    cart(Customer c){
        o= new ArrayList<>();
        this.c = c;
    }

    public double placeorder(){
        double billvalue = 0;
        for (int i=0; i<o.size();i++){
            foodItem f = o.get(i).getfoodItem();
            billvalue += ((f.getprice()* o.get(i).getQuantity()) - (( f.getprice()* o.get(i).getQuantity()* f.getdiscount(1.0))/100.0));  // applying food discount
        }

        billvalue-= r.getdiscount(billvalue); // applying rest discount
        billvalue -= c.getdiscount(billvalue); // applying customer discount
        billvalue+= c.getdeliverycharge();

        if((c.getWallet().getrewards()+c.getWallet().getbalance())<billvalue){
            System.out.println("INSUFFICIENT BALANCE AMOUNT!");
            System.out.println(c.getWallet().getrewards());
            ArrayList<Order> tp = c.getcart().getOrders();
            for (int i=0; i<tp.size();i++){
                foodItem f = tp.get(i).getfoodItem();
                double price= (((f.getprice()))- (( f.getprice()* f.getdiscount(1.0))/100.0));
                int q = tp.get(i).getQuantity();
                if (((c.getWallet().getrewards()+c.getWallet().getbalance()))>=(billvalue-(price*q))){
                    int m =1;
                    while ((c.getWallet().getrewards()+c.getWallet().getbalance())<=billvalue){
                        billvalue-= price;
                        m++;
                    }
                    System.out.println("Removing "+ (m-1)+" "+tp.get(i).getfoodItem().getname()+" from the cart and placing the order");
                    tp.get(i).setQuantity(q-(m-1));
                    break;
                }
            }
        }

        if (c.getWallet().getrewards()>= billvalue){
            c.getWallet().setrewards(c.getWallet().getrewards()-billvalue);
            System.out.println("ORDER PLACED SUCCESSFULY!");
        }

        else{
            billvalue-=c.getWallet().getrewards();
            c.getWallet().setrewards(0);
            c.getWallet().setbalance(c.getWallet().getbalance()-billvalue);
            System.out.println("ORDER PLACED SUCCESSFULY!");
        }

        c.getWallet().setrewards(c.getWallet().getrewards()+ r.getpoints(billvalue-c.getdeliverycharge()));
        c.getorders().add(c.getcart());
        return (Math.round((billvalue-c.getdeliverycharge()) * 100.0) / 100.0);
    }
    public void printcart(boolean br){
        if (br){
            System.out.println(this.c.getname()+"'s Order at "+this.r.getname());
        }

        for (Order od : o){
            double overall_price = ((od.getfoodItem().getprice()*od.getQuantity())-((od.getfoodItem().getprice()*od.getQuantity()*od.getfoodItem().getdiscount(0))/100.0));
            System.out.println(" " + od.getfoodItem().getname() + " " + od.getQuantity() +" "+ overall_price);
        }
    }
    public int numitems(){
        int sum=0;
        for (Order od : o){
            sum+= od.getQuantity();
        }
        return sum;
    }
    public ArrayList<Order> getOrders(){
        return this.o;
    }
    public void setrest(Restaurant r){
        this.r = r;
    }

}
class Order{
    private Customer c;
    private int quantity;
    private foodItem f;
    Order(foodItem f, int q, Customer c){
        this.f = f;
        this.quantity= q;
        this.c = c;
    }
    public foodItem  getfoodItem(){
        return this.f;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public void setQuantity(int a){
        this.quantity=a;
    }

}
class CustomerQueries{
    CustomerQueries(Customer c, HashMap<String, Restaurant>rmap, Zotato z){
        c.setcart(new cart(c));
        int input =0;
        while (input!=5){
            System.out.println(" (1) Select Restaurant \n 2) checkout Cart \n 3) Reward won \n 4) Print recent orders \n 5) Exit");
            System.out.println("Enter query number ");
            input = app.scn.nextInt();
            callqueries(input,c, rmap,z);
        }
    }
    public void callqueries(int num, Customer c, HashMap<String, Restaurant>rmap, Zotato z){
        if (num==1){
            query1(c,rmap);
        }
        else if (num==2){
            query2(c,z);
        }
        else if (num==3){
            query3(c);
        }
        else if (num==4){
            query4(c);
        }
        else if (num==5){
            System.out.println("Logging out");
        }
        else{
            System.out.println("Invalid Query number entered.");
        }
    }
    public void query1(Customer c, HashMap<String, Restaurant>rmap){
        System.out.println("Select Restaurant:");
        ArrayList<String> listOfKeys = new ArrayList<String>(rmap.keySet());
        for (int i=0; i<listOfKeys.size();i++) {
            System.out.println((i+1)+" "+listOfKeys.get(i)+ "("+ rmap.get(listOfKeys.get(i)).getcategory()+")");
        }

        int num = app.scn.nextInt();
        Restaurant r = rmap.get(listOfKeys.get(num-1));
        c.getcart().setrest(r);
        r.setnorders(r.getnorders()+1);
        System.out.println("Choose item by code:");
        HashMap<Integer, foodItem> fmap = r.getfoodItems();
        for (Map.Entry<Integer, foodItem> mapElement : fmap.entrySet()) {
            foodItem f = mapElement.getValue();
            System.out.println(f.getID()+" "+ r.getname()+" "+ f.getname()+" "+f.getprice()+" "+f.getqnt()+" "+f.getdiscount(1.0)+"% off "+f.getcategory()); }
        int code = app.scn.nextInt();
        foodItem f = fmap.get(code);
        System.out.println("Enter quantity:");
        int q = app.scn.nextInt();
        Order o = new Order(f,q,c);
        c.getcart().getOrders().add(o);
        System.out.println("Items added to Cart");
        //if insufficient balance

    }
    public void query2(Customer c, Zotato z){

        double amt = c.getcart().placeorder();
        System.out.println("Items in cart");
        c.getcart().printcart(true);
        System.out.println("Delivery charge: INR "+ c.getdeliverycharge());
        System.out.println("Total bill value after discounts: INR "+ (amt+c.getdeliverycharge()));
        System.out.println("1) Proceed to checkout");
        int n = app.scn.nextInt();
        if (n ==1){  // successful
            System.out.println(c.getcart().numitems()+" items bought successfully for INR "+ (amt+c.getdeliverycharge()));
        }
        double sum = (+ c.getdeliverycharge());
        z.setcharges(z.getcharges()+(0.01* amt));
        z.setdcharges(z.getdcharges()+c.getdeliverycharge());
        c.getorders().add(c.getcart());
    }
    public void query3(Customer c){
        System.out.println("Rewards won:" + c.getWallet().getrewards());
    }
    public void query4(Customer c){
        ArrayList<cart> o = c.getorders();
        System.out.println("Order by "+ c.getname());
        for (int i=0; i<o.size();i++){
            cart crt= o.get(i);
            crt.printcart(false);
        }
    }
}
class RestaurantQueries{
    private static int id;

    RestaurantQueries(Restaurant r){
        int input =0;
        while (input!=5){
            System.out.println(" (1) Add item \n 2) Edit item \n 3) Print Rewards \n 4) Discount on bill value \n 5) Exit");
            System.out.println("Enter your query number ");
            input = app.scn.nextInt();
            if (input==1){
                id++;
            }
            callqueries(input,r, id);
        }
    }

    public void callqueries(int num,Restaurant r,int id){
        if (num==1){
            query1(r, id);
        }
        else if (num==2){
            query2(r);
        }
        else if (num==3){
            query3(r);
        }
        else if (num==4){
            query4(r);
        }
        else if (num==5){
            System.out.println("Logging out");
        }
        else{
            System.out.println("Invalid Query number entered.");
        }
    }

    public void query1(Restaurant r, int id){
        foodItem f = new foodItem(id);
        System.out.println("Item name:");
        f.setname(app.scn.next());
        System.out.println("Item price:");
        f.setprice(app.scn.nextInt());
        System.out.println("Item quantity:");
        f.setqnt(app.scn.nextInt());
        System.out.println("Item category:");
        f.setcategory(app.scn.next());
        System.out.println("Item offer:");
        f.setDiscount(app.scn.nextInt());
        r.addfooditems(f);
        System.out.println(f.getID()+" "+ f.getname()+" "+f.getprice()+" "+f.getqnt()+" "+f.getdiscount(1.0)+"% off "+f.getcategory());
        System.out.println("Updated Successfully!");
    }

    public void query2(Restaurant r){
        System.out.println("Choose the item to edit by ID");
        HashMap<Integer, foodItem> fmap = r.getfoodItems();
        for (Map.Entry<Integer, foodItem> mapElement : fmap.entrySet()) {
            foodItem f = mapElement.getValue();
            System.out.println(f.getID()+" "+ r.getname()+" "+ f.getname()+" "+f.getprice()+" "+f.getqnt()+" "+f.getdiscount(1.0)+"% off "+f.getcategory()); }

        int itemnum = app.scn.nextInt();
        foodItem food = fmap.get(itemnum);
        System.out.println("Choose attribute to edit:");
        System.out.println(" (1) Name\n 2) Price\n 3) Quantity\n 4) Category\n 5) Offer");

        int attrnum = app.scn.nextInt();
        if (attrnum==1){
            System.out.println("Enter new name");
            food.setname(app.scn.next());
        }
        else if(attrnum ==2){
            System.out.println("Enter new price");
            food.setprice(app.scn.nextInt());
        }
        else if(attrnum ==3){
            System.out.println("Enter new quantity");
            food.setqnt(app.scn.nextInt());
        }else if(attrnum ==4){
            System.out.println("Enter new category");
            food.setcategory(app.scn.next());
        }else if(attrnum ==5){
            System.out.println("Enter new offer");
            food.setDiscount(app.scn.nextInt());
        }else{
            System.out.print("Wrong attribute number inputted");
        }

        System.out.println(food.getID()+" "+ r.getname()+" "+ food.getname()+" "+food.getprice()+" "+food.getqnt()+" "+food.getdiscount(1.0)+"% off "+food.getcategory());
        System.out.println("Updated Successfully!");
    }

    public void query3(Restaurant r){
        System.out.println("REWARD POINTS: "+r.getrewards());
    }

    public void query4(Restaurant r){
        System.out.println("Enter offer on total bill value");
        r.setDiscount(app.scn.nextInt());
    }

}

public class app{

    static Scanner scn = new Scanner(System.in);
    app(){
        System.out.println("Welcome to Zotato!");
        Zotato z = new Zotato();
        int ip = 0;
        while (ip!=5){
            System.out.println(" 1) Enter as Restaurant Owner\n 2) Enter as Customer\n 3) Check User Details\n 4) Company Account details\n 5) Exit");
            ip = scn.nextInt();
            if (ip==5) {
                System.out.println("Thank you for using Zotato. Hope you had a great experience :)");break; }
            else { Userqueries(ip,z); }
        }
    }

    private void Userqueries(int ip, Zotato z){
        User u;
        if (ip==1) { System.out.println("Choose Restaurant:");
            u = new restaurantOwner(z.getrsts(),true);
        }
        else if (ip==2) {
            System.out.println("Choose Name:");
            u = new Customer(z.getcts(),z.getrsts(),true, z); }
        else if (ip==3) {
            System.out.println(" 1) Customer List\n 2) Restaurant List");
            int input = scn.nextInt();
            if (input==1) { u = new Customer(z.getcts(), z.getrsts(),false,z);}
            else { u = new restaurantOwner(z.getrsts(),false); }
        }
        else if (ip==4) { System.out.println("Restaurant expenses "+z.getcharges()+"INR, Delivery charges " + z.getdcharges()+"INR"); }
    }

    public static void main(String[] args) {
        new app();
    }

}