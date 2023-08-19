package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Menu {
    Regest regest = new Regest();
    Scanner scanner = new Scanner(System.in);
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//text//";

    public void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void consoleDelay(long second) {
        try {
            Thread.sleep(second); // 延迟三秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String xuanze;

    public void next() {
        System.out.println("请按任意键继续..");
        
        xuanze = scanner.nextLine();
    }

    private void showFileContent(String fileName) {
        clear();
        String filePath = basePath + fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHome() {
        showFileContent("Home.txt");
    }

    public void showMaster() {
        clear();
        System.out.println("============================================");
        System.out.println("               管理员" + regest.getCurrentuserName() + ",你好！");
        System.out.println("   您可以做以下操作：");
        System.out.println("       0--退出登录");
        System.out.println("       1--密码管理");
        System.out.println("       2--客户管理");
        System.out.println("       3--商品管理");
        System.out.println("============================================");
    }

    public void showpasswordMaster() {
        showFileContent("passwordMaster.txt");
    }

    public void showductionMaster() {
        showFileContent("ductionMaster.txt");
    }

    public void showuserMaster() {
        showFileContent("userMaster.txt");
    }

    public void showCheck() {
        showFileContent("CheckStyle.txt");
    }

    public void showpasswordUser() {
        showFileContent("passwordUser.txt");
    }

    public void showShopUser() {
        showFileContent("ShopUser.txt");
    }

    public void showUser() {
        showFileContent("UserHome.txt");
    }

    public void showSearchStyle() {
        showFileContent("SearchStyle.txt");
    }

    public void showSecondUser() {
        clear();
        System.out.println("==============================");
        System.out.println("      欢迎您！" + regest.getCurrentuserName());
        System.out.println("         0--退出登录");
        System.out.println("         1--密码管理");
        System.out.println("         2--开始购物");
        System.out.println("==============================");
    }

    public void showDuctionModify() {
        showFileContent("DuctionModify.txt");
    }
}
