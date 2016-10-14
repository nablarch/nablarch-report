package nablarch.integration.report.outputresolver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.ReportParam;
import nablarch.integration.report.testhelper.ReportTestRule;

/**
 * {@link BasicReportOutputResolver}のテストクラス。
 * 
 * @author Naoki Tamura
 */
public class BasicReportOutputResolverTest {

    @ClassRule
    public static ReportTestRule r = new ReportTestRule();

    @Rule
    public ExpectedException ee = ExpectedException.none();

    /**
     * 指定した帳票出力ファイルオブジェクトが取得することが出来ること。
     */
    @Test
    public void testGetReportOutput() {

        ReportOutputResolver r = new BasicReportOutputResolver();

        ReportContext ctx = new ReportContext("R001");

        ReportParam param = new ReportParam(null);
        ctx.addReportParam(param);

        File output = r.getReportOutput(ctx);

        assertThat(output.exists(), is(true));

    }

    /**
     * 帳票出力ファイルのパスとして存在しないパスを指定した場合に例外が発生すること。
     */
    @Test
    public void testGetReportOutput_NotFind() {

        ee.expect(ReportException.class);

        ReportOutputResolver r = new BasicReportOutputResolver();

        ReportContext ctx = new ReportContext("aaaaaa");

        ReportParam param = new ReportParam(null);
        ctx.addReportParam(param);

        r.getReportOutput(ctx);

        fail("誤ったファイルパスを指定した場合は、例外が発生されなければならない。");

    }

}
