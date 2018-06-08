package com.holywatertemple.java_lib;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Main {
    public static void main(String[] args) {
        System.out.println("===");

    }

    public static void smart(String filePath) {
        WritableWorkbook book;
        try {
            System.out.println("---start---");
            //打开文件
            book = Workbook.createWorkbook(new File(filePath));

            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("sheet_one", 0);

            //在Label对象的构造中指名单元格位置是第一列第一行(0,0)
            //以及单元格内容为Hello World
            Label label = new Label(0, 0, "Hello World");

            //将定义好的单元格添加到工作表中
            sheet.addCell(label);

             /*生成一个保存数字的单元格
              必须使用Number的完整包路径，否则有语法歧义
              单元格位置是第二列，第一行，值为789.123*/
            jxl.write.Number num = new jxl.write.Number(0, 1, 123.456);
            sheet.addCell(num);

            //写入数据并关闭文
            book.write();
            book.close();
            System.out.println("---end---");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
