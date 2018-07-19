import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario('with exception') {
    throw new RuntimeException('deliberate exception')
}