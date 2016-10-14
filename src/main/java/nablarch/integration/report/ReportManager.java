package nablarch.integration.report;

import java.io.File;
import java.io.InputStream;

import nablarch.core.repository.SystemRepository;
import nablarch.integration.report.creator.ReportCreator;

/**
 * 帳票処理用の管理クラス。
 * 
 * @author Naoki Tamura
 */
public final class ReportManager {

    /** プライベートコンストラクタ */
    private ReportManager() {
    }

    /**
     * 帳票を出力します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @return 帳票ファイルオブジェクト
     */
    public static File createReport(ReportContext ctx) {
        return getReportCreator(ctx.getCreatorKey()).createReport(ctx);
    }

    /**
     * 帳票オブジェクトのストリームを返します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @return 帳票オブジェクトのストリーム
     */
    public static InputStream createReportStream(ReportContext ctx) {
        return getReportCreator(ctx.getCreatorKey()).createReportStream(ctx);
    }

    /**
     * システムリポジトリより帳票作成処理クラスを取得します。
     * 
     * @param key
     *            帳票作成処理の登録名
     * @return 帳票作成処理
     */
    private static ReportCreator getReportCreator(String key) {
        ReportCreator c = (ReportCreator) SystemRepository.get(key != null ? key : "reportCreator");
        return c;
    }
}
