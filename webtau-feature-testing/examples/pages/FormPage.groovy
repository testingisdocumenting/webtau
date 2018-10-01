package pages

import static com.twosigma.webtau.WebTauDsl.*

class FormPage {
    def name = $('#name')
    def rank = $('#rank')
    def startDate = $('#startDate')
}