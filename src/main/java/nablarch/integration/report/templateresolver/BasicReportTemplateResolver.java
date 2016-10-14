package nablarch.integration.report.templateresolver;

import java.io.File;
import java.util.Locale;

import nablarch.core.ThreadContext;
import nablarch.core.util.FilePathSetting;
import nablarch.integration.report.ReportContext;
import nablarch.integration.report.ReportParam;

/**
 * {@inheritDoc}
 * 
 * @author Naoki Tamura
 */
public class BasicReportTemplateResolver implements ReportTemplateResolver {

    /** テンプレートファイル名のデフォルトベース名 */
    private static final String DEFUALT_BASE_NAME = "index";

    /**
     * {@inheritDoc}。
     * <p/>
     * ディレクトリ、ファイルの解決は
     * {@link BasicReportTemplateResolver#getReportTemplate(String, String, Locale)}
     * を参照. <br />
     * 
     * @param ctx
     *            レポートコンテキスト
     * @param param
     *            レポートパラメータ
     * @return 帳票テンプレートファイル
     */
    @Override
    public File getReportTemplate(ReportContext ctx, ReportParam param) {

        FilePathSetting setting = FilePathSetting.getInstance();
        String reportDir = setting.getBaseDirectory("report").getPath() + "/"
                + ctx.getReportId();

        String templateName = param.getTemplateName();

        // テンプレート名が未設定の場合はデフォルトベース名を設定
        if (templateName == null) {
            templateName = DEFUALT_BASE_NAME;
        }

        // ロケールの取得
        // 指定がない場合は、{@link ThreadContext}より取得した値を設定する。
        Locale locale = ctx.getLocale();
        if (locale == null) {
            locale = ThreadContext.getLanguage();
        }

        // Localeにあった帳票テンプレートを取得
        // Localeにあった帳票テンプレートが取得出来ない場合は、言語指定のないテンプレートファイルの取得を試みる
        File template = new File(reportDir + "/" + templateName + "_"
                + locale.getLanguage() + ".jasper");

        if (!template.exists()) {
            template = new File(reportDir + "/" + templateName + ".jasper");
        }

        return template;
    }
}
