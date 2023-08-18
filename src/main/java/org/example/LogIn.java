package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class LogIn {
    IsTrueEnter isTrueEnter = new IsTrueEnter();
    Regest regest = new Regest();
    Scanner scanner = new Scanner(System.in);
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";
    private final String MASTER_FILE = basePath + "Master.xlsx";
    private final String USER_FILE = basePath + "User.xlsx";
    private final String PASSWORD_SHEET = "password";

    public boolean adminLogin(String username, String password) {
        return checkLogin(MASTER_FILE, "Master", username, password);
    }

    public boolean userLogin(String username, String password) {
        return checkLogin(MASTER_FILE, "PasswordMaster", username, password);
    }

    public boolean adminExit(String username) {
        return checkExit(MASTER_FILE, "Master", username);
    }

    public boolean userExit(String username) {
        return checkExit(MASTER_FILE, "PasswordMaster", username);
    }

    private boolean checkExit(String fileName, String sheetName, String username) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0); // 第一列为用户名
                if (usernameCell != null && usernameCell.getCellType() == CellType.STRING) {
                    String storedUsername = usernameCell.getStringCellValue();
                    if (storedUsername.equals(username)) {
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



    private boolean checkLogin(String fileName, String sheetName, String username, String password) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell usernameCell = row.getCell(0); // 第二列为用户名
                Cell passwordCell = row.getCell(1); // 第三列为密码

                if (usernameCell != null && usernameCell.getCellType() == CellType.STRING &&
                        passwordCell != null && passwordCell.getCellType() == CellType.STRING) {

                    String storedUsername = usernameCell.getStringCellValue();
                    String storedPassword = passwordCell.getStringCellValue();

                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
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

    public void register(String userName, String password) {
        String content = userName + "," + password;
        List<String> infoList = List.of(content);
        regest.fillCellWithData(MASTER_FILE, "PasswordMaster", infoList);
    }

    public int userSign(int command) {
        int count = 0;
        String userName;
        String password;
        if (command == 1) {
            System.out.print("请输入管理员名称：");
            userName = scanner.next();
            System.out.print("请输入您的密码：");
            password = scanner.next();
            boolean adminExists = adminExit(userName);
            if (!adminExists) {
                System.out.println("管理员不存在！");
                count = 5;
            } else {
                while (!adminLogin(userName, password) && count < 5) {
                    count++;
                    System.out.println("管理员不存在或密码错误！");
                    System.out.print("用户名：");
                    userName = scanner.next();
                    System.out.print("密码：");
                    password = isTrueEnter.passwordhefa(scanner.next());
                }
                regest.setCurrentUserName(userName);
            }
        }
        if (command == 2) {
            System.out.print("请输入用户名：");
            userName = scanner.next();
            System.out.print("请输您的密码：");
            password = isTrueEnter.passwordhefa(scanner.next());
            boolean userExists = userExit(userName);
            if (!userExists) {
                System.out.println("用户不存在！");
                count = 5;
            } else {
                while (!userLogin(userName, password) && count < 5) {
                    count++;
                    System.out.println("用户名或密码错误！");
                    System.out.print("用户名：");
                    userName = scanner.next();
                    System.out.print("密码：");
                    password = isTrueEnter.passwordhefa(scanner.next());
                }
                regest.setCurrentUserName(userName);
            }
        }
        if (count > 5) {
            System.out.println("由于您多次失败，请三小时后再试！");
            Menu menu = new Menu();
            long yanchi = 1000 * 60 * 60 * 3;
            menu.consoleDelay(yanchi);
        } else if (count == 5) {
            System.out.println("登录失败！");
        } else {
            System.out.println("登录成功！");
        }
        return count;
    }

    public void regestuser() {
        System.out.print("请输入您的用户名:");
        String userName = scanner.next();
        System.out.print("请输入您的密码：");
        String password = isTrueEnter.passwordhefa(scanner.next());
        System.out.print("请确认您的密码：");
        String password1 = isTrueEnter.passwordhefa(scanner.next());
        if (password1.equals(password)) {
            register(userName, password);
            regest.userRegest(userName);
        } else {
            System.out.println("密码不一致！");
        }
    }
}

class Regest {
    private final String basePath = System.getProperty("user.dir") + "//src//main//java//org//example//ShujuData//";
    public void userRegest(String userName) {
        String filePath=basePath+"Master.xlsx";
        String phoneNumber = getInput("请输入手机号：");
        String email = getInput("请输入邮箱：");
        String ID = getRandomID();
        String registDate = getCurrentDate();
        String content = ID + "," + userName + "," + "铜牌客户" +","+registDate+",0,"+phoneNumber+","+email;
        List<String> inforList=List.of(content);
        fillCellWithData(filePath,"UserMaster",inforList);
    }

    private String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);

    }

    private String getRandomID() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int LENGTH = 6;
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder();

        char firstChar = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
        sb.append(firstChar);

        for (int i = 0; i < LENGTH - 1; i++) {
            int randomDigit = RANDOM.nextInt(10);
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    private String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    public void fillCellWithData(String filePath, String sheetName, List<String> infoList) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);

            int rowIndex = findNextAvailableRow(sheet);
            for (String info : infoList) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }

                String[] infoArray = info.split(",");
                for (int i = 0; i < infoArray.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(infoArray[i]);
                }
                rowIndex++;
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int findNextAvailableRow(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return i;
            }
        }
        return lastRowNum + 1;
    }
    public void setCurrentUserName(String content) {
        final String filePath = System.getProperty("user.dir") + "//src//main//java//org//example//text//CurrentUser.txt";
        try {
            // 创建 FileWriter 对象，传入文件路径
            FileWriter fileWriter = new FileWriter(filePath);
            // 将内容写入文件
            fileWriter.write(content);
            // 刷新缓冲区数据到文件
            fileWriter.flush();
            // 关闭 FileWriter 对象
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
    }

    public String getCurrentuserName() {
        final String filePath = System.getProperty("user.dir") + "//src//main//java//org//example//text//CurrentUser.txt";
        String fileContent;
        try {
            // 使用 Files 类的静态方法读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            // 将字节数组转换为字符串
            fileContent = new String(fileBytes);
        } catch (IOException e) {
            System.out.println("读取文件时发生错误：" + e.getMessage());
            fileContent = "";
        }
        return fileContent;
    }

    public void modifyCellValue(String filePath, String sheetName, int rowIndex, int cellIndex, String newValue) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);

            // 获取要修改的行和单元格
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(cellIndex);

            // 修改单元格的值
            cell.setCellValue(newValue);

            // 保存修改后的 Excel 文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Excel文件已成功修改！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
