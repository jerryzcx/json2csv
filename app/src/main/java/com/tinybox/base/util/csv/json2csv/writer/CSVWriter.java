/*
 * Copyright 2012-2014 Dristhi software
 * Copyright 2015 Arkni Brahim
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.tinybox.base.util.csv.json2csv.writer;


import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class CSVWriter {
    /**
     * The class logger
     */
    private static final Logger LOGGER = Logger.getLogger(CSVWriter.class.getSimpleName());

    /**
     * Convert the given List of String keys-values as a CSV String.
     *
     * @param flatJson   The List of key-value pairs generated from the JSON String
     *
     * @return The generated CSV string
     */
    public static String getCSV(List<Map<String, String>> flatJson) {
        // Use the default separator
        return getCSV(flatJson, ",");
    }

    /**
     * Convert the given List of String keys-values as a CSV String.
     *
     * @param flatJson   The List of key-value pairs generated from the JSON String
     * @param separator  The separator can be: ',', ';' or '\t'
     *
     * @return The generated CSV string
     */
    public static String getCSV(List<Map<String, String>> flatJson, String separator) {
        Set<String> headers = collectHeaders(flatJson);
        String csvString = StringUtil.join(headers, separator) + "\n";

        for (Map<String, String> map : flatJson) {
            csvString = csvString + getSeperatedColumns(headers, map, separator) + "\n";
        }

        return csvString;
    }

    /**
     * Write the given CSV string to the given file.
     *
     * @param csvString  The csv string to write into the file
     * @param fileName   The file to write (included the path)
     */
    public static void writeToFile(String csvString, String fileName) {
        try {
            write(new File(fileName), csvString);
        } catch (Exception e) {
            error("CSVWriter#writeToFile(csvString, fileName) IOException: ", e);
        }
    }

    private static void write(File file, String csvString) throws Exception {
        boolean res=FileIOUtils.writeFileFromString(file,csvString);
        if(!res){
            throw new Exception("write error");
        }else{
            error("wirte success",null);
        }
    }

    private static void write(File file, byte[] bytes) throws Exception {
        boolean res=FileIOUtils.writeFileFromBytesByStream(file,bytes);
        if(!res){
            throw new Exception("write error");
        }else{
            error("wirte success",null);
        }
    }

    private static void writeAppend(File file, byte[] bytes,boolean append) throws Exception {
        boolean res=FileIOUtils.writeFileFromBytesByStream(file,bytes,append,null);
        if(!res){
            throw new Exception("write error");
        }else{
            error("wirte success",null);
        }
    }

    private static void error(String s, Exception e) {
        if(e==null)
            LOGGER.info(s);
        else
            LOGGER.info(s+e.getLocalizedMessage());
    }


    /**
     * Write the given CSV from a flat json to the given file.
     * 
     * @param flatJson
     * @param separator
     * @param fileName 
     * @param headers
     */
//    public static void writeLargeFile(List<Map<String, String>> flatJson, String separator, String fileName, Set<String> headers){
//    	String csvString;
//        csvString = StringUtil.join(headers, separator) + "\n";
//        File file = new File(fileName);
//
//        try {
//            // ISO8859_1 char code to Latin alphabet
//            FileUtils.write(file, csvString, "ISO8859_1");
//
//            for (Map<String, String> map : flatJson) {
//            	csvString = "";
//            	csvString = getSeperatedColumns(headers, map, separator) + "\n";
//            	Files.write(Paths.get(fileName), csvString.getBytes("ISO8859_1"), StandardOpenOption.APPEND);
//            }
//        } catch (IOException e) {
//            error("CSVWriter#writeLargeFile(flatJson, separator, fileName, headers) IOException: ", e);
//        }
//    }

    public static void writeLargeFile(List<Map<String, String>> flatJson, String separator, File file, Set<String> headers){
        String csvString;
        csvString = StringUtil.join(headers, separator) + "\n";

        try {
            write(file, csvString.getBytes(StandardCharsets.UTF_8));

            for (Map<String, String> map : flatJson) {
                csvString = "";
                csvString = getSeperatedColumns(headers, map, separator) + "\n";
                writeAppend(file, csvString.getBytes(StandardCharsets.UTF_8),true);
            }
        } catch (IOException e) {
            error("CSVWriter#writeLargeFile(flatJson, separator, fileName, headers) IOException: ", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * by zj
     * @param flatJson
     * @param separator
     * @param fileName
     * @param headers
     */
    public static void writeLargeFile(List<Map<String, String>> flatJson, String separator, String fileName, Set<String> headers){
        writeLargeFile(flatJson,separator,new File(fileName),headers);
    }

    /**
     * Get separated comlumns used a separator (comma, semi column, tab).
     *
     * @param headers The CSV headers
     * @param map     Map of key-value pairs contains the header and the value
     *
     * @return a string composed of columns separated by a specific separator.
     */
    private static String getSeperatedColumns(Set<String> headers, Map<String, String> map, String separator) {
        List<String> items = new ArrayList<String>();
        for (String header : headers) {
            String value = map.get(header) == null ? "" : map.get(header).replaceAll("[\\,\\;\\r\\n\\t\\s]+", " "); 
            items.add(value);
        }

        return StringUtil.join(items, separator);
    }

    /**
     * Get the CSV header.
     *
     * @param flatJson
     *
     * @return a Set of headers
     */
    private static Set<String> collectHeaders(List<Map<String, String>> flatJson) {
        Set<String> headers = new LinkedHashSet<String>();

        for (Map<String, String> map : flatJson) {
            headers.addAll(map.keySet());
        }

        return headers;
    }

    /**
     * Get the CSV ordered header
     *
     * @param flatJson
     *
     * @return a Set of ordered headers
     */
    public static Set<String> collectOrderedHeaders(List<Map<String, String>> flatJson) {
        Set<String> headers = new TreeSet<String>();
        for (Map<String, String> map : flatJson) {
        	headers.addAll(map.keySet());
        }
        return headers;
    }    
}
