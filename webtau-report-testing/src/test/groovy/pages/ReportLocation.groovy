package pages

import java.nio.file.Path

import static org.testingisdocumenting.webtau.WebTauDsl.getCfg

class ReportLocation {
    static String groovyFeatureTestingFullUrl(String reportName) {
        return toUrl(modulePath('webtau-feature-testing')
                .resolve('webtau-reports')
                .resolve('scenarios')
                .resolve(reportName))
    }

    static String javaJunit5FullUrl(String reportName) {
        return toUrl(modulePath('webtau-junit5-examples').resolve(reportName))
    }

    private static String toUrl(Path path) {
        return 'file://' + path.toAbsolutePath()
    }

    private static Path modulePath(String moduleName) {
        return cfg.workingDir.toAbsolutePath()
                .resolve('..')
                .resolve('..')
                .resolve('..')
                .resolve('..')
                .resolve(moduleName)
    }
}
