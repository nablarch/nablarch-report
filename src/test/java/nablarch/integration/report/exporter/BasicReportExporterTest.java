package nablarch.integration.report.exporter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nablarch.core.beans.BeanUtil;
import nablarch.core.util.DateUtil;
import nablarch.core.util.FilePathSetting;
import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.ReportParam;
import nablarch.integration.report.outputresolver.BasicReportOutputResolver;
import nablarch.integration.report.testhelper.CompileUtil;
import nablarch.integration.report.testhelper.ReportTest1;
import nablarch.test.support.SystemRepositoryResource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BasicReportExporterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public SystemRepositoryResource repositoryResource = new SystemRepositoryResource("nablarch/integration/report/default-definition.xml");

    @BeforeClass
    public static void beforeClass() throws Throwable {
        CompileUtil.compileReportDirAll("report/R001");
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
        String reportDir = setting.getBaseDirectory("report").getPath() + '/' + reportId;

        return new File(reportDir + '/' + "index.jasper");
    }

    /**
     * 帳票ファイルが正常に出力されること。
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExport() throws Exception {
        ReportContext ctx = new ReportContext("R001");

        ReportTest1 bean = new ReportTest1("1", "tttt", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS"));
        ReportParam param = new ReportParam(bean);
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    BeanUtil.createMapAndCopy(param.getParams()),
                    param.getDataSource());

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(print);

        BasicReportExporter re = new BasicReportExporter();
        re.setReportOutputResolver(new BasicReportOutputResolver());
        File report = re.export(ctx, jasperPrintList);

        assertThat("帳票ファイルが作成されている", report.length(), is(greaterThan(0L)));

    }

    /**
     * 帳票オブジェクトのストリームを作成出来ること。
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testExportStream() throws Exception {

        ReportContext ctx = new ReportContext("R001");

        ReportTest1 bean = new ReportTest1("1", "tttt", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS"));
        ReportParam param = new ReportParam(bean);
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    BeanUtil.createMapAndCopy(param.getParams()),
                    param.getDataSource());

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

        ReportContext ctx = new ReportContext("R001");

        ReportTest1 bean = new ReportTest1("1", "tttt", 999, new BigDecimal("999.99"), DateUtil.getDate("19990901"), DateUtil.getParsedDate("19990901123011123", "yyyyMMddHHmmssSSS"));
        ReportParam param = new ReportParam(bean);
        ctx.addReportParam(param);

        File template = findTemplate(ctx.getReportId());

        JasperPrint print = JasperFillManager.fillReport(template.getAbsolutePath(),
                    BeanUtil.createMapAndCopy(param.getParams()),
                    param.getDataSource());

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(print);

        BasicReportExporter re = new BasicReportExporter();
        re.setReportOutputResolver(new BasicReportOutputResolver());

        expectedException.expect(ReportException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(JRException.class));

        Thread.currentThread().interrupt();
        re.export(ctx, jasperPrintList);

    }

}
