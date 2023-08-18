package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class User {
    Menu menu = new Menu();
    Regest regest = new Regest();
    IsTrueEnter isTrueEnter = new IsTrueEnter();
    PasswordUser passwordUser = new PasswordUser();
    ShopUser shopUser = new ShopUser();
    LogIn logIn = new LogIn();
    Scanner scanner = new Scanner(System.in);

    public int passwordUser(int command) {
        int exit = 1;
        if (command == 1) {
            String userName = regest.getCurrentuserName();
            System.out.print("请确认旧密码：");
            String passwordOld = isTrueEnter.passwordhefa(scanner.next());
            System.out.print("请输入新密码：");
            String passwordNew = isTrueEnter.passwordhefa(scanner.next());
            if (logIn.userLogin(userName, passwordOld)) {
                passwordUser.fixPassword(userName, passwordNew);
            } else {
                System.out.println("密码不匹配！");
            }
        }
        if (command == 2) {
            System.out.print("请输入用户名：");
            String username = scanner.next();
            System.out.print("请输入注册时使用的邮箱地址：");
            String email = scanner.next();
            boolean sucess = passwordUser.forgotPassword(username, email);
            if (sucess) {
                exit = 0;
            }
        }
        menu.next();
        return exit;
    }

    public int shopUser(int command) {
        DuctionMaster ductionMaster = new DuctionMaster();
        if (command == 1) {
            ductionMaster.showDuctionInfo();
            System.out.println("-------------------------------------------------");
            System.out.print("请输入商品编号：");
            String shopID = scanner.next();
            System.out.print("请输入购买数量：");
            String shopNum = scanner.next();
            boolean sucess = shopUser.addDuction(shopID, shopNum);
            if (sucess) {
                System.out.println("添加成功！");
                shopUser.showCurrentChat();
            } else {
                System.out.println("添加失败！");
            }
        }
        if (command == 2) {
            System.out.print("请输入商品编号：");
            String shopID = scanner.next();
            shopUser.showCurrentChat();
            shopUser.removeDuction(shopID);
            shopUser.showCurrentChat();
        }
        if (command == 3) {
            shopUser.showCurrentChat();
            System.out.print("请输入商品编号：");
            String shopID = scanner.next();
            shopUser.modifyDuctionInfo(shopID);
            shopUser.showCurrentChat();
        }
        if (command == 4) {
            shopUser.checkout();
        }
        if (command == 5) {
            shopUser.showHistory();
        }
        menu.next();
        return 1;
    }

    public void user(int command, boolean susucessLoin) {
        menu.showSecondUser();
        System.out.print("请输入您的选择：");
        command = isTrueEnter.inthefa(2);
        int exit = 1;
        while (true) {
            if (command == 0) {
                break;
            } else {
                switch (command) {
                    case 1:
                        menu.showpasswordUser();
                        System.out.print("请输入您的选择：");
                        command = isTrueEnter.inthefa(2);
                        while (true) {
                            if (command == 0) {
                                break;
                            } else {
                                exit = passwordUser(command);
                                if (exit == 0) {
                                    break;
                                }
                            }
                            if (exit != 0) {
                                menu.showpasswordUser();
                                System.out.print("请输入您的选择：");
                                command = isTrueEnter.inthefa(2);
                            }
                        }
                        break;
                    case 2:
                        menu.showShopUser();
                        System.out.print("请输入您的选择：");
                        command = isTrueEnter.inthefa(5);
                        while (true) {
                            if (command == 0) {
                                break;
                            } else {
                                exit = shopUser(command);
                                if (exit == 0) {
                                    break;
                                }
                            }
                            if (exit != 0) {
                                menu.showShopUser();
                                System.out.print("请输入您的选择：");
                                command = isTrueEnter.inthefa(5);
                            }
                        }
                        break;
                }
            }
            if (exit != 0) {
                menu.showSecondUser();
                System.out.print("请输入您的选择：");
                command = isTrueEnter.inthefa(2);
            }
        }
    }
}

