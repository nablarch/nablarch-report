package nablarch.integration.report.creator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportParam;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import nablarch.integration.report.ReportException;

/**
 * 大容量データ用帳票作成処理クラス。
 * 
 * @author Naoki Tamura
 */
public class VirtualizerReportCreator extends ReportCreatorSupport {

    /**
     * {@inheritDoc}
     */
    @Override
    public File createReport(ReportContext ctx) {
        return createReport(ctx, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream createReportStream(ReportContext ctx) {
        return createReport(ctx, false);
    }

    /**
     * 帳票生成共通処理。
     * 
     * @param <T> 出力ファイルの型
     * @param ctx 帳票コンテキススト
     * @param isFile 出力形式がファイルかどうかをあわらすフラグ
     * @return 帳票出力
     */
    @SuppressWarnings("unchecked")
    public <T> T createReport(ReportContext ctx, boolean isFile) {

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

        JRFileVirtualizer virtualizer = new JRFileVirtualizer(2,
                System.getProperty("java.io.tmpdir"));

        T report = null;

        try {

            for (ReportParam param : ctx.getReportParamList()) {
                File template = getReportTemplateResolver().getReportTemplate(
                        ctx, param);

                Map<String, Object> paramMap = paramConvert(param.getParams());
                paramMap.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

                JasperPrint print = JasperFillManager.fillReport(
                        template.getAbsolutePath(), paramMap,
                        param.getDataSource());

                jasperPrintList.add(print);

            }

            report = (T) (isFile ? getReportExporter().export(ctx,
                    jasperPrintList) : getReportExporter().exportStream(ctx,
                    jasperPrintList));

        } catch (JRException e) {
            throw new ReportException(e);
        } finally {
            virtualizer.cleanup();
        }

        return report;
    }

}
