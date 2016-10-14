package nablarch.integration.report.outputresolver;

import java.io.File;
import java.io.IOException;

import nablarch.core.util.FilePathSetting;
import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;

/**
 * 帳票出力先解決処理のデフォルトクラス。
 * 
 * @author Naoki Tamura
 */
public class BasicReportOutputResolver implements ReportOutputResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public File getReportOutput(ReportContext ctx) {

        String reportId = ctx.getReportId();

        FilePathSetting setting = FilePathSetting.getInstance();
        File tmpPdf = null;

        try {
            tmpPdf = File.createTempFile("report_tmp", ".pdf", new File(setting
                    .getBaseDirectory("report").getPath() + "/" + reportId));
        } catch (IOException e) {
            throw new ReportException(e);
        }

        return tmpPdf;
    }

}
