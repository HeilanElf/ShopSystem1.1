package org.example;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsTrueEnter {
    Scanner scanner = new Scanner(System.in);

    public int inthefa(int n) {
        int command;
        while (true) {
            try {
                command = Integer.parseInt(scanner.next());
                if (command >= 0 && command <= n) {
                    break;
                } else {
                    System.out.print("选择错误，请重新输入：");
                }
            } catch (NumberFormatException e) {
                System.out.print("输入非法，请重新输入：");
            }
        }
        return command;
    }

    public String passwordhefa(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d!@#$%^&*()_+]{8,}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);

        while (!matcher.matches()) {
            if (!Pattern.compile(".*[a-z].*").matcher(password).matches()) {
                System.out.print("密码必须包含小写字母，请重新输入：");
            } else if (!Pattern.compile(".*[A-Z].*").matcher(password).matches()) {
                System.out.print("密码必须包含大写字母，请重新输入：");
            } else if (!Pattern.compile(".*\\d.*").matcher(password).matches()) {
                System.out.print("密码必须包含数字，请重新输入：");
            } else if (!Pattern.compile(".*[!@#$%^&*()_+].*").matcher(password).matches()) {
                System.out.print("密码必须包含特殊字符，请重新输入：");
            } else if (password.length() < 8) {
                System.out.print("密码长度至少为8位，请重新输入：");
            }
            password = scanner.next();
            matcher = regex.matcher(password);
        }

        return password;
    }
}
