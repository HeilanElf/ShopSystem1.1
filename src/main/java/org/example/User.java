package org.example;

import java.util.Scanner;

class User {
    Menu menu=new Menu();
    Regest regest=new Regest();
    IsTrueEnter isTrueEnter=new IsTrueEnter();
    PasswordUser passwordUser=new PasswordUser();
    ShopUser shopUser=new ShopUser();
    LogIn logIn=new LogIn();
    Scanner scanner=new Scanner(System.in);
//    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//text//";
//    String filePath;
    public int passwordUser(int command){
        int exit=1;
        if(command==1){
            String userName=regest.getCurrentuserName();
            System.out.print("请确认旧密码：");
            String passwordOld= isTrueEnter.passwordhefa(scanner.next());
            System.out.print("请输入新密码：");
            String passwordNew= isTrueEnter.passwordhefa(scanner.next());
            if(logIn.userLogin(userName,passwordOld)){
                passwordUser.fixPassword(userName,passwordNew);
            }else{
                System.out.println("密码不匹配！");
            }
        }
        if(command==2){
            System.out.print("请输入用户名：");
            String username = scanner.next();
            System.out.print("请输入注册时使用的邮箱地址：");
            String email = scanner.next();
            boolean sucess=passwordUser.forgotPassword(username,email);
            if(sucess){
                exit=0;
            }
        }
        menu.next();
        return exit;
    }
    public int shopUser(int command){
        DuctionMaster ductionMaster=new DuctionMaster();
        if(command==1){
            ductionMaster.showDuctionInfo();
            System.out.println("-------------------------------------------------");
            System.out.print("请输入商品编号：");
            String shopID= scanner.next();
            System.out.print("请输入购买数量：");
            String shopNum=scanner.next();
            boolean sucess=shopUser.addDuction(shopID,shopNum);
            if(sucess){
                System.out.println("添加成功！");
                shopUser.showCurrentChat();
            }else{
                System.out.println("添加失败！");
            }
        }
        if(command==2){
            System.out.print("请输入商品编号：");
            String shopID= scanner.next();
            shopUser.showCurrentChat();
            shopUser.removeDuction(shopID);
            shopUser.showCurrentChat();
        }
        if(command==3){
            System.out.print("请输入商品编号：");
            String shopID= scanner.next();
            shopUser.modifyDuctionInfo(shopID);
            shopUser.showCurrentChat();
        }
        if(command==4){
            shopUser.checkout();
        }
        if(command==5){
            shopUser.showHistory();
        }
        menu.next();
        return 1;
    }
    public void user(int command,boolean susucessLoin){
        menu.showSecondUser();
        System.out.print("请输入您的选择：");
        command = isTrueEnter.inthefa(2);
        int exit=1;
        while(true){
            if(command==0){
                break;
            }else{
                switch(command){
                    case 1:
                        menu.showpasswordUser();
                        System.out.print("请输入您的选择：");
                        command=isTrueEnter.inthefa(2);
                        while(true){
                            if(command==0){
                                break;
                            }else{
                                exit=passwordUser(command);
                                if(exit==0){
                                    break;
                                }
                            }
                            if(exit!=0){
                                menu.showpasswordUser();
                                System.out.print("请输入您的选择：");
                                command=isTrueEnter.inthefa(2);
                            }
                        }
                        break;
                    case 2:
                        menu.showShopUser();
                        System.out.print("请输入您的选择：");
                        command=isTrueEnter.inthefa(5);
                        while(true) {
                            if(command==0){
                                break;
                            }else{
                                exit=shopUser(command);
                                if(exit==0){
                                    break;
                                }
                            }
                            if(exit!=0){
                                menu.showShopUser();
                                System.out.print("请输入您的选择：");
                                command=isTrueEnter.inthefa(5);
                            }
                        }
                        break;
                }
            }
            if(exit!=0){
                menu.showSecondUser();
                System.out.print("请输入您的选择：");
                command = isTrueEnter.inthefa(2);
            }
        }
    }
}
class PasswordUser{

    public boolean forgotPassword(String username, String email) {
        return false;
    }

    public void fixPassword(String userName, String passwordNew) {
    }
}
class ShopUser{

    public void checkout() {
    }

    public void showHistory() {
    }

    public void showCurrentChat() {
    }

    public void modifyDuctionInfo(String shopID) {
    }

    public void removeDuction(String shopID) {
    }

    public boolean addDuction(String shopID, String shopNum) {
        return false;
    }
}