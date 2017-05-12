/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author viegas
 */
public class Topologies_BATCH_No_Update {

    public static class FilesByDateDTO {

        public Date date;
        public String train;
        public ArrayList<String> files;
    }

    public static void findFilesPath(File[] files, String featureSetPrefix, String directory, ArrayList<FilesByDateDTO> filesByDate,
            int depth) {

        if (depth == 3) {
            FilesByDateDTO fileByDate = new FilesByDateDTO();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.valueOf(directory.substring(directory.length() - 30, directory.length() - 26)));
            c.set(Calendar.MONTH, Integer.valueOf(directory.substring(directory.length() - 26, directory.length() - 24)) - 1);
            c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(directory.substring(directory.length() - 24, directory.length() - 22)));
            fileByDate.date = c.getTime();
            fileByDate.files = new ArrayList<>();

            for (File file : files) {
                if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(file.getName(), "train")
                        && org.apache.commons.lang3.StringUtils.containsIgnoreCase(file.getName(), featureSetPrefix)) {
                    fileByDate.train = file.getAbsolutePath();
                } else {
                    if (file.getName().contains(featureSetPrefix)) {
                        fileByDate.files.add(file.getAbsolutePath());
                    }
                }
            }
            filesByDate.add(fileByDate);
        }

        for (File file : files) {
            if (file.isDirectory()) {
                try {
                    System.out.println("Directory: " + file.getAbsolutePath());
                    if (depth == 0 && Integer.valueOf(file.getName()) >= 2000 && Integer.valueOf(file.getName()) < 2020) {
                        findFilesPath(file.listFiles(), featureSetPrefix, file.getAbsolutePath(), filesByDate, depth + 1);
                    } else if (depth > 0 && depth < 3) {
                        findFilesPath(file.listFiles(), featureSetPrefix, file.getAbsolutePath(), filesByDate, depth + 1);
                    }
                } catch (NumberFormatException ex) {

                }
            }
        }

    }

    public static void runTopology(
            String path,
            String featureSetPrefix)
            throws Exception {

        File[] files = new File(path).listFiles();
        ArrayList<FilesByDateDTO> filesByDate = new ArrayList();

        Topologies_BATCH_No_Update.findFilesPath(files, featureSetPrefix, "", filesByDate, 0);

        
        Collections.sort(filesByDate, new Comparator<FilesByDateDTO>() {
            @Override
            public int compare(FilesByDateDTO o1, FilesByDateDTO o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        
        for (FilesByDateDTO filesBy : filesByDate) {
            System.out.println("Train: " + filesBy.train + " date: " + filesBy.date);
            for (String s : filesBy.files) {
                System.out.println("\t" + s);
            }
        }
        
        System.out.println("Found " + filesByDate.size() + " days of test.");

    }

}
