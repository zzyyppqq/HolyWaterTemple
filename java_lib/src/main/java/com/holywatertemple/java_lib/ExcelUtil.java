package com.holywatertemple.java_lib;

import com.holywatertemple.java_lib.bean.Header;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.java_lib.bean.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {

    public static Table readExcel(String excelFilePath) {
        System.out.println("excel读取开始");
        try {
            Workbook book = Workbook.getWorkbook(new File(excelFilePath));
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);

            String sheetName = sheet.getName();
            String title = sheet.getCell(0, 0).getContents();

            Table table = new Table();
            List<Person> personList = new ArrayList<>();

            int rows = sheet.getRows();
            for (int i = 0; i < rows; ++i) {
                String jossId = (sheet.getCell(0, i)).getContents();
                String name = (sheet.getCell(1, i)).getContents();
                String phoneNum = (sheet.getCell(2, i)).getContents();
                String jossType = (sheet.getCell(3, i)).getContents();
                String fendPrice = (sheet.getCell(4, i)).getContents();
                String fendTime = (sheet.getCell(5, i)).getContents();
                String extendTime = (sheet.getCell(6, i)).getContents();
                if (i == 1) {
                    Header header = new Header(jossId, name, phoneNum, jossType, fendPrice, fendTime, extendTime, title, sheetName);
                    System.out.println(i + ": " + header.toString());
                    table.setHeader(header);
                }

                if (i >= 3 && !jossId.equals("")) {
                    Person person = new Person(jossId, name, phoneNum, jossType, fendPrice, fendTime, extendTime);
                    System.out.println(i + ": " + person.toString());
                    personList.add(person);
                }

            }
            table.setPersonList(personList);
            book.close();
            System.out.println("excel读取完成");
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(String filePath) {
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
