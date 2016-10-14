package nablarch.integration.report;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nablarch.core.db.statement.SqlResultSet;
import net.sf.jasperreports.engine.JRException;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import nablarch.integration.report.datasource.SqlResultSetDataSource;
import nablarch.integration.report.testhelper.CompileUtil;
import nablarch.integration.report.testhelper.ReportDbAccessSupport;
import nablarch.integration.report.testhelper.ReportTest1;
import nablarch.integration.report.testhelper.ReportTestRule;

/**
 * {@link ReportManager}のテストクラス。
 * 
 * @author Naoki Tamura
 */
public class ReportManagerTest {

    @ClassRule
    public static ReportTestRule r = new ReportTestRule();;

    public static ReportDbAccessSupport db;

    @BeforeClass
    public static void beforeClass() throws Throwable {

        db = new ReportDbAccessSupport(r.getTransactionManager());

        List<ReportTest1> list = new ArrayList<ReportTest1>();
        list.add(db.createData("1", "tttt", 999, new BigDecimal("999.99"),
                "1999/09/01", "1999-09-01 12:30:11.123"));
        list.add(db.createData("2", "dddd", 999, new BigDecimal("999.99"),
                "1999/09/01", "1999-09-01 12:30:11.123"));
        list.add(db.createData("3", "ssss", 999, new BigDecimal("999.99"),
                "1999/09/01", "1999-09-01 12:30:11.123"));
        db.insertReportTest1Entity((ReportTest1[]) list
                .toArray(new ReportTest1[0]));

        CompileUtil.compileReportDirAll("report/ERR001");
        CompileUtil.compileReportDirAll("report/R001");
        CompileUtil.compileReportDirAll("report/R002");
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

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length() > 0, is(true));
    }

    /**
     * 帳票ファイルの異常系テスト（パラメータのみ）テスト。
     */
    @Test
    public void testReportFailed() {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("ERR001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        try {
            ReportManager.createReport(ctx);
            fail("JRExceptionが発生し、リスローされる");
        } catch (ReportException e) {
            assertThat("元例外の確認", e.getCause(),
                    is(instanceOf(JRException.class)));
        }
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

        try {
            ReportManager.createReport(ctx);
            fail("JRExceptionが発生し、リスローされる");
        } catch (ReportException e) {
            assertThat("元例外の確認", e.getCause(),
                    is(instanceOf(InvocationTargetException.class)));
        }
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
    public void testReport_list() {

        SqlResultSet s = db.executeSql("SELECT * FROM REPORT_TEST_1", null);

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(s));

        List<ReportParam> list = new ArrayList<ReportParam>();
        list.add(param);
        ReportContext ctx = new ReportContext("R002", list);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length() > 0, is(true));
    }

    /**
     * 帳票ファイルの正常出力（フィールド）テスト。
     */
    @Test
    public void testReport_list_locale() {

        SqlResultSet s = db.executeSql("SELECT * FROM REPORT_TEST_1", null);

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(s));

        List<ReportParam> list = new ArrayList<ReportParam>();
        list.add(param);
        ReportContext ctx = new ReportContext("R002", list, Locale.US);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length() > 0, is(true));
    }

    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト。パラメータのみ。
     */
    @Test
    public void testReport_creator() {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length() > 0, is(true));
    }
    
    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト（異常系）。パラメータのみ。
     */
    @Test
    public void testReport_creator_Faild() {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "ERR001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        try {
            ReportManager.createReport(ctx);
            fail("JRExceptionが発生し、リスローされる");
        } catch (ReportException e) {
            assertThat("元例外の確認", e.getCause(),
                    is(instanceOf(JRException.class)));
        }
    }

    /**
     * 帳票作成クラスにデフォルト以外を指定した場合のテスト。フィールド指定。
     */
    @Test
    public void testReport_creator_list() {

        SqlResultSet s = db.executeSql("SELECT * FROM REPORT_TEST_1", null);

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(s));
        ctx.addReportParam(param);

        File report = ReportManager.createReport(ctx);

        assertThat("帳票ファイルが正常に出力されること", report.length() > 0, is(true));
    }

    /**
     * 帳票オブジェクトストリームの正常出力（パラメータのみ）テスト。
     */
    @Test
    public void testReportStream() {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(s.get(0));
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

        SqlResultSet s = db.executeSql("SELECT * FROM REPORT_TEST_1", null);

        ReportContext ctx = new ReportContext("R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(s));
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

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R001");

        ReportParam param = new ReportParam(s.get(0));
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

        SqlResultSet s = db.executeSql("SELECT * FROM REPORT_TEST_1", null);

        ReportContext ctx = new ReportContext("virtualizerReportCreator",
                "R002");

        ReportParam param = new ReportParam(new Object(),
                new SqlResultSetDataSource(s));
        ctx.addReportParam(param);

        InputStream report = ReportManager.createReportStream(ctx);

        assertThat("帳票ストリームが取得出来ていること", isInputStreamSizeCheck(report),
                is(true));
    }

}
