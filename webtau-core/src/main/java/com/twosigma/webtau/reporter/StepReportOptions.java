package com.twosigma.webtau.reporter;

/**
 * for values we already fetched we don't want report redundant "expecting value to equal" as there
 * is not going to be any delay to fetch a value
 */
public enum StepReportOptions {
    SKIP_START,
    REPORT_ALL
}
