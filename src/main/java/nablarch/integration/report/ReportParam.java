package nablarch.integration.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 * 帳票パラメータ。
 * 
 * @author Naoki Tamura
 */
public class ReportParam {

    /**
     * 帳票テンプレートのベース名。
     * <p/>
     * 帳票テンプレートファイルのコンパイル後のオブジェクト名が、test.jasperなら「test」を設定する。
     */
    private final String templateName;

    /** パラメータ用オブジェクト */
    private final Object params;

    /** 繰返データ用データソースオブジェクト */
    private final JRDataSource dataSource;

    /**
     * コンストラクタ。
     * 
     * @param params
     *            パラメータ
     */
    public ReportParam(Object params) {
        this.templateName = null;
        this.params = params;
        this.dataSource = new JREmptyDataSource();
    }

    /**
     * コンストラクタ。
     * 
     * @param params
     *            パラメータ
     * @param dataSource
     *            データソース
     */
    public ReportParam(Object params, JRDataSource dataSource) {
        this.templateName = null;
        this.params = params;
        this.dataSource = dataSource;
    }

    /**
     * コンストラクタ。
     * 
     * @param templateName
     *            テンプレート名
     * @param params
     *            パラメータ
     * @param dataSource
     *            データソース
     */
    public ReportParam(String templateName, Object params,
            JRDataSource dataSource) {
        this.templateName = templateName;
        this.params = params;
        this.dataSource = dataSource;
    }

    /**
     * コンストラクタ。
     * 
     * @param templateName
     *            テンプレート名
     * @param params
     *            パラメータ
     */
    public ReportParam(String templateName, Object params) {
        this.templateName = templateName;
        this.params = params;
        this.dataSource = new JREmptyDataSource();
    }

    /**
     * 帳票テンプレート名取得メソッド。
     * 
     * @return 帳票テンプレート名
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * パラメータ用オブジェクト取得メソッド。
     * 
     * @return パラメータ用オブジェクト
     */
    public Object getParams() {
        return params;
    }

    /**
     * 繰返データ用データソースオブジェクト取得メソッド。
     * 
     * @return 繰返データ用データソースオブジェクト
     */
    public JRDataSource getDataSource() {
        return dataSource;
    }

}
