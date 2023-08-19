package org.example;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Master {
    Menu menu = new Menu();
    PasswordMaster passWordMaster = new PasswordMaster();
    UserMaster userMaster = new UserMaster();
    DuctionMaster ductionMaster = new DuctionMaster();
    Scanner scanner = new Scanner(System.in);
    Regest regest = new Regest();
    LogIn logIn = new LogIn();
    IsTrueEnter isTrueEnter = new IsTrueEnter();

    public void master(int command) {
        while (true) {
            if (command == 0) {
                break;
            } else {
                switch (command) {
                    case 1:
                        while (true) {
                            menu.showpasswordMaster();
                            System.out.print("请输入您的选择：");
                            command = isTrueEnter.inthefa(2);
                            if (command == 0) {
                                break;
                            } else {
                                passwordMaster(command);
                            }
                        }
                        break;
                    case 2:
                        while (true) {
                            menu.showuserMaster();
                            System.out.print("请输入您的选择：");
                            command = isTrueEnter.inthefa(3);
                            if (command == 0) {
                                break;
                            } else {
                                userMaster(command);
                            }
                        }
                        break;
                    case 3:
                        while (true) {
                            menu.showductionMaster();
                            System.out.print("请输入您的选择：");
                            command = isTrueEnter.inthefa(5);
                            if (command == 0) {
                                break;
                            } else {
                                ductionMaster(command);
                            }
                        }
                        break;
                }
            }
            menu.showMaster();
            System.out.print("请输入您的选择：");
            command = isTrueEnter.inthefa(3);
        }
    }

    public void passwordMaster(int command) {
        if (command == 1) {
            String userName = regest.getCurrentuserName();
            System.out.print("请输入您的旧密码：");
            String passwordOlder = scanner.next();
            if (logIn.adminLogin(userName, passwordOlder)) {
                System.out.print("请输入您的新密码：");
                String passwordNew = scanner.next();
                System.out.print("请确认您的新密码：");
                String passwordNew1 = scanner.next();
                while (true) {
                    if (passwordNew1.equals(passwordNew)) {
                        break;
                    }
                    System.out.println("您的新密码不一致！请重新输入：");
                    System.out.print("请输入您的新密码：");
                    passwordNew = scanner.next();
                    System.out.print("请确认您的新密码：");
                    passwordNew1 = scanner.next();
                }
                passWordMaster.modifySelfPassword(userName, passwordNew);
            }
        }

        if (command == 2) {
            System.out.print("请输入用户名：");
            String userName = scanner.next();
            if (logIn.userExit(userName)) {
                System.out.print("是否确定重置" + userName + "的密码：(Y/N)");
                String confirm = scanner.next();
                if (confirm.equalsIgnoreCase("Y")) {
                    passWordMaster.resetUserPassword(userName);
                }
            } else {
                System.out.println("该用户不存在！");
            }


        }
    }

    public void userMaster(int command) {
        if (command == 1) {
            userMaster.showUserData();
            menu.next();
        }
        if (command == 2) {
            System.out.print("请输入要删除的用户ID：");
            String userID = scanner.next();
            userMaster.deleteUserData(userID);
            menu.next();
        }
        if (command == 3) {
            menu.showSearchStyle();
            System.out.print("请选择输入您的选择：");
            int fangshi = isTrueEnter.inthefa(3);
            while (true) {
                if (fangshi == 0) {
                    break;
                } else {
                    switch (fangshi) {
                        case 1:
                            System.out.print("请输入用户ID：");
                            String userID = scanner.next();
                            userMaster.searchUserData(userID, "ID");
                            break;
                        case 2:
                            System.out.print("请输入用户名：");
                            String username = scanner.next();
                            userMaster.searchUserData(username, "Name");
                            break;
                        case 3:
                            userMaster.showUserData();
                            break;
                    }
                    menu.next();
                    menu.showSearchStyle();
                    System.out.print("请选择您的查询方式：");
                    fangshi = isTrueEnter.inthefa(3);
                }
            }
        }
    }

    public void ductionMaster(int command) {
        if (command == 1) {
            ductionMaster.showDuctionInfo();
        }
        if (command == 2) {
            ductionMaster.addDuctionInfo();
        }
        if (command == 3) {
            System.out.print("请输入要修改的商品编号:");
            String shopID=scanner.next();
            ductionMaster.modifyDuctionInfo(shopID);
        }
        if (command == 4) {
            System.out.print("请输入要删除的商品编号：");
            String id = scanner.nextLine();
            ductionMaster.deleteDuctionInfo(id);
        }
        if (command == 5) {
            ductionMaster.searchDuctionInfo();
        }
        menu.next();
    }

}

