import React, {Component} from 'react'
import {DebounceInput} from 'react-debounce-input';

import ListOfTests from './ListOfTests'
import StatusFilter from './StatusFilter'
import TestDetails from './TestDetails'
import HttpCalls from './details/http/HttpCalls'
import WebTauReportStateCreator from './WebTauReportStateCreator'
import OverallSummary from './OverallSummary'

import './WebTauReport.css'

class WebTauReport extends Component {
    constructor(props) {
        super(props)

        this._stateCreator = new WebTauReportStateCreator(props.report)

        this.state = this.stateFromUrl()
    }

    render() {
        const {report} = this.props
        const {testId, statusFilter, filterText} = this.state

        return (
            <div className="report">
                <div className="report-name-area" onClick={this.onReportNameClick}>
                    <div className="tool-name">webtau</div>
                </div>

                <div className="search-area">
                    <DebounceInput value={filterText}
                                   onChange={this.onFilterTextChange}
                                   placeholder="filter text"
                                   minLength={3}
                                   debounceTimeout={300}/>
                </div>

                <div className="items-lists-area">
                    <ListOfTests tests={this.filteredTests}
                                 selectedId={testId}
                                 onSelect={this.onTestSelect}/>
                </div>

                <div className="test-details-area">
                    {this.renderTestDetailsArea()}
                </div>

                <div className="status-filter-area">
                    <StatusFilter summary={report.summary}
                                  onTitleClick={this.onHeaderTitleClick}
                                  selectedStatusFilter={statusFilter}
                                  onTestStatusSelect={this.onTestStatusSelect}/>
                </div>
            </div>
        )
    }

    renderTestDetailsArea() {
        const {report} = this.props
        const {testId, detailTabName} = this.state

        const selectedTest = testId ? report.findTestById(testId) : null

        if (selectedTest) {
            return (
                <TestDetails test={selectedTest}
                             selectedDetailTabName={detailTabName}
                             onDetailsTabSelection={this.onDetailsTabSelection}
                             detailTabs={selectedTest.details}
                             urlState={this.state}
                             onInternalStateUpdate={this.onDetailsStateUpdate}/>
            )
        }

        return (
            <OverallSummary report={report}/>
        )
    }

    get filteredTests() {
        const {report} = this.props
        const {statusFilter, filterText} = this.state

        return report.withStatusAndFilteredByText(statusFilter, filterText)
    }

    onDetailsStateUpdate = (newState) => this.pushPartialUrlState(newState)

    onHeaderTitleClick = () => this.pushPartialUrlState({selectedId: null})

    onTestSelect = (id) => {
        const currentTestId = this.state.testId
        if (id === currentTestId) {
            return
        }

        this.pushFullUrlState({
            ...createEmptyFullState(),
            detailTabName: this.state.detailTabName,
            statusFilter: this.state.statusFilter,
            filterText: this.state.filterText,
            testId: id
        })
    }

    onReportNameClick = () => {
        this.pushFullUrlState(createEmptyFullState())
    }

    onTestStatusSelect = (status) => {
        const {report} = this.props
        const {filterText} = this.state

        const filtered = report.withStatusAndFilteredByText(status, filterText)
        const firstTestId = filtered.length > 0 ? filtered[0].id : null

        this.pushPartialUrlState({statusFilter: status, testId: firstTestId})
    }

    onDetailsTabSelection = (tabName) => this.pushPartialUrlState({detailTabName: tabName})

    onFilterTextChange = (e) => {
        this.pushPartialUrlState({filterText: e.target.value})
    }

    componentDidMount() {
        this.subscribeToUrlChanges()
        this.updateStateFromUrl()
    }

    subscribeToUrlChanges() {
        window.addEventListener('popstate', () => {
            this.updateStateFromUrl();
        });
    }

    stateFromUrl() {
        return this._stateCreator.stateFromUrl(document.location.search)
    }

    updateStateFromUrl() {
        this.setState(this.stateFromUrl())
    }

    pushPartialUrlState(partialNewState) {
        this.pushFullUrlState({...this.state, ...partialNewState})
    }

    pushFullUrlState(fullState) {
        const searchParams = this._stateCreator.buildUrlSearchParams(fullState)
        window.history.pushState({}, '', '?' + searchParams)
        this.updateStateFromUrl()
    }
}

function createEmptyFullState() {
    return {
        testId: '',
        detailTabName: '',
        statusFilter: '',
        filterText: '',
        [HttpCalls.stateName]: ''
    }
}

export default WebTauReport
