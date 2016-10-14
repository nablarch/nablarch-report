package nablarch.integration.report.exporter;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import nablarch.integration.report.ReportContext;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * 帳票出力クラスを表すインターフェース。
 * 
 * @author Naoki Tamura
 */
public interface ReportExporter {

    /**
     * 帳票ファイルを出力します。
     * 
     * @param ctx 帳票コンテキスト
     * @param jasperPrintList 帳票プリントオブジェクトのリスト
     * @return 帳票ファイルオブジェクト
     */
    File export(ReportContext ctx, List<JasperPrint> jasperPrintList);

    /**
     * 帳票オブジェクトのストリームを返します。
     * 
     * @param ctx
     *            帳票コンテキスト
     * @param jasperPrintList
     *            帳票プリントオブジェクト
     * @return 帳票オブジェクトのストリーム
     */
    InputStream exportStream(ReportContext ctx,
            List<JasperPrint> jasperPrintList);

}
