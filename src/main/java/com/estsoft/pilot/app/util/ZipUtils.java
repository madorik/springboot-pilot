package com.estsoft.pilot.app.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * Zips a collection of files to a destination zip output stream.
     *
     * @param files        A collection of files and directories
     * @param outputStream The output stream of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zipFiles(List<File> files, OutputStream outputStream) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        for (File file : files) {
            if (file.isDirectory()) {   //if it's a folder
                addFolderToZip("", file, zos);
            } else {
                addFileToZip("", file, zos);
            }
        }
        zos.finish();
    }

    /**
     * Adds a directory to the current zip
     *
     * @param path   the path of the parent folder in the zip
     * @param folder the directory to be  added
     * @param zos    the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFolderToZip(String path, File folder, ZipOutputStream zos) throws IOException {
        String currentPath = !StringUtils.isEmpty(path)? path + "/" + folder.getName(): folder.getName();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToZip(currentPath, file, zos);
            } else {
                addFileToZip(currentPath, file, zos);
            }
        }
    }

    /**
     * Adds a file to the current zip output stream
     *
     * @param path the path of the parent folder in the zip
     * @param file the file to be added
     * @param zos  the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void addFileToZip(String path, File file, ZipOutputStream zos) throws IOException {
        String currentPath = !StringUtils.isEmpty(path)? path + "/" + file.getName(): file.getName();
        zos.putNextEntry(new ZipEntry(currentPath));
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            IOUtils.copy(is, zos);
        } finally {
            IOUtils.closeQuietly(is);
        }
        zos.closeEntry();
    }
    /**
     * Zips a collection of files to a destination zip file.
     *
     * @param files   A collection of files and directories
     * @param zipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zipFiles(List<File> files, File zipFile) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipFile));
        try {
            zipFiles(files, os);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
