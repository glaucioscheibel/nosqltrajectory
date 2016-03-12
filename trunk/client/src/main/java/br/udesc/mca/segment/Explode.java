package br.udesc.mca.segment;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

public class Explode {

    public static void main(String[] args) throws Exception {
        File dir = new File("C:/busrj");
        File data = new File("C:/Users/Glaucio/OneDrive/trajecDataSets/busrj");
        String[] ext = {"7z"};
        Iterator<File> ifs = FileUtils.iterateFiles(data, ext, true);
        while (ifs.hasNext()) {
            File f = ifs.next();
            ProcessBuilder pb = new ProcessBuilder("7z", "x", f.getAbsolutePath());
            pb.directory(dir);
            pb.inheritIO();
            System.out.println(pb.command());
            Process p = pb.start();
            p.waitFor();
        }
    }
}
