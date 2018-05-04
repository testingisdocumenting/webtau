import React from 'react'
import ReactDOM from 'react-dom'

import WebTauReport from './report/WebTauReport'
import Report from './report/Report'

import './index.css'

global.WebTauReport = WebTauReport

global.renderReport = () => {
    ReactDOM.render(<WebTauReport report={new Report(global.testReport)} />, document.getElementById('root'));
}

global.renderReport()
