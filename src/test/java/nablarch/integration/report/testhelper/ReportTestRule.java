package nablarch.integration.report.testhelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nablarch.core.ThreadContext;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.ConnectionFactory;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.connection.TransactionManagerConnection;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.db.statement.exception.SqlStatementException;
import nablarch.core.db.transaction.SimpleDbTransactionExecutor;
import nablarch.core.db.transaction.SimpleDbTransactionManager;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.ComponentDefinitionLoader;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.transaction.TransactionContext;
import nablarch.core.util.FileUtil;

import org.junit.rules.ExternalResource;

public class ReportTestRule extends ExternalResource {

    /** DDLパス */
    private static final String DDL_PATH = "nablarch/integration/report/forward_with_drop.sql";

    public void callBefore() throws Throwable {
        this.before();
    }

    public void callAfter() throws Throwable {
        this.after();
    }

    @Override
    protected void before() throws Throwable {
        // 念のためリポジトリ情報は事前にクリアする。
        SystemRepository.clear();

        // デフォルトロケール指定
        ThreadContext.setLanguage(Locale.JAPAN);

        ComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/integration/report/default-definition.xml");
        SystemRepository.load(new DiContainer(loader));

        // 業務トランザクション用コネクションの登録
        ConnectionFactory connectionFactory = SystemRepository
                .get("databaseConnectionFactory");
        TransactionManagerConnection connection = connectionFactory
                .getConnection(TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY);
        DbConnectionContext.setConnection(connection);

        // テスト用テーブル作成
        createReportTestTable(loadSql());
    }

    @Override
    protected void after() {
        try {
            TransactionManagerConnection connection = (TransactionManagerConnection) DbConnectionContext
                    .getConnection();
            connection.rollback();
            connection.terminate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SystemRepository.clear();
        DbConnectionContext.removeConnection();
    }

    /**
     * 帳票用のテーブルを作成する。
     * 
     * @param sqlHolder
     *            実行対象のSQL文
     */
    public void createReportTestTable(final SqlHolder sqlHolder) {

        SimpleDbTransactionManager transactionManager = getTransactionManager();
        new SimpleDbTransactionExecutor<Void>(transactionManager) {
            @Override
            public Void execute(AppDbConnection connection) {
                for (String sql : sqlHolder.dropList) {
                    SqlPStatement statement = connection.prepareStatement(sql);
                    try {
                        statement.execute();
                    } catch (SqlStatementException ignored) {
                        ignored.printStackTrace();
                    }
                }
                for (String sql : sqlHolder.createList) {
                    SqlPStatement statement = connection.prepareStatement(sql);
                    statement.execute();
                }
                return null;
            }

        }.doTransaction();
    }

    /**
     * データベーストランザクションを取得する。
     * 
     * @return トランザクションオブジェクト
     */
    public SimpleDbTransactionManager getTransactionManager() {
        return SystemRepository.get("tran");
    }

    /**
     * SQLファイルからSQL文をロードする。
     */
    private SqlHolder loadSql() throws Exception {
        InputStream resource = FileUtil.getClasspathResource(DDL_PATH);
        if (resource == null) {
            throw new IllegalStateException(
                    "forward_with_drop.sql was not found.");
        }

        SqlHolder result = new SqlHolder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                resource, "UTF-8"));
        try {
            StringBuilder sql = new StringBuilder();
            SqlHolder.SqlType type = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.equals("/") && sql.length() != 0) {
                    switch (type) {
                    case drop:
                        result.dropList.add(sql.toString());
                        break;
                    case create:
                        result.createList.add(sql.toString());
                        break;
                    case alter:
                        result.alterList.add(sql.toString());
                        break;
                    }
                    sql.setLength(0);
                    continue;
                }
                if (sql.length() == 0) {
                    if (line.contains("DROP ")) {
                        line = line.substring(line.indexOf("DROP "));
                        type = SqlHolder.SqlType.drop;
                    } else if (line.contains("CREATE ")) {
                        line = line.substring(line.indexOf("CREATE "));
                        type = SqlHolder.SqlType.create;
                    } else if (line.contains("ALTER ")) {
                        line = line.substring(line.indexOf("ALTER "));
                        type = SqlHolder.SqlType.alter;
                    }
                }
                sql.append(line);
                sql.append(' ');
            }
        } finally {
            FileUtil.closeQuietly(reader);
        }
        return result;
    }

    private static class SqlHolder {

        enum SqlType {
            drop, create, alter
        }

        private List<String> dropList = new ArrayList<String>();

        private List<String> createList = new ArrayList<String>();

        private List<String> alterList = new ArrayList<String>();
    }
}
