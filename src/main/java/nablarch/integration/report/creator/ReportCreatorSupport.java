package nablarch.integration.report.creator;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import nablarch.integration.report.ReportException;
import nablarch.integration.report.exporter.ReportExporter;
import nablarch.integration.report.templateresolver.ReportTemplateResolver;

/**
 * 帳票作成処理を行う抽象クラス。
 * 
 * @author Naoki Tamura
 */
public abstract class ReportCreatorSupport implements ReportCreator {

    /** 帳票テンプレートファイル解決クラス.　 */
    private ReportTemplateResolver reportTemplateResolver;

    /** 帳票出力クラス. */
    private ReportExporter reportExporter;

    /**
     * 帳票テンプレートファイル解決クラスを取得するメソッド。
     * 
     * @return the reportTemplateResolver
     */
    protected ReportTemplateResolver getReportTemplateResolver() {
        return reportTemplateResolver;
    }

    /**
     * 帳票テンプレートファイル解決クラスを設定するメソッド。
     * 
     * @param reportTemplateResolver
     *            the reportTemplateResolver to set
     */
    public void setReportTemplateResolver(
            ReportTemplateResolver reportTemplateResolver) {
        this.reportTemplateResolver = reportTemplateResolver;
    }

    /**
     * 帳票出力クラスを取得するメソッド。
     * 
     * @return the reportExporter
     */
    protected ReportExporter getReportExporter() {
        return reportExporter;
    }

    /**
     * 帳票出力クラスを設定するメソッド。
     * 
     * @param reportExporter
     *            the reportExporter to set
     */
    public void setReportExporter(ReportExporter reportExporter) {
        this.reportExporter = reportExporter;
    }

    /**
     * 帳票パラメータの変換を行います。
     * 
     * @param param
     *            　帳票パラメータ
     * @return Mapに変換した帳票パラメータ
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> paramConvert(Object param) {

        Map<String, Object> ret = null;

        try {

            if (param instanceof Map) {
                ret = (Map<String, Object>) param;
            } else {
                ret = (Map<String, Object>) BeanUtils.describe(param);
            }

        } catch (Exception e) {
            throw new ReportException(e);
        }

        return ret;

    }
}
