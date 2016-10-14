package nablarch.integration.report.templateresolver;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Locale;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.testhelper.CompileUtil;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import nablarch.integration.report.ReportParam;
import nablarch.integration.report.testhelper.ReportTestRule;

/**
 * {@link BasicReportTemplateResolver}のテスト
 * 
 * @author Naoki Tamura
 */
public class BasicReportTemplateResolverTest {

    @ClassRule
    public static ReportTestRule r = new ReportTestRule();

    @BeforeClass
    public static void beforeClass() throws Throwable {
        CompileUtil.compileReportDirAll("report/R001");
    }

    /**
     * デフォルト名の帳票テンプレートファイルオブジェクトを取得できること。
     */
    @Test
    public void testGetReportTemplate() {

        ReportTemplateResolver r = new BasicReportTemplateResolver();

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(null);
        ctx.addReportParam(param);

        File template = r.getReportTemplate(ctx, param);

        assertThat(template.exists(), is(true));
        assertThat(template.getName().equals("index.jasper"), is(true));

    }

    /**
     * 指定した帳票テンプレートファイルオブジェクトを取得できること。
     */
    @Test
    public void testGetReportTemplate_setTemplate() {

        ReportTemplateResolver r = new BasicReportTemplateResolver();

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam("hoge", new Object(), new JREmptyDataSource());
        ctx.addReportParam(param);

        File template = r.getReportTemplate(ctx, param);

        assertThat(template.exists(), is(true));
        assertThat(template.getName().equals("hoge.jasper"), is(true));

    }

    /**
     * 存在しない帳票テンプレートファイルのパスを指定した場合、ファイルが存在しないこと。
     */
    @Test
    public void testGetReportTemplate_NotFind() {

        ReportTemplateResolver r = new BasicReportTemplateResolver();

        ReportContext ctx = new ReportContext("aaaaaa");

        ReportParam param = new ReportParam(new String(), new Object());
        ctx.addReportParam(param);

        File template = r.getReportTemplate(ctx, param);

        assertThat(template.exists(), is(false));

    }

    /**
     * 指定したロケールに応じた帳票テンプレートファイルオブジェクトを取得出来ること。
     */
    @Test
    public void testGetReportTemplate_Locale() {

        ReportTemplateResolver r = new BasicReportTemplateResolver();

        ReportContext ctx = new ReportContext("R001", Locale.US);

        ReportParam param = new ReportParam(null);
        ctx.addReportParam(param);

        File template = r.getReportTemplate(ctx, param);

        assertThat(template.exists(), is(true));
        assertThat(template.getName().equals("index_en.jasper"), is(true));
    }
}
