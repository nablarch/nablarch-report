package nablarch.integration.report.outputresolver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportException;
import nablarch.integration.report.ReportParam;
import nablarch.test.support.SystemRepositoryResource;
import nablarch.test.support.db.helper.DatabaseTestRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * {@link BasicReportOutputResolver}のテストクラス。
 * 
 * @author Naoki Tamura
 */
@RunWith(DatabaseTestRunner.class)
public class BasicReportOutputResolverTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public SystemRepositoryResource repositoryResource = new SystemRepositoryResource("nablarch/integration/report/default-definition.xml");

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

        expectedException.expect(ReportException.class);

        ReportOutputResolver r = new BasicReportOutputResolver();

        ReportContext ctx = new ReportContext("aaaaaa");

        ReportParam param = new ReportParam(null);
        ctx.addReportParam(param);

        r.getReportOutput(ctx);
    }

}
