package nablarch.integration.report.datasource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nablarch.core.db.statement.SqlResultSet;
import nablarch.integration.report.testhelper.ReportDbAccessSupport;
import nablarch.integration.report.testhelper.ReportTestRule;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nablarch.integration.report.testhelper.ReportTest1;

/**
 * {@link SqlResultSetDataSource} のテストクラス
 * 
 * @author Naoki Tamura
 */
public class SqlResultSetDataSourceTest {

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
    }

    /**
     * {@link SqlResultSetDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)}
     * のテスト。
     * 
     * @throws Exception
     */
    @Test
    public void testGetFieldValue() throws Exception {
        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 ORDER BY COL1", null);
        JRDataSource ds = new SqlResultSetDataSource(s);

        JRDesignField fld = new JRDesignField();
        fld.setName("col1");
        fld.setValueClass(String.class);
        fld.setValueClassName("java.lang.String");

        ds.next();
        Object ret = ds.getFieldValue(fld);

        assertThat("型のテスト", ret instanceof String, is(true));
        assertThat("値のテスト", ret.toString().equals("1         "), is(true));

        ds.next();
        ret = ds.getFieldValue(fld);

        assertThat("型のテスト", ret instanceof String, is(true));
        assertThat("値のテスト", ret.toString().equals("2         "), is(true));

    }

    /**
     * {@link SqlResultSetDataSource#next()}のテスト。
     * 
     * @throws Exception
     */
    @Test
    public void testNext() throws Exception {
        SqlResultSet s = db.executeSql(
                "SELECT * FROM REPORT_TEST_1 ORDER BY COL1", null);
        JRDataSource ds = new SqlResultSetDataSource(s);

        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＯＫ", ds.next(), is(true));
        assertThat("次レコード取得ＮＧ", ds.next(), is(false));
    }

}
