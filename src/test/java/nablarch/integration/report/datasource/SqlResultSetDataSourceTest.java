package nablarch.integration.report.datasource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;

import nablarch.core.db.connection.ConnectionFactory;
import nablarch.core.db.connection.TransactionManagerConnection;
import nablarch.core.db.statement.SqlResultSet;
import nablarch.core.transaction.TransactionContext;
import nablarch.core.util.DateUtil;
import nablarch.integration.report.testhelper.ReportTest1;
import nablarch.test.support.SystemRepositoryResource;
import nablarch.test.support.db.helper.DatabaseTestRunner;
import nablarch.test.support.db.helper.VariousDbTestHelper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link SqlResultSetDataSource} のテストクラス
 * 
 * @author Naoki Tamura
 */
@RunWith(DatabaseTestRunner.class)
public class SqlResultSetDataSourceTest {

    @Rule
    public SystemRepositoryResource repositoryResource = new SystemRepositoryResource("db-default.xml");

    private TransactionManagerConnection connection;

    @BeforeClass
    public static void beforeClass() throws Throwable {
        VariousDbTestHelper.createTable(ReportTest1.class);
        VariousDbTestHelper.setUpTable(
                new ReportTest1("1", "tttt", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS")),
                new ReportTest1("2", "dddd", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS")),
                new ReportTest1("3", "ssss", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS"))
        );
    }

    @Before
    public void setUp() throws Exception {
        ConnectionFactory connectionFactory = repositoryResource.getComponent("connectionFactory");
        connection = connectionFactory.getConnection(TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY);
    }

    @After
    public void tearDown() throws Exception {
        connection.terminate();
    }

    /**
     * {@link SqlResultSetDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)}
     * のテスト。
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldValue() throws Exception {
        SqlResultSet s = connection.prepareStatement("SELECT * FROM REPORT_TEST_1 ORDER BY COL1").retrieve();
        JRDataSource ds = new SqlResultSetDataSource(s);

        JRDesignField fld = new JRDesignField();
        fld.setName("col1");
        fld.setValueClass(String.class);
        fld.setValueClassName("java.lang.String");

        ds.next();
        Object ret = ds.getFieldValue(fld);

        assertThat("型のテスト", ret instanceof String, is(true));
        assertThat("値のテスト", ret.toString(), is("1"));

        ds.next();
        ret = ds.getFieldValue(fld);

        assertThat("型のテスト", ret instanceof String, is(true));
        assertThat("値のテスト", ret.toString(), is("2"));

    }

    /**
     * {@link SqlResultSetDataSource#next()}のテスト。
     *
     * @throws Exception
     */
    @Test
    public void testNext() throws Exception {
        SqlResultSet s = connection.prepareStatement("SELECT * FROM REPORT_TEST_1 ORDER BY COL1").retrieve();
        JRDataSource ds = new SqlResultSetDataSource(s);

        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＮＧ", ds.next(), is(false));
    }

}
