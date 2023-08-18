package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Master {
//    private String basePath=System.getProperty("user.dir")+"//src//main//java//org//example//text//";
//    String signfilePath;
    Menu menu=new Menu();
    PasswordMaster passWordMaster=new PasswordMaster();
    UserMaster userMaster=new UserMaster();
    DuctionMaster ductionMaster=new DuctionMaster();
    Scanner scanner=new Scanner(System.in);
    Regest regest=new Regest();
    IsTrueEnter isTrueEnter=new IsTrueEnter();

    public void master(int command){
        while(true){
            if (command==0){
                break;
            }else{
                switch (command){
                    case 1:
                        while(true){
                            menu.showpasswordMaster();
                            System.out.print("请输入您的选择：");
                            command=isTrueEnter.inthefa(2);
                            if(command==0){
                                break;
                            }else{
                                passwordMaster(command);
                            }
                        }
                        break;
                    case 2:
                        while(true){
                            menu.showuserMaster();
                            System.out.print("请输入您的选择：");
                            command=isTrueEnter.inthefa(3);
                            if(command==0){
                                break;
                            }else{
                                userMaster(command);
                            }
                        }
                        break;
                    case 3:
                        while(true){
                            menu.showductionMaster();
                            System.out.print("请输入您的选择：");
                            command=isTrueEnter.inthefa(5);
                            if(command==0){
                                break;
                            }else{
                                ductionMaster(command);
                            }
                        }
                        break;
                }
            }
            menu.showMaster();
            System.out.print("请输入您的选择：");
            command=isTrueEnter.inthefa(3);
        }
    }
    public void passwordMaster(int command){
        if(command==1){
            passWordMaster.modifySelfPassword();
        }
        if(command==2){
            passWordMaster.resetUserPassword();
        }
    }
    public void userMaster(int command){
        if(command==1){
            userMaster.showUserData();
            menu.next();
        }
        if(command==2){
            System.out.print("请输入要删除的用户ID：");
            String userID=scanner.next();
            userMaster.deleteUserData(userID);
            menu.next();
        }
        if(command==3){
            menu.showSearchStyle();
            System.out.print("请选择输入您的选择：");
            int fangshi=isTrueEnter.inthefa(3);
            while(true){
                if(fangshi==0){
                    break;
                }else{
                    switch(fangshi){
                        case 1:
                            System.out.print("请输入用户ID：");
                            String userID=scanner.next();
                            userMaster.searchUserData(userID,"ID");
                            break;
                        case 2:
                            System.out.print("请输入用户名：");
                            String username=scanner.next();
                            userMaster.searchUserData(username,"Name");
                            break;
                        case 3:
                            userMaster.showUserData();
                            break;
                    }
                    menu.next();
                    menu.showSearchStyle();
                    System.out.print("请选择您的查询方式：");
                    fangshi=isTrueEnter.inthefa(3);
                }
            }
        }
    }
    public void ductionMaster(int command){
        if(command==1){
            ductionMaster.showDuctionInfo();
        }
        if(command==2){
            ductionMaster.addDuctionInfo();
        }
        if(command==3){
            ductionMaster.modifyDuctionInfo();
        }
        if(command==4){
            System.out.print("请输入要删除的商品编号：");
            String id = scanner.nextLine();
            ductionMaster.deleteDuctionInfo(id);
        }
        if(command==5){
            ductionMaster.searchDuctionInfo();
        }
        menu.next();
    }

}
class PasswordMaster{

    public void modifySelfPassword() {
    }

    public void resetUserPassword() {
    }
}
class UserMaster{

    public void showUserData() {
    }

    public void searchUserData(String username, String name) {
    }

    public void deleteUserData(String userID) {
    }
}
class DuctionMaster{

    public void searchDuctionInfo() {
    }

    public void deleteDuctionInfo(String id) {
    }

    public void modifyDuctionInfo() {
    }

    public void addDuctionInfo() {
    }

    public void showDuctionInfo() {
    }
}