class PasswordUser {
    Regest regest = new Regest();
    PasswordMaster passwordMaster = new PasswordMaster();
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";

    public boolean forgotPassword(String username, String email) {
        boolean sucess = false;
        if (isMatch(username, email)) {
            String newPassword = generateRandomPassword();
            sendEmail(email, newPassword);
            System.out.println("新密码已发送到您的邮箱，请查收。");
            System.out.println("请使用新密码登录，并尽快修改为您熟悉的密码。");
            fixPassword(username, newPassword);
            sucess = true;
        } else {
            System.out.println("用户名和邮箱地址不匹配!");
        }
        return sucess;
    }

    private boolean isMatch(String username, String email) {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "UserMaster";
        return checkMatch(filePath, sheetName, username, email);
    }

    private boolean checkMatch(String fileName, String sheetName, String username, String email) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell usernameCell = row.getCell(1); // 第二列为用户名
                Cell passwordCell = row.getCell(6); // 第六列为邮箱

                if (usernameCell != null && usernameCell.getCellType() == CellType.STRING &&
                        passwordCell != null && passwordCell.getCellType() == CellType.STRING) {

                    String storedUsername = usernameCell.getStringCellValue();
                    String storedPassword = passwordCell.getStringCellValue();

                    if (storedUsername.equals(username) && storedPassword.equals(email)) {
                        return true;
                    }
                }
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendEmail(String email, String newPassword) {
        System.out.println("已向邮箱 " + email + " 发送新密码：" + newPassword);
    }

    private String generateRandomPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$%^&*()_+";
        String numbers = "0123456789";

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // 生成包含至少一个大写字母、一个小写字母、一个特殊字符和一个数字的密码
        password.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        password.append(lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));

        // 生成剩余的密码字符
        for (int i = 4; i < 10; i++) {
            String allCharacters = uppercaseLetters + lowercaseLetters + specialCharacters + numbers;
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }

    public void fixPassword(String userName, String passwordNew) {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "PasswordMaster";
        int modifyRow = passwordMaster.getRowNumber(filePath, sheetName, userName, 1);//表的第1列查找
        if (regest.modifyCellValue(filePath, sheetName, modifyRow, 2, passwordNew)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }
}

class ShopUser {
    Regest regest = new Regest();
    UserMaster userMaster = new UserMaster();
    Scanner scanner = new Scanner(System.in);
    PasswordMaster passwordMaster = new PasswordMaster();
    Menu menu = new Menu();
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";

    public void checkout() {
        String filePath = basePath + "User.xlsx";
        menu.showCheck();
        System.out.print("请选择您的支付方式：");
        IsTrueEnter isTrueEnter = new IsTrueEnter();
        int check = isTrueEnter.inthefa(3);
        double xiaofei = getTotalPrice();
        System.out.print("共花费"+xiaofei+"￥，是否支付:(Y/N)");
        String confirm = scanner.next();
        if (confirm.equalsIgnoreCase("Y")) {
            switch (check) {
                case 1:
                    System.out.println("支付宝支付中！");
                    break;
                case 2:
                    System.out.println("微信支付中！");
                    break;
                case 3:
                    System.out.println("银行卡支付中！");
                    break;
            }
            if (updataInfo(xiaofei)) {
                clearSheetData(filePath, "ShopChat");
                System.out.println("支付成功！");
            } else {
                System.out.println("支付失败！");
            }
        } else {
            System.out.println("您已取消支付！");
        }
    }

