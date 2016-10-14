package nablarch.integration.report.creator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nablarch.integration.report.ReportContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.ReportParam;

/**
 * 帳票作成処理を行うデフォルトクラス。
 * 
 * @author Naoki Tamura
 */
public class BasicReportCreator extends ReportCreatorSupport {

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
    private <T> T createReport(ReportContext ctx, boolean isFile) {
        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        T report = null;

        try {

            for (ReportParam param : ctx.getReportParamList()) {
                File template = getReportTemplateResolver().getReportTemplate(
                        ctx, param);

                Map<String, Object> paramMap = paramConvert(param.getParams());

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
        }

        return report;

    }
}
