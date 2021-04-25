package pages

import static org.testingisdocumenting.webtau.WebTauDsl.*

class FormPage {
    def name = $('#name')
    def rank = $('#rank')
    def confirmation = $('#confirmation')
    def choice = $('[name="choice"]')
    def startDate = $('#startDate')
}