class PasswordMaster {
    Regest regest = new Regest();
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";

    public void modifySelfPassword(String userName, String passwordNew) {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "Master";
        int modifyRow = getRowNumber(filePath, sheetName, userName, 1);//表的第1列查找
        if (regest.modifyCellValue(filePath, sheetName, modifyRow, 2, passwordNew)) {//密码在2列
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    public void resetUserPassword(String useName) {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "PasswordMaster";
        int resetRow = getRowNumber(filePath, sheetName, useName, 1);
        if (regest.modifyCellValue(filePath, sheetName, resetRow, 2, "@SPGL1234system")) {
            System.out.println("重置成功！");
        } else {
            System.out.println("重置失败！");
        }
    }

    public int getRowNumber(String filePath, String sheetName, String key, int colum) {
        int rowNumber = -1;

        try (InputStream inp = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell cell = row.getCell(colum - 1); // Assuming username is in the first column

                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();

                    if (cellValue.equals(key)) {
                        rowNumber = row.getRowNum();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowNumber;
    }
}

class UserMaster {
    PasswordMaster passwordMaster=new PasswordMaster();
    Scanner scanner = new Scanner(System.in);
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";

    public void showUserData() {
        String filePath = basePath + "Master.xlsx";
        String sheetName = "UserMaster";
        List<String> userInfoList = readSheetData(filePath, sheetName);
        if (userInfoList.isEmpty()) {
            System.out.println("当前用户信息为空！");
        } else {
            System.out.println("当前用户信息如下：");
            System.out.println("-----------------------------------------------------------------");
            for (String userinfo : userInfoList) {
                String[] parts = userinfo.split(",");
                String output = "用户ID:" + parts[0] + " 用户名称:" + parts[1] + " 用户级别:" + parts[2] +
                        " 注册时间:" + parts[3] + " 累计消费金额:" + parts[4] + " 电话号码:" + parts[5] + " 邮箱:" + parts[6];
                System.out.println(output);
            }
        }
    }
    public void showUserInfo(List<String> infoList){
        if(infoList.isEmpty()){
            System.out.println("此类用户信息不存在！");
        }else{
            System.out.println("用户信息如下：");
            System.out.println("----------------------------------------------------------");
            for(String userinfo:infoList){
                String[] parts=userinfo.split(",");
                String output = "用户ID:" + parts[0] + " 用户名称:" + parts[1] + " 用户级别:" + parts[2] +
                        " 注册时间:" + parts[3] + " 累计消费金额:" + parts[4] + " 电话号码:" + parts[5] + " 邮箱:" + parts[6];
                System.out.println(output);
            }
        }
    }
    public void searchUserData(String content, String key) {
        String filePath=basePath+"Master.xlsx";
        String sheetName="UserMaster";
        if(key.equals("Name")){
            List<String> userinfoList=getRowsDataByKeyword(filePath,sheetName,1,content);
            showUserInfo(userinfoList);
        }
        if(key.equals("ID")){
            List<String> userinfoList=getRowsDataByKeyword(filePath,sheetName,0,content);
            showUserInfo(userinfoList);
        }
    }

    public void deleteUserData(String userID) {
        String filePath=basePath+"Master.xlsx";
        String sheetName="UserMaster";
        int deleteRow=passwordMaster.getRowNumber(filePath,sheetName,userID,1);
        if(deleteRowByIndex(filePath,sheetName,deleteRow)){
            System.out.println("删除成功！");
        }else{
            System.out.println("删除失败！");
        }

    }

    public List<String> readSheetData(String filePath, String sheetName) {
        List<String> infoList = new ArrayList<>();

        try (InputStream inp = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                // 获取行数
                int rowCount = sheet.getLastRowNum();

                // 从第二行开始遍历
                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    StringBuilder rowData = new StringBuilder();
                    if (row != null) {
                        for (Cell cell : row) {
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case STRING:
                                        rowData.append(cell.getStringCellValue()).append(",");
                                        break;
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            rowData.append(dateFormat.format(date)).append(",");
                                        } else {
                                            double value = cell.getNumericCellValue();
                                            long longValue = (long) value;
                                            String stringValue = String.valueOf(longValue);
                                            rowData.append(stringValue).append(",");
                                        }
                                        break;
                                    case BOOLEAN:
                                        rowData.append(cell.getBooleanCellValue()).append(",");
                                        break;
                                    default:
                                        rowData.append("").append(",");
                                }
                            } else {
                                rowData.append("").append(",");
                            }
                        }
                        // 删除最后一个逗号
                      //  rowData.deleteCharAt(rowData.length() - 1);
                        infoList.add(rowData.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoList;
    }
    public  List<String> getRowsDataByKeyword(String filePath, String sheetName, int keywordColumnIndex, String key) {
        List<String> infoList = new ArrayList<>();

        try (InputStream inp = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println(sheetName+"工作表不存在 "  );
                return infoList;
            }

            int lastRowNum = sheet.getLastRowNum();
            for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(keywordColumnIndex);
                    if (cell != null && cell.getCellType() == CellType.STRING &&
                            key.equals(cell.getStringCellValue())) {
                        StringBuilder rowInfo = new StringBuilder();
                        int lastCellNum = row.getLastCellNum();
                        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                            Cell dataCell = row.getCell(cellIndex);
                            if (dataCell != null) {
                                dataCell.setCellType(CellType.STRING);
                                rowInfo.append(dataCell.getStringCellValue()).append(",");
                            } else {
                                rowInfo.append(",");
                            }
                        }
                        rowInfo.deleteCharAt(rowInfo.length() - 1);  // 删除最后一个逗号
                        infoList.add(rowInfo.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoList;
    }

    public boolean deleteRowByIndex(String filePath, String sheetName, int rowIndex) {
        try (InputStream inp = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet not found: " + sheetName);
                return false;
            }

            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                sheet.removeRow(row);

                // 移动删除行下方的行，从而消除空行
                int lastRowNum = sheet.getLastRowNum();
                for (int i = rowIndex; i < lastRowNum; i++) {
                    sheet.shiftRows(i + 1, lastRowNum, -1);
                }
            }

            try (OutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

class DuctionMaster {
    PasswordMaster passwordMaster=new PasswordMaster();
    UserMaster userMaster=new UserMaster();
    Regest regest=new Regest();
    Menu menu=new Menu();
    IsTrueEnter isTrueEnter=new IsTrueEnter();
    Scanner scanner = new Scanner(System.in);
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";
    public void searchDuctionInfo() {
        String filePath=basePath+"Master.xlsx";
        String sheetName="DuctionMaster";
        System.out.println("请选择查询方式：");
        System.out.println("(1) 根据商品名称查询");
        System.out.println("(2) 根据生产厂家查询");
        System.out.println("(3) 根据零售价格查询");
        System.out.println("(4) 组合查询");
        System.out.println("----------------------------------------");
        System.out.print("请选择操作：");
        int choice = isTrueEnter.inthefa(4);
        List<String> productList =userMaster.readSheetData(filePath,sheetName);
        List<String> searchResults = new ArrayList<>();

        switch (choice) {
            case 1:
                System.out.print("请输入商品名称：");
                String name = scanner.nextLine();

                for (String productInfo : productList) {
                    String[] parts = productInfo.split(",");
                    if (parts[1].equals(name)) {
                        searchResults.add(productInfo);
                    }
                }

                break;
            case 2:
                System.out.print("请输入生产厂家：");
                String manufacturer = scanner.nextLine();

                for (String productInfo : productList) {
                    String[] parts = productInfo.split(",");
                    if (parts[2].equals(manufacturer)) {
                        searchResults.add(productInfo);
                    }
                }

                break;
            case 3:
                System.out.print("请输入零售价格下限：");
                double minRetailPrice = scanner.nextDouble();
                scanner.nextLine(); // 清空输入缓冲区

                System.out.print("请输入零售价格上限：");
                double maxRetailPrice = scanner.nextDouble();
                scanner.nextLine(); // 清空输入缓冲区

                for (String productInfo : productList) {
                    String[] parts = productInfo.split(",");
                    double retailPrice = Double.parseDouble(parts[6]);
                    if (retailPrice >= minRetailPrice && retailPrice <= maxRetailPrice) {
                        searchResults.add(productInfo);
                    }
                }

                break;
            case 4:
                System.out.print("请输入商品名称：");
                name = scanner.nextLine();

                System.out.print("请输入生产厂家：");
                manufacturer = scanner.nextLine();

                System.out.print("请输入零售价格下限：");
                minRetailPrice = scanner.nextDouble();
                scanner.nextLine(); // 清空输入缓冲区

                System.out.print("请输入零售价格上限：");
                maxRetailPrice = scanner.nextDouble();
                scanner.nextLine(); // 清空输入缓冲区

                for (String productInfo : productList) {
                    String[] parts = productInfo.split(",");
                    if (parts[1].equals(name) && parts[2].equals(manufacturer)) {
                        double retailPrice = Double.parseDouble(parts[6]);
                        if (retailPrice >= minRetailPrice && retailPrice <= maxRetailPrice) {
                            searchResults.add(productInfo);
                        }
                    }
                }

                break;
            default:
                System.out.println("无效输入！");
                return;
        }

        if (searchResults.isEmpty()) {
            System.out.println("未找到符合条件的商品。");
        } else {
            System.out.println("查询结果：");
            showDuctionData(searchResults);
        }
    }

    private void showDuctionData(List<String> userInfoList) {
        if (userInfoList.isEmpty()) {
            System.out.println("当前商品信息为空！");
        } else {
            System.out.println("-----------------------------------------------------------------");
            for (String userinfo : userInfoList) {
                String[] parts = userinfo.split(",");
                String output = "商品编号:" + parts[0] + " 商品名称:" + parts[1] + " 生产厂家:" + parts[2] +
                        " 生产日期:" + parts[3] + " 型号:" + parts[4] + " 进货价:" + parts[5] + "￥ 零售价格:" + parts[6]+"￥ 库存数量:"+parts[7];
                System.out.println(output);
            }
            System.out.println("-----------------------------------------------------------------");
        }
    }
    public void deleteDuctionInfo(String shopID) {
        String filePath=basePath+"Master.xlsx";
        String sheetName="DuctionMaster";
        int deleteRow=passwordMaster.getRowNumber(filePath,sheetName,shopID,1);
        if(deleteRow>0){
            System.out.print("是否确认删除该商品：(Y/N)");
            String confim=scanner.next();
            if(confim.equalsIgnoreCase("Y")){
                if(userMaster.deleteRowByIndex(filePath,sheetName,deleteRow)){
                    System.out.println("删除成功！");
                }else{
                    System.out.println("删除失败！");
                }
            }else{
                System.out.println("您已取消删除！");
            }
        }
    }

    public void modifyDuctionInfo(String shopID) {
        String filePath=basePath+"Master.xlsx";
        String sheetName="DuctionMaster";
        int modifyRow=passwordMaster.getRowNumber(filePath,sheetName,shopID,1);
        List<String> ductioninfoList=userMaster.getRowsDataByKeyword(filePath,sheetName,0,shopID);
        if(ductioninfoList.isEmpty()){
            System.out.println("当前商品信息为空！");
        }else{
            showDuctionData(ductioninfoList);
            Map<Integer, String> modifymap=getColum();
            int modifyColum=modifymap.keySet().iterator().next();
            String modifyContent=modifymap.get(modifyColum);
            if(modifyColum>0){
                if(regest.modifyCellValue(filePath,sheetName,modifyRow,modifyColum,modifyContent)){
                    System.out.println("修改成功！");
                }else{
                    System.out.println("修改失败！");
                }
            }else{
                System.out.println("您未作任何修改！");
            }
        }
    }



    public  Map<Integer, String> getColum() {
        int colum = 0;
        String newValue = "";
        menu.showDuctionModify();
        System.out.print("请输入您的选择：");
        int option =isTrueEnter.inthefa(7);
        switch (option) {
            case 1:
                System.out.print("请输入新的商品名称（不修改请直接回车）：");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) {
                    newValue = newName;
                    colum = 2;
                }
                break;
            case 2:
                System.out.print("请输入新的生产厂家（不修改请直接回车）：");
                String newManufacturer = scanner.nextLine();
                if (!newManufacturer.isEmpty()) {
                    newValue = newManufacturer;
                    colum = 3;
                }
                break;
            case 3:
                System.out.print("请输入新的生产日期（不修改请直接回车）：");
                String newProductionDate = scanner.nextLine();
                if (!newProductionDate.isEmpty()) {
                    newValue = newProductionDate;
                    colum = 4;
                }
                break;
            case 4:
                System.out.print("请输入新的型号（不修改请直接回车）：");
                String newModel = scanner.nextLine();
                if (!newModel.isEmpty()) {
                    newValue = newModel;
                    colum = 5;
                }
                break;
            case 5:
                System.out.print("请输入新的进货价（不修改请直接回车）：");
                String newPurchasePriceStr = scanner.nextLine();
                if (!newPurchasePriceStr.isEmpty()) {
                    double newPurchasePrice = Double.parseDouble(newPurchasePriceStr);
                    newValue = String.format("%.2f", newPurchasePrice);
                    colum = 6;
                }
                break;
            case 6:
                System.out.print("请输入新的零售价格（不修改请直接回车）：");
                String newRetailPriceStr = scanner.nextLine();
                if (!newRetailPriceStr.isEmpty()) {
                    double newRetailPrice = Double.parseDouble(newRetailPriceStr);
                    newValue = String.format("%.2f", newRetailPrice);
                    colum = 7;
                }
                break;
            case 7:
                System.out.print("请输入新的数量（不修改请直接回车）：");
                String newQuantityStr = scanner.nextLine();
                if (!newQuantityStr.isEmpty()) {
                    int newQuantity = Integer.parseInt(newQuantityStr);
                    newValue = String.valueOf(newQuantity);
                    colum = 8;
                }
                break;
            default:
                System.out.println("无效的选项！");
        }
        return Map.of(colum, newValue);
    }


    public void addDuctionInfo() {
        String filePath=basePath+"Master.xlsx";
        String sheetName="DuctionMaster";
        System.out.print("请输入商品编号：");
        String id = scanner.nextLine();
        System.out.print("请输入商品名称：");
        String name = scanner.nextLine();
        System.out.print("请输入生产厂家：");
        String manufacturer = scanner.nextLine();
        System.out.print("请输入生产日期：");
        String productionDate = scanner.nextLine();
        System.out.print("请输入型号：");
        String model = scanner.nextLine();
        System.out.print("请输入进货价：");
        double purchasePrice = scanner.nextDouble();
        System.out.print("请输入零售价格：");
        double retailPrice = scanner.nextDouble();
        System.out.print("请输入数量：");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 清空输入缓冲区

        String productInfo = String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%d", id, name, manufacturer,
                productionDate, model, purchasePrice, retailPrice, quantity);

        List<String> ductionList=List.of(productInfo);

        regest.fillCellWithData(filePath,sheetName,ductionList);
        System.out.println("商品添加成功！");
    }

    public void showDuctionInfo() {
        String filePath=basePath+"Master.xlsx";
        String sheetName="DuctionMaster";
        List<String> userInfoList = userMaster.readSheetData(filePath, sheetName);
        System.out.println("商品信息如下：");
        showDuctionData(userInfoList);
    }
}