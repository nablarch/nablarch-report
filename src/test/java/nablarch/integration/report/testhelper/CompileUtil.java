package nablarch.integration.report.testhelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class CompileUtil {

    public static void compileReportDirAll(String dir) {

        String path = CompileUtil.class.getClassLoader().getResource(dir)
                .getPath();

        List<File> jrxmlList = readJrxml(new File(path));
        for (File jrxml : jrxmlList) {
            compileToFile(jrxml);
        }

    }

    public static void compileToFile(File file) {

        InputStream jrxmlInput = null;

        try {

            jrxmlInput = JRLoader.getFileInputStream(file.getAbsolutePath());
            JasperDesign design;

            design = JRXmlLoader.load(jrxmlInput);
            String distFile = file.getAbsolutePath().replaceAll("\\.jrxml$",
                    ".jasper");
            JasperCompileManager.compileReportToFile(design, distFile);
        } catch (JRException e) {
            e.printStackTrace();

        } finally {
            try {
                jrxmlInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static List<File> readJrxml(File dir) {

        List<File> list = new ArrayList<File>();

        File[] files = dir.listFiles();
        if (files == null)
            return new ArrayList<File>();
        for (File file : files) {
            if (!file.exists())
                continue;
            else if (file.isDirectory())
                list.addAll(readJrxml(file));
            else if (file.isFile() && file.getName().endsWith(".jrxml"))
                list.add(file);
        }

        return list;
    }

}
