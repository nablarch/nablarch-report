package nablarch.integration.report.outputresolver;

import java.io.File;

import nablarch.integration.report.ReportContext;

/**
 * 帳票出力先解決クラスをあらわすインターフェース。
 * 
 * @author Naoki Tamura
 */
public interface ReportOutputResolver {

    /**
     * 帳票の出力先ファイルを返します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @return 出力先ファイルオブジェクト
     */
    File getReportOutput(ReportContext ctx);

}
