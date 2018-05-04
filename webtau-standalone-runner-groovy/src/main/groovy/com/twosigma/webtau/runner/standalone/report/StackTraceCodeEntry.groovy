package com.twosigma.webtau.runner.standalone.report

import groovy.transform.Canonical

@Canonical
class StackTraceCodeEntry {
    String filePath
    List<Integer> lineNumbers
}