    public void clearSheetData(String filePath, String sheetName) {
        try (InputStream inp = new FileInputStream(filePath)) {

            Workbook workbook = WorkbookFactory.create(inp);

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                int lastRowNum = sheet.getLastRowNum();
                for (int i = 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        int lastCellNum = row.getLastCellNum();
                        for (int j = row.getFirstCellNum(); j < lastCellNum; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                row.removeCell(cell);
                            }
                        }
                    }
                }
            }

            try (OutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean updataInfo(double price) {
        if (updateHistory()) {
            updateUserInfo(regest.getCurrentuserName(), price);
            updateStock();
            return true;
        }
        return false;
    }

    private void updateStock() {
        String filaPath = basePath + "User.xlsx";
        String filePath1 = basePath + "Master.xlsx";
        String sheetName = "ShopChat";
        String sheetName1 = "DuctionMaster";
        List<String> buyList = userMaster.readSheetData(filaPath, sheetName);
        if (buyList.isEmpty()) {
            System.out.println("购物车为空！");
        } else {
            for (String buyinfo : buyList) {
                String[] parts = buyinfo.split(",");
                int idRow = passwordMaster.getRowNumber(filePath1, sheetName1, parts[0], 1);

                int num = Integer.parseInt(getCellValue(filePath1, sheetName1, idRow, 7)) - Integer.parseInt(parts[6]);
                String newVlue = String.valueOf(num);
                regest.modifyCellValue(filePath1, sheetName1, idRow, 8, newVlue);
            }
        }
    }

    public String getCellValue(String filePath, String sheetName, int rowNumber, int columnNumber) {
        try (InputStream inp = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                Row row = sheet.getRow(rowNumber);
                if (row != null) {
                    Cell cell = row.getCell(columnNumber);
                    if (cell != null) {
                        // 根据单元格类型获取值
                        switch (cell.getCellType()) {
                            case STRING:
                                return cell.getStringCellValue();
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    return cell.getDateCellValue().toString();
                                } else {
                                    return String.valueOf(cell.getNumericCellValue());
                                }
                            case BOOLEAN:
                                return String.valueOf(cell.getBooleanCellValue());
                            default:
                                return "";
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private void updateUserInfo(String currentuserName, double price) {
        String filePath=basePath+"Master.xlsx";
        String sheetName="UserMaster";
        int modifyRow=passwordMaster.getRowNumber(filePath,sheetName,currentuserName,2);//第二列为用户名称
        String newLevel=getLevel(price);
        String newHost=String.valueOf(price);
        regest.modifyCellValue(filePath,sheetName,modifyRow,5,newHost);
        regest.modifyCellValue(filePath,sheetName,modifyRow,3,newLevel);
    }

    private String getLevel(double ptice) {
        if(ptice<=5000&&ptice>1000){
            return "银牌客户";
        }
        if(ptice>5000){
            return "金牌客户";
        }
        if(ptice<1000){
            return "铜牌客户";
        }
        return "";
    }

    private boolean updateHistory() {
        String filePath=basePath+"User.xlsx";
        String sheetName="ShopChat";

        String sheetName1=regest.getCurrentuserName();
        createHistory(filePath,sheetName1);
        List<String> buyList=userMaster.readSheetData(filePath,sheetName);
        if(buyList.isEmpty()){
            System.out.println("购物车为空！");
        }else{
            regest.fillCellWithData(filePath,sheetName1,buyList);
            return true;
        }
        return false;
    }

    private void createHistory(String filePath, String userName) {
        try (InputStream inp = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);

            // 检查工作表是否存在
            Sheet existingSheet = workbook.getSheet(userName);
            if (existingSheet == null) {
                Sheet sheet = workbook.createSheet(userName);

                // 创建第一行并设置字段名
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("商品编号");
                headerRow.createCell(1).setCellValue("商品名称");
                headerRow.createCell(2).setCellValue("生产厂家");
                headerRow.createCell(3).setCellValue("购买日期");
                headerRow.createCell(4).setCellValue("商品型号");
                headerRow.createCell(5).setCellValue("购买价格");
                headerRow.createCell(6).setCellValue("购买数量");
            }

            try (OutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getTotalPrice() {
        String filePath = basePath + "User.xlsx";
        String sheetName = "ShopChat";
        double sumprice = 0;
        List<String> buyList = userMaster.readSheetData(filePath, sheetName);
        if (buyList.isEmpty()) {
            return 0;
        } else {
            for (String buyinfo : buyList) {
                String[] parts = buyinfo.split(",");
                sumprice += Double.parseDouble(parts[5]) * Integer.parseInt(parts[6]);
            }
        }
        return sumprice;
    }

    public void showHistory() {
        String filePath = basePath + "User.xlsx";
        String sheetName =regest.getCurrentuserName();
        List<String> userInfoList = userMaster.readSheetData(filePath, sheetName);
        if (userInfoList.isEmpty()) {
            System.out.println("购物历史信息为空！");
        } else {
            System.out.println("购物历史信息如下：");
            System.out.println("-----------------------------------------------------------------");
            for (String userinfo : userInfoList) {
                String[] parts = userinfo.split(",");
                String output = "商品ID:" + parts[0] + " 商品名称:" + parts[1] + " 生产厂家:" + parts[2] +
                        " 购买日期:" + parts[3] + " 商品型号:" + parts[4] + " 购买价格:" + parts[5] + " 购买数量:" + parts[6];
                System.out.println(output);
            }
        }
    }

    public void showCurrentChat() {
        String filePath = basePath + "User.xlsx";
        String sheetName = "ShopChat";
        List<String> userInfoList = userMaster.readSheetData(filePath, sheetName);
        if (userInfoList.isEmpty()) {
            System.out.println("当前购物车空！");
        } else {
            System.out.println("当前购物车信息如下：");
            System.out.println("-----------------------------------------------------------------");
            for (String userinfo : userInfoList) {
                String[] parts = userinfo.split(",");
                String output = "商品ID:" + parts[0] + " 商品名称:" + parts[1] + " 生产厂家:" + parts[2] +
                        " 购买日期:" + parts[3] + " 商品型号:" + parts[4] + " 购买价格:" + parts[5] + " 购买数量:" + parts[6];
                System.out.println(output);
            }
        }
    }

    public void modifyDuctionInfo(String shopID) {
        String filePath = basePath + "User.xlsx";
        String sheetName = "ShopChat";
        int modifyRow = passwordMaster.getRowNumber(filePath, sheetName, shopID, 1);
        if (modifyRow > 0) {
            System.out.print("请输入新数量：(不修改直接回车)");
            String newValue = scanner.nextLine();
            if (!newValue.isEmpty()) {
                if (regest.modifyCellValue(filePath, sheetName, modifyRow, 7, newValue)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
            } else {
                System.out.println("您已取消修改！");
            }

        } else {
            System.out.println("购物车为空！");
        }
    }

    public void removeDuction(String shopID) {
        String filePath = basePath + "User.xlsx";
        String sheetName = "ShopChat";
        int deleteRow = passwordMaster.getRowNumber(filePath, sheetName, shopID, 1);
        if (deleteRow > 0) {
            System.out.print("是否确认移除该商品：(Y/N)");
            String confim = scanner.next();
            if (confim.equalsIgnoreCase("Y")) {
                if (userMaster.deleteRowByIndex(filePath, sheetName, deleteRow)) {
                    System.out.println("移除成功！");
                } else {
                    System.out.println("移除失败！");
                }
            } else {
                System.out.println("您已取消删除！");
            }
        } else {
            System.out.println("购物车为空！");
        }
    }

    public boolean addDuction(String shopID, String shopNum) {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "DuctionMaster";
        String filePath1 = basePath + "User.xlsx";
        String sheetName1 = "ShopChat";
        List<String> productList = userMaster.readSheetData(filePath, sheetName);
        List<String> addResults = new ArrayList<>();
        if (productList.isEmpty()) {
            System.out.println("当前没有商品！");
        } else {
            for (String productInfo : productList) {
                String[] parts = productInfo.split(",");
                if (parts[0].equals(shopID)) {
                    String reslut=parts[0]+","+parts[1]+","+parts[2]+","+regest.getCurrentDate()+","+
                            parts[4]+","+parts[6]+","+shopNum;
                    addResults.add(reslut);
                }
            }
            regest.fillCellWithData(filePath1, sheetName1, addResults);
            return true;
        }
        return false;
    }
}