package nablarch.integration.report.templateresolver;

import java.io.File;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportParam;

/**
 * 帳票テンプレートファイルのパス解決を行うクラス。
 * 
 * @author Naoki Tamura
 */
public interface ReportTemplateResolver {

    /**
     * 帳票テンプレートファイルオブジェクトを取得します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @param param
     *            帳票パラメータ
     * @return 帳票テンプレートファイルオブジェクト
     */
    File getReportTemplate(ReportContext ctx, ReportParam param);

}
