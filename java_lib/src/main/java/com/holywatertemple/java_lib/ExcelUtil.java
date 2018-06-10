package com.holywatertemple.java_lib;

import com.holywatertemple.java_lib.bean.Header;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.java_lib.bean.Table;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {

    private static SimpleDateFormat SF_date = new SimpleDateFormat("yyyy年M月d日");

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

                if (i == 1) {
                    String fendTime = (sheet.getCell(5, i)).getContents();
                    String extendTime = (sheet.getCell(6, i)).getContents();
                    Header header = new Header(jossId, name, phoneNum, jossType, fendPrice, fendTime, extendTime, title, sheetName);
                    System.out.println(i + ": " + header.toString());
                    table.setHeader(header);
                }

                if (i >= 2 && !jossId.equals("")) {
                    String fendTime = convertDateToStr(sheet.getCell(5, i));
                    String extendTime = convertDateToStr(sheet.getCell(6, i));
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

    private static String convertDateToStr(Cell cell) {
        if (cell instanceof DateCell) {
            DateCell dateCell = (DateCell) cell;
            return SF_date.format(dateCell.getDate());
        }
        return cell.getContents();
    }

    public static void writeExcel(String filePath, Table table) {
            System.out.println("excel写入开始");

        WritableWorkbook book;
        try {
            //打开文件
            book = Workbook.createWorkbook(new File(filePath));

            Header header = table.getHeader();
            String title = header.getTitle();
            String sheetName = header.getSheetName();
            List<Person> personList = table.getPersonList();

            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet(sheetName, 0);

            //在Label对象的构造中指名单元格位置是第一列第一行(0,0)
            //以及单元格内容为Hello World

            WritableFont wf_title = new WritableFont(WritableFont.ARIAL, 11,
                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色

            WritableCellFormat wcf_title = new WritableCellFormat(wf_title); // 单元格定义
            wcf_title.setBackground(jxl.format.Colour.WHITE); // 设置单元格的背景颜色
            wcf_title.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式

            int size = personList.size();
            for (int i = 0; i < size + 2; i++) {
                if (i == 0) {
                    Label titleLabel = new Label(i, 0, title,wcf_title);
                    sheet.addCell(titleLabel);
                    sheet.mergeCells(0,0,5,0);

                    continue;
                }
                if (i == 1) {
                    System.err.println(header.toString());
                    sheet.addCell(new Label(0, i, header.getJossId()));
                    sheet.addCell(new Label(1, i, header.getName()));
                    sheet.addCell(new Label(2, i, header.getPhoneNum()));
                    sheet.addCell(new Label(3, i, header.getJossType()));
                    sheet.addCell(new Label(4, i, header.getFendPrice()));
                    sheet.addCell(new Label(5, i, header.getFendTime()));
                    sheet.addCell(new Label(6, i, header.getExtendTime()));

                    continue;
                }
                if (i >= 2) {
                    Person person = personList.get(i - 2);
                    sheet.addCell(new Label(0, i, person.getJossId()));
                    sheet.addCell(new Label(1, i, person.getName()));
                    sheet.addCell(new Label(2, i, person.getPhoneNum()));
                    sheet.addCell(new Label(3, i, person.getJossType()));
                    sheet.addCell(new Label(4, i, person.getFendPrice()));

                    Date fendTimeDate = parseDate(person.getFendTime());
                    if (fendTimeDate == null){
                        sheet.addCell(new Label(5, i,person.getExtendTime()));
                    }else {
                        sheet.addCell(new DateTime(5, i,fendTimeDate));
                    }

                    Date extendTimeDate = parseDate(person.getExtendTime());
                    if (extendTimeDate == null) {
                        sheet.addCell(new Label(6, i, person.getExtendTime()));
                    }else {
                        sheet.addCell(new DateTime(6, i, extendTimeDate));
                    }
                }

            }
            //写入数据并关闭文
            book.write();
            book.close();
            System.out.println("excel写入完成");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static Date parseDate(String dataStr){
        if (dataStr == null || dataStr.trim().equals("")) {
            return null;
        }else {
            try {
              return  SF_date.parse(dataStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
