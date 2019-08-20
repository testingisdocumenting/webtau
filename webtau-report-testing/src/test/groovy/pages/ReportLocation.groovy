package pages

import static com.twosigma.webtau.WebTauDsl.getCfg

class ReportLocation {
    static String fullUrl(String reportName) {
        return 'file://' + cfg.workingDir.toAbsolutePath()
                .resolve('..')
                .resolve('..')
                .resolve('..')
                .resolve('..')
                .resolve('webtau-feature-testing')
                .resolve('target')
                .resolve('classes')
                .resolve('scenarios')
                .resolve(reportName).toAbsolutePath()
    }
}
