package nablarch.integration.report;

/**
 * 帳票ライブラリ用の例外クラス。
 * 
 * @author Naoki Tamura
 */
public class ReportException extends RuntimeException {

    /**
     * シリアル
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     * 
     * @param cause
     *            例外
     */
    public ReportException(Throwable cause) {
        super(cause);
    }
}
