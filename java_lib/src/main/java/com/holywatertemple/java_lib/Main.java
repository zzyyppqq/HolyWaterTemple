package com.holywatertemple.java_lib;

import com.holywatertemple.java_lib.bean.Table;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("---start---");

        Table table = ExcelUtil.readExcel("/Users/zhangyipeng/Documents/HolyWaterTemple/java_lib/file/person.xls");

        String filePath = "/Users/zhangyipeng/Documents/HolyWaterTemple/java_lib/out/person" + new Date().toLocaleString() + ".xls";

        ExcelUtil.writeExcel(filePath, table);

    }


}
