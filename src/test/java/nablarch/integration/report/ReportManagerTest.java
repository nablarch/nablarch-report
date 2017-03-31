package nablarch.integration.report;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nablarch.core.ThreadContext;
import nablarch.core.db.connection.ConnectionFactory;
import nablarch.core.db.connection.TransactionManagerConnection;
import nablarch.core.db.statement.SqlResultSet;
import nablarch.core.transaction.TransactionContext;
import nablarch.core.util.DateUtil;
import nablarch.integration.report.datasource.SqlResultSetDataSource;
import nablarch.integration.report.testhelper.CompileUtil;
import nablarch.integration.report.testhelper.ReportTest1;
import nablarch.test.support.SystemRepositoryResource;
import nablarch.test.support.db.helper.DatabaseTestRunner;
import nablarch.test.support.db.helper.VariousDbTestHelper;
import net.sf.jasperreports.engine.JRException;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * {@link ReportManager}のテストクラス。
 * 
 * @author Naoki Tamura
 */
@RunWith(DatabaseTestRunner.class)
public class ReportManagerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public SystemRepositoryResource repositoryResource = new SystemRepositoryResource("nablarch/integration/report/default-definition.xml");

    private TransactionManagerConnection connection;

    @BeforeClass
    public static void beforeClass() throws Throwable {
        VariousDbTestHelper.createTable(ReportTest1.class);
        VariousDbTestHelper.setUpTable(
                new ReportTest1("1", "tttt", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS")),
                new ReportTest1("2", "dddd", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS")),
                new ReportTest1("3", "ssss", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS"))
        );

        CompileUtil.compileReportDirAll("report/ERR001");
        CompileUtil.compileReportDirAll("report/R001");
        CompileUtil.compileReportDirAll("report/R002");
    }

    @Before
    public void setUp() throws Exception {
        ThreadContext.setLanguage(Locale.JAPANESE);
        ConnectionFactory connectionFactory = repositoryResource.getComponent("connectionFactory");
        connection = connectionFactory.getConnection(TransactionContext.DEFAULT_TRANSACTION_CONTEXT_KEY);
    }

    @After
    public void tearDown() throws Exception {
        ThreadContext.clear();
        connection.terminate();
    }

    /**
     * インプットストリームから読み込みをチェックします。
     * 
     * @param in
     *            インプットストリーム
     * @return true - 読み込めた場合 / false - 読み込めなかった場合
     */
    private boolean isInputStreamSizeCheck(InputStream in) {
        try {
            in.read();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 帳票ファイルの正常出力（パラメータのみ）テスト。
     */
    @Test
    public void testReport() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("R001");
        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length(), is(greaterThan(0L)));
    }

    /**
     * 帳票ファイルの異常系テスト（パラメータのみ）テスト。
     */
    @Test
    public void testReportFailed() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("ERR001");

        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        expectedException.expect(ReportException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(JRException.class));

        ReportManager.createReport(ctx);
    }

    /**
     * 帳票ファイルの異常系テスト（パラメータのみ）テスト。パラメータのコンバートで例外処理発生。
     */
    @Test
    public void testReport_map_convert_Failed() {

        ReportContext ctx = new ReportContext("R001");

        ErrorBean eb = new ErrorBean();
        eb.setError("aaaaaddd");
        ReportParam param = new ReportParam(eb);
        ctx.addReportParam(param);

        expectedException.expect(ReportException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(InvocationTargetException.class));

        ReportManager.createReport(ctx);
    }

    public class ErrorBean{

        private String error;

        public String getError() {

            if(true) throw new RuntimeException();
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    }

    /**
     * 帳票ファイルの正常出力（フィールド）テスト。
     */
    @Test
    public void testReport_list() throws Exception{

        SqlResultSet resultSet = connection.prepareStatement("SELECT * FROM REPORT_TEST_1").retrieve();

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(resultSet));

        List<ReportParam> list = new ArrayList<ReportParam>();
        list.add(param);
        ReportContext ctx = new ReportContext("R002", list);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length(), is(greaterThan(0L)));
    }

    /**
     * 帳票ファイルの正常出力（フィールド）テスト。
     */
    @Test
    public void testReport_list_locale() {

        SqlResultSet resultSet = connection.prepareStatement("SELECT * FROM REPORT_TEST_1").retrieve();

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(resultSet));

        List<ReportParam> list = new ArrayList<ReportParam>();
        list.add(param);
        ReportContext ctx = new ReportContext("R002", list, Locale.US);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length(), is(greaterThan(0L)));
    }

    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト。パラメータのみ。
     */
    @Test
    public void testReport_creator() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("virtualizerReportCreator","R001");

        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length(), is(greaterThan(0L)));
    }

    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト（異常系）。パラメータのみ。
     */
    @Test
    public void testReport_creator_Faild() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "ERR001");

        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        expectedException.expect(ReportException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(JRException.class));

        ReportManager.createReport(ctx);

    }

    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト。フィールド指定。
     */
    @Test
    public void testReport_creator_list() {

        SqlResultSet resultSet = connection.prepareStatement("SELECT * FROM REPORT_TEST_1").retrieve();

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(resultSet));
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length(), is(greaterThan(0L)));
    }

    /**
     * 帳票オブジェクトストリームの正常出力（パラメータのみ）テスト。
     */
    @Test
    public void testReportStream() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        InputStream report = ReportManager.createReportStream(ctx);

        assertThat("帳票ストリームが取得出来ていること", isInputStreamSizeCheck(report),
                is(true));
    }

    /**
     * 帳票オブジェクトストリームの正常出力（フィールド）テスト。
     */
    @Test
    public void testReportStream_list() {

        SqlResultSet resultSet = connection.prepareStatement("SELECT * FROM REPORT_TEST_1").retrieve();

        ReportContext ctx = new ReportContext("R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(resultSet));
        ctx.addReportParam(param);

        InputStream report = ReportManager.createReportStream(ctx);

        assertThat("帳票ストリームが取得出来ていること", isInputStreamSizeCheck(report),
                is(true));
    }

    /**
     * 帳票作成コンポーネントにデフォルト以外を指定。帳票オブジェクトストリームの正常出力（パラメータのみ）テスト。
     */
    @Test
    public void testReportStream_creator() {

        ReportTest1 entity = VariousDbTestHelper.findById(ReportTest1.class, "1");

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R001");

        ReportParam param = new ReportParam(entity);
        ctx.addReportParam(param);

        InputStream report = ReportManager.createReportStream(ctx);

        assertThat("帳票ストリームが取得出来ていること", isInputStreamSizeCheck(report),
                is(true));
    }

    /**
     * 帳票作成コンポーネントにデフォルト以外を指定。帳票オブジェクトストリームの正常出力（フィールド）テスト。
     */
    @Test
    public void testReportStream_creator_list() {

        SqlResultSet resultSet = connection.prepareStatement("SELECT * FROM REPORT_TEST_1").retrieve();

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(resultSet));
        ctx.addReportParam(param);

        InputStream report = ReportManager.createReportStream(ctx);

        assertThat("帳票ストリームが取得出来ていること", isInputStreamSizeCheck(report),
                is(true));
    }

}
