package nablarch.integration.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 帳票コンテキスト。
 * 
 * @author Naoki Tamura
 */
public class ReportContext {

    /** 帳票作成処理クラス登録名 */
    private final String creatorKey;

    /** 帳票ID */
    private final String reportId;

    /** 帳票パラメータリスト */
    private final List<ReportParam> reportParamList;

    /** Locale */
    private final Locale locale;

    /**
     * コンストラクタ。
     * 
     * @param reportId
     *            帳票ＩＤ
     */
    public ReportContext(String reportId) {
        this(null, reportId, new ArrayList<ReportParam>(), null);
    }

    /**
     * コンストラクタ。
     * 
     * @param reportId
     *            帳票ＩＤ
     * @param locale
     *            ロケール
     */
    public ReportContext(String reportId, Locale locale) {
        this(null, reportId, new ArrayList<ReportParam>(), locale);
    }
    
    /**
     * コンストラクタ。
     * 
     * @param creatorKey 帳票生成処理キー
     * @param reportId 帳票ＩＤ
     */
    public ReportContext(String creatorKey, String reportId) {
        this(creatorKey, reportId, new ArrayList<ReportParam>(), null);
    }

    /**
     * コンストラクタ。
     * 
     * @param reportId
     *            帳票ＩＤ
     * @param reportParamList
     *            帳票パラメータリスト
     */
    public ReportContext(String reportId, List<ReportParam> reportParamList) {
        this(null, reportId, reportParamList, null);
    }

    /**
     * コンストラクタ。
     * 
     * @param reportId
     *            帳票ＩＤ
     * @param reportParamList
     *            帳票パラメータリスト
     * @param locale
     *            ロケール
     */
    public ReportContext(String reportId, List<ReportParam> reportParamList,
            Locale locale) {
        this(null, reportId, reportParamList, locale);
    }

    /**
     * コンストラクタ。
     * 
     * @param creatorKey
     *            帳票生成処理キー
     * @param reportId
     *            帳票ＩＤ
     * @param reportParamList
     *            帳票パラメータリスト
     * @param locale
     *            ロケール
     */
    public ReportContext(String creatorKey, String reportId,
            List<ReportParam> reportParamList, Locale locale) {
        this.creatorKey = creatorKey;
        this.reportId = reportId;
        this.reportParamList = reportParamList;
        this.locale = locale;
    }

    /**
     * 帳票IDを取得します。
     * 
     * @return 帳票ID
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * 帳票パラメータリストを取得します。
     * 
     * @return 帳票パラメータリスト
     */
    public List<ReportParam> getReportParamList() {
        return reportParamList;
    }

    /**
     * 帳票パラメータリストを追加します。
     * 
     * @param reportParam
     *            帳票パラメータ
     */
    public void addReportParam(ReportParam reportParam) {
        this.reportParamList.add(reportParam);
    }

    /**
     * 帳票作成処理クラス登録名を取得します。
     * 
     * @return 帳票作成処理クラス登録名
     */
    public String getCreatorKey() {
        return creatorKey;
    }

    /**
     * ロケールを取得します。
     * 
     * @return ロケール
     */
    public Locale getLocale() {
        return locale;
    }

}
