package com.tinybox.base.util.csv.json2csv;

import com.tinybox.base.util.csv.json2csv.parser.JSONFlattener;
import com.tinybox.base.util.csv.json2csv.writer.CSVWriter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: zjerry
 * created on: 2020/11/28 下午3:06
 * description:
 */
public class Json2CSVUtil {

    public static void trans(String jsonStr, String fileName){
        trans(jsonStr,new File(fileName));
    }

    public static void trans(String jsonStr, File file){
        /*
         *  Parse a Large JSON File and convert it to CSV
         */
        List<Map<String, String>> flatJson = JSONFlattener.parseJson(jsonStr);
        // Using ';' as separator
        Set<String> header = CSVWriter.collectOrderedHeaders(flatJson);
        // the intention is generate a csv file with specific headers - not all
        CSVWriter.writeLargeFile(flatJson, ",", file, header);
    }
}
