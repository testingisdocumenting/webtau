
/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import moment from 'moment'

import ElapsedTime from "../../widgets/ElapsedTime"
import CliCallDetails from "./CliCallDetails"
import {SelectedIndexes} from "../SelectedIndexes"

import '../../widgets/Table.css'
import './TestCliCalls.css';

export default class TestCliCalls extends React.Component {
    state = {}
    static stateName = 'cliCallIdxs'

    static getDerivedStateFromProps(props) {
        const callIdxs = props.urlState[TestCliCalls.stateName]
        return {selectedIndexes: new SelectedIndexes(callIdxs)}
    }

    render() {
        const {test} = this.props

        return (
            <table className="test-cli-calls-table table">
                <thead>
                <tr>
                    <th width="35px"/>
                    <th width="auto">Command</th>
                    <th width="80px">Exit Code</th>
                    <th width="60px">Start</th>
                    <th width="60px">Took</th>
                </tr>
                </thead>
                <tbody>
                {test.cliCalls.map((cliCall, idx) => this.renderRow(cliCall, idx))}
                </tbody>
            </table>
        )
    }

    renderRow(cliCall, idx) {
        const isExpanded = this.isExpanded(idx)

        const hasProblem = cliCall.mismatches.length > 0 || !!cliCall.errorMessage

        const className = 'test-cli-call'
            + (hasProblem ? ' with-problem' : '')
            + (isExpanded ? ' expanded' : '')

        const startDateTime = new Date(cliCall.startTime)

        const onClick = () => this.onCollapseToggleClick(idx)

        return (
            <React.Fragment key={idx}>
                <tr className={className}>
                    <td className="collapse-toggle" onClick={onClick}>{isExpanded ? '-' : '+'}</td>
                    <td className="command" onClick={onClick}>{cliCall.command}</td>
                    <td className="exit-code" onClick={onClick}>{cliCall.exitCode}</td>
                    <td className="start" onClick={onClick}>{moment(startDateTime).local().format('HH:mm:ss.SSS')}</td>
                    <td className="cli-call-elapsed-time" onClick={onClick}>
                        <ElapsedTime millis={cliCall.elapsedTime}/>
                    </td>
                </tr>

                {isExpanded && <CliCallDetails cliCall={cliCall}/>}
            </React.Fragment>
        )

    }

    isExpanded(idx) {
        return this.state.selectedIndexes.isExpanded(idx)
    }

    onCollapseToggleClick = (idx) => {
        const {onInternalStateUpdate} = this.props
        const {selectedIndexes} = this.state

        onInternalStateUpdate({
            [TestCliCalls.stateName]: selectedIndexes.toggle(idx)
        })
    }
}
