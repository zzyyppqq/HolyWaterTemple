package com.holywatertemple;

import android.os.Environment;

import java.io.File;

public class Config {

    public static final String EXCEL_IMPORT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "person.xls";
    public static final String EXCEL_FILE_EXPORT_PORT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator ;
}
