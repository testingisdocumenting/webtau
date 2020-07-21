package pages

import static org.testingisdocumenting.webtau.WebTauDsl.*

class FormPage {
    def name = $('#name')
    def rank = $('#rank')
    def confirmation = $('#confirmation')
    def startDate = $('#startDate')
}