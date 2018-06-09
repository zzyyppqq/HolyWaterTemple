package com.holywatertemple;

import android.os.Environment;

import java.io.File;

public class Config {

    public static final String EXCEL_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "person.xls";
}
