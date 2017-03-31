package nablarch.integration.report.templateresolver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Locale;

import nablarch.core.ThreadContext;
import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportParam;
import nablarch.integration.report.testhelper.CompileUtil;
import nablarch.test.support.SystemRepositoryResource;
import nablarch.test.support.db.helper.DatabaseTestRunner;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link BasicReportTemplateResolver}のテスト
 * 
 * @author Naoki Tamura
 */
@RunWith(DatabaseTestRunner.class)
public class BasicReportTemplateResolverTest {

    @Rule
    public SystemRepositoryResource repositoryResource = new SystemRepositoryResource("nablarch/integration/report/default-definition.xml");

    @BeforeClass
    public static void beforeClass() throws Throwable {
        ThreadContext.setLanguage(Locale.JAPANESE);
        CompileUtil.compileReportDirAll("report/R001");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ThreadContext.clear();
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
