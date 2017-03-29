package nablarch.integration.report.testhelper;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "REPORT_TEST_1")
public class ReportTest1 {

    public ReportTest1() {
    }

    public ReportTest1(String col1, String col2, Integer col3, BigDecimal col4, Date col5, Date col6) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
    }

    @Id
    @Column(name = "COL1", length = 10, nullable = false)
    public String col1;

    @Column(name = "COL2", length = 10, nullable = false)
    public String col2;

    @Column(name = "COL3", precision = 10, nullable = false)
    public Integer col3;

    @Column(name = "COL4", precision = 10, scale = 2, nullable = false)
    public BigDecimal col4;

    @Temporal(TemporalType.DATE)
    @Column(name = "COL5", nullable = false)
    public Date col5;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COL6", nullable = false)
    public Date col6;
}
