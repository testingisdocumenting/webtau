package pages

import static org.testingisdocumenting.webtau.WebTauDsl.*

class CalculationPage {
    def calcButton = $('#calc-button')
    def errorDismissButton = $('#error-dismiss')
    def feedback = $('.feedback')
    def error = $('#error')
    def input = $('#value-a')
    def results = $('#results .result')

    def open() {
        browser.reopen('/calculation')
    }

    def start() {
        open()
        calculate()
    }

    def calculate() {
        calcButton.click()
    }

    def dismissError() {
        errorDismissButton.click()
    }
}