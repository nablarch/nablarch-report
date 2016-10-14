package nablarch.integration.report.datasource;

import nablarch.core.db.statement.ResultSetIterator;

/**
 * {@link ResultSetIterator}用のデータソースクラス.
 * 
 * @author Naoki Tamura
 */
public class ResultSetIteratorDataSource extends SqlRowIteratorDataSourceSupport {

    /**
     * コンストラクタ.
     * 
     * @param rsi
     *            {@link ResultSetIterator}
     */
    public ResultSetIteratorDataSource(ResultSetIterator rsi) {
        super(rsi.iterator());
    }

}
