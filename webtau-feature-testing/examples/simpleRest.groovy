import static com.twosigma.webtau.WebTauDsl.http
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("simple get") {
    http.get("/weather") {
        temperature.should == 88
    }
}