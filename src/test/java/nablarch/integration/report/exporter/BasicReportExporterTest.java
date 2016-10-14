package nablarch.integration.report.exporter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nablarch.core.db.statement.SqlResultSet;
import nablarch.core.util.FilePathSetting;
import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.ReportParam;
import nablarch.integration.report.outputresolver.BasicReportOutputResolver;
import nablarch.integration.report.testhelper.CompileUtil;
import nablarch.integration.report.testhelper.ReportDbAccessSupport;
import nablarch.integration.report.testhelper.ReportTest1;
import nablarch.integration.report.testhelper.ReportTestRule;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BasicReportExporterTest {

    @Rule
    public ExpectedException ee = ExpectedException.none();

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

        CompileUtil.compileReportDirAll("report/R001");
        CompileUtil.compileReportDirAll("report/ERR001");
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
     * 帳票テンプレートファイルオブジェクトを取得する。
     * 
     * @param reportId
     * @return 帳票テンプレートファイル
     */
    private File findTemplate(String reportId) {

        FilePathSetting setting = FilePathSetting.getInstance();
        String reportDir = setting.getBaseDirectory("report").getPath() + "/"
                + reportId;

        return new File(reportDir + "/" + "index.jasper");
    }

    /**
     * 帳票ファイルが正常に出力されること。
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExport() throws Exception {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    (Map<String, Object>) param.getParams(),
                    param.getDataSource());
        } catch (JRException e) {
            e.printStackTrace();
        }

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(print);

        BasicReportExporter re = new BasicReportExporter();
        re.setReportOutputResolver(new BasicReportOutputResolver());
        File report = re.export(ctx, jasperPrintList);

        assertThat("帳票ファイルが作成されている", report.length() > 0, is(true));

    }

    /**
     * 帳票オブジェクトのストリームを作成出来ること。
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExportStream() {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    (Map<String, Object>) param.getParams(),
                    param.getDataSource());
        } catch (JRException e) {
            e.printStackTrace();
        }

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(print);

        BasicReportExporter re = new BasicReportExporter();
        re.setReportOutputResolver(new BasicReportOutputResolver());
        InputStream report = re.exportStream(ctx, jasperPrintList);

        assertThat("帳票ストリームオブジェクトが存在する", isInputStreamSizeCheck(report),
                is(true));

    }

    /**
     * スレッド割り込みによる例外キャッチの確認。
     * 
     * @throws Exception
     */
    @Test
    public void testFailedInExport() throws Exception {

        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 WHERE COL1= '1'", null);

        ReportContext ctx = new ReportContext("R001");
        ReportParam param = new ReportParam(s.get(0));
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    (Map<String, Object>) param.getParams(),
                    param.getDataSource());
        } catch (JRException e) {
            e.printStackTrace();
        }

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(print);

        BasicReportExporter re = new BasicReportExporter();
        re.setReportOutputResolver(new BasicReportOutputResolver());

        try {
            Thread.currentThread().interrupt();
            re.export(ctx, jasperPrintList);
            fail("JRExceptionが発生し、リスローされる");
        } catch (ReportException e) {
            assertThat("元例外の確認", e.getCause(),
                    is(instanceOf(JRException.class)));
        }

    }

}
