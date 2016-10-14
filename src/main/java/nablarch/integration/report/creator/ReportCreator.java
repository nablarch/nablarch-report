package nablarch.integration.report.creator;

import java.io.File;
import java.io.InputStream;

import nablarch.integration.report.ReportContext;

/**
 * 帳票作成処理をあらわすインターフェース。
 * 
 * @author Naoki Tamura
 */
public interface ReportCreator {

    /**
     * 帳票を作成します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @return 帳票ファイルオブジェクト
     */
    File createReport(ReportContext ctx);

    /**
     * 帳票のストリームオブジェクトを返します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @return 帳票ストリームオブジェクト
     */
    InputStream createReportStream(ReportContext ctx);

}
