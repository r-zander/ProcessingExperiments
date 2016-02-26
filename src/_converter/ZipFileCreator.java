package _converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileCreator {

    private SketchFile  sketchFile;

    private Set<String> imports;

    public ZipFileCreator(SketchFile sketchFile, Set<String> importedFiles) {
        this.sketchFile = sketchFile;
        this.imports = importedFiles;
    }

    public static void createZip(SketchFile sketchFile, Set<String> importedFiles) {
        new ZipFileCreator(sketchFile, importedFiles).createZip();
    }

    private void createZip() {
        try {
            FileOutputStream fos =
                    new FileOutputStream(new File(sketchFile.getSketchDirectory(), sketchFile.getSketchName() + ".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);

            String sketchPath = sketchFile.getSketchName() + File.separator;

            addToZipFile(sketchFile, sketchPath + sketchFile.getName(), zos);

            for (String importFile : imports) {
                addToZipFile(new File(sketchFile.getSketchDirectory(), importFile), sketchPath + importFile, zos);
            }

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToZipFile(File file, String fileNameInZip, ZipOutputStream zos) throws FileNotFoundException,
            IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileNameInZip);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
