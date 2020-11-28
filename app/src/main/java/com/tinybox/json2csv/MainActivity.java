package com.tinybox.json2csv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.PathUtils;
import com.tinybox.base.util.csv.json2csv.Json2CSVUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 在sdcard中生成result.csv
     */
    void test(){
        String path = PathUtils.getExternalStoragePath();
        String fileName = "result";
        String destFile = path + File.separator + fileName + ".csv";

        Json2CSVUtil.trans(getJson(),destFile);
    }

    private static String getJson() {
        return  "[" +
                "    {" +
                "        \"studentName\": \"Foo\"," +
                "        \"Age\": \"12\"," +
                "        \"subjects\": [" +
                "            {" +
                "                \"name\": \"English\"," +
                "                \"marks\": \"40\"" +
                "            }," +
                "            {" +
                "                \"name\": \"History\"," +
                "                \"marks\": \"50\"" +
                "            }" +
                "        ]" +
                "    }" +
                "]";
    }

    public void onClick(View view) {
        test();
    }
}