package nablarch.integration.report.datasource;

import nablarch.core.db.statement.SqlResultSet;

/**
 * {@link SqlResultSet}用のデータソースクラス.
 * 
 * @author Naoki Tamura
 */
public class SqlResultSetDataSource extends SqlRowIteratorDataSourceSupport {

    /**
     * コンストラクタ.
     * 
     * @param rst
     *            {@link SqlResultSet}
     */
    public SqlResultSetDataSource(SqlResultSet rst) {
        super(rst.iterator());
    }

}
