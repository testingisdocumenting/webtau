import static com.twosigma.webtau.WebTauGroovyDsl.*

class Meta {
    static void owner(String name) {
        attachTestMetaValue("owner", name)
    }
}