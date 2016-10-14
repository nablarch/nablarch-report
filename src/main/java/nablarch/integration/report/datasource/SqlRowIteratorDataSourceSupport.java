package nablarch.integration.report.datasource;

import java.util.Iterator;

import nablarch.core.db.statement.SqlRow;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * {@link SqlRow}用データソースの抽象クラス.
 * 
 * @author Naoki Tamura
 */
public abstract class SqlRowIteratorDataSourceSupport implements JRDataSource {

    /** 簡易取得結果の1行データを保持するオブジェクトのイテレータ */
    private final Iterator<SqlRow> ite;

    /** 簡易取得結果の1行データを保持するクラス */
    private SqlRow row;

    /**
     * コンストラクタ
     * 
     * @param s SqlRowイテレータ
     */
    protected SqlRowIteratorDataSourceSupport(Iterator<SqlRow> s) {
        this.ite = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getFieldValue(JRField fld) throws JRException {
        return row.get(fld.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean next() throws JRException {
        this.row = ite.hasNext() ? ite.next() : null;
        return this.row != null;
    }

}
