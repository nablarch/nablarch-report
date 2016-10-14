package nablarch.integration.report.exporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.outputresolver.ReportOutputResolver;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * 帳票出力処理のデフォルトクラス。
 * 
 * @author Naoki Tamura
 */
public class BasicReportExporter implements ReportExporter {

    /** 帳票出力先解決クラス */
    private ReportOutputResolver reportOutputResolver;

    /**
     * 帳票出力先解決クラスを設定するメソッド。
     * 
     * @param reportOutputResolver
     *            帳票出力先解決クラス
     */
    public void setReportOutputResolver(
            ReportOutputResolver reportOutputResolver) {
        this.reportOutputResolver = reportOutputResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File export(ReportContext ctx, List<JasperPrint> jasperPrintList) {

        File report = reportOutputResolver.getReportOutput(ctx);

        doExporter(ctx, new SimpleOutputStreamExporterOutput(report),
                jasperPrintList);

        return report;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream exportStream(ReportContext ctx,
            List<JasperPrint> jasperPrintList) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        doExporter(ctx, new SimpleOutputStreamExporterOutput(os),
                jasperPrintList);

        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * 帳票エクスポーターを生成します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @param out
     *            出力先
     * @param jasperPrintList
     *            帳票プリントオブジェクト
     * @return PDFエクスポーター
     */
    protected JRPdfExporter doExporter(ReportContext ctx,
            OutputStreamExporterOutput out, List<JasperPrint> jasperPrintList) {

        JRPdfExporter exporter;

        try {
            exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput
                    .getInstance(jasperPrintList));
            exporter.setExporterOutput(out);

            exporter.exportReport();

        } catch (JRException e) {
            throw new ReportException(e);
        }

        return exporter;

    }
}
