package pages

import static com.twosigma.webtau.WebTauDsl.*

class CalculationPage {
    def calcButton = $('#calc-button')
    def feedback = $('.feedback')
    def results = $('#results .result')

    def start() {
        browser.reopen('/calculation')
        calcButton.click()
    }
}