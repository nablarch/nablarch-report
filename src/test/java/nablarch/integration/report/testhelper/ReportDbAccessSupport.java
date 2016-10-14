package nablarch.integration.report.testhelper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.statement.ParameterizedSqlPStatement;
import nablarch.core.db.statement.ResultSetIterator;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.db.statement.SqlResultSet;
import nablarch.core.db.transaction.SimpleDbTransactionExecutor;
import nablarch.core.db.transaction.SimpleDbTransactionManager;
import nablarch.core.transaction.TransactionContext;

/**
 * 帳票APIテスト用データアクセスユーティリティクラス。
 * 
 * @author Naoki Tamura
 */
public class ReportDbAccessSupport {

    /** トランザクションマネージャ */
    private final SimpleDbTransactionManager transactionManager;

    /**
     * コンストラクタ
     * 
     * @param transactionManager トランザクションマネージャ
     */
    public ReportDbAccessSupport(SimpleDbTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * テーブルをクリーニングする。
     */
    public void cleanupAll() {
        cleanup("REPORT_TEST_1");
    }

    /**
     * 指定されたテーブルのデータを削除する。
     * 
     * @param tableName
     *            テーブル名
     */
    public void cleanup(final String... tableName) {
        new SimpleDbTransactionExecutor<Void>(transactionManager) {
            @Override
            public Void execute(AppDbConnection connection) {
                for (String name : tableName) {
                    SqlPStatement statement = connection
                            .prepareStatement("delete from " + name);
                    statement.executeUpdate();
                }
                return null;
            }
        }.doTransaction();
    }

    /**
     * レポート用テストテーブル１にデータを登録する。
     */
    public void insertReportTest1Entity(final ReportTest1... entities) {
        new SimpleDbTransactionExecutor<Void>(transactionManager) {
            @Override
            public Void execute(AppDbConnection connection) {
                SqlPStatement statement = connection
                        .prepareStatement("INSERT INTO REPORT_TEST_1 "
                                + " (COL1,COL2,COL3,COL4,COL5,COL6) "
                                + "VALUES (?, ?, ?, ?, ?, ?)");
                for (ReportTest1 entity : entities) {
                    statement.setString(1, entity.getCol1());
                    statement.setString(2, entity.getCol2());
                    statement.setInt(3, entity.getCol3());
                    statement.setBigDecimal(4, entity.getCol4());
                    statement.setDate(5, entity.getCol5());
                    statement.setTimestamp(6, entity.getCol6());
                    statement.addBatch();
                }
                statement.executeBatch();
                return null;
            }
        }.doTransaction();
    }

    public SqlResultSet executeSql(String sql, Object cond) {

        AppDbConnection conn = DbConnectionContext
                .getConnection(TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY);

        SqlResultSet rst = null;

        if (cond == null) {
            SqlPStatement statement = conn.prepareStatement(sql);
            rst = statement.retrieve();
        } else {
            ParameterizedSqlPStatement statement = conn
                    .prepareParameterizedSqlStatement(sql, cond);
            rst = statement.retrieve(cond);
        }

        return rst;
    }

    public ResultSetIterator executeQuery(String sql, Object cond) {

        AppDbConnection conn = DbConnectionContext
                .getConnection(TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY);

        ResultSetIterator rst = null;

        if (cond == null) {
            SqlPStatement statement = conn.prepareStatement(sql);
            rst = statement.executeQuery();
        } else {
            ParameterizedSqlPStatement statement = conn
                    .prepareParameterizedSqlStatement(sql, cond);
            rst = statement.executeQueryByObject(cond);
        }

        return rst;
    }

    public ReportTest1 createData(String col1, String col2, Integer col3,
            BigDecimal col4, String col5, String col6) {

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat formatTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        java.util.Date date = null;
        Timestamp ts = null;
        try {
            date = format.parse(col5);
            ts = new Timestamp(formatTs.parse(col6).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ReportTest1 d = new ReportTest1();
        d.setCol1(col1);
        d.setCol2(col2);
        d.setCol3(col3);
        d.setCol4(col4);
        d.setCol5(new Date(date.getTime()));
        d.setCol6(ts);
        return d;
    }

}
