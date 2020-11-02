package com.estsoft.pilot;

import com.estsoft.pilot.app.util.ZipUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ZipUtilsTest {

    @Test
    public void zipFilesTest() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\mgjeong\\Downloads\\src_sample1.zip");
        File file1 = new File("D:\\data\\stor\\files\\D14\\8g\\L7c\\AQ\\Cgo\\Hfc.o");
        File file2 = new File("D:\\data\\stor\\files\\D14\\8g\\L7c\\AQ\\\\F5s\\Hfg.win");

        List<File> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);
        ZipUtils.zipFiles(files, fos);
    }

    @Test
    public void unZipFilesTest() throws IOException {
        File zipFile = new File("C:/Project_Work/samples/src_sample1.zip");
        File unZipOutputFolder = new File("C:/Project_Work/samples/dest_folder");
    }
}
