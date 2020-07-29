/*
 * Copyright 2020 webtau maintainers
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

import {SelectedIndexes} from "../SelectedIndexes"

import CliBackgroundDetails from './CliBackgroundDetails'

import '../../widgets/Table.css'
import './TestCliBackground.css';

export default class TestCliBackground extends React.Component {
    state = {}
    static stateName = 'cliBackgroundIdxs'

    static getDerivedStateFromProps(props) {
        const backgroundIdxs = props.urlState[TestCliBackground.stateName]
        return {selectedIndexes: new SelectedIndexes(backgroundIdxs)}
    }

    render() {
        const {test} = this.props

        return (
            <table className="test-cli-background-table table">
                <thead>
                <tr>
                    <th width="35px"/>
                    <th width="auto">Command</th>
                    <th width="60px">Start</th>
                </tr>
                </thead>
                <tbody>
                {test.cliBackground.map((cliBackground, idx) => this.renderRow(cliBackground, idx))}
                </tbody>
            </table>
        )
    }

    renderRow(cliBackground, idx) {
        const isExpanded = this.isExpanded(idx)

        const className = 'test-cli-background'
            + (isExpanded ? ' expanded' : '')

        const startDateTime = new Date(cliBackground.startTime)

        const onClick = () => this.onCollapseToggleClick(idx)

        return (
            <React.Fragment key={idx}>
                <tr className={className}>
                    <td className="collapse-toggle" onClick={onClick}>{isExpanded ? '-' : '+'}</td>
                    <td className="command" onClick={onClick}>{cliBackground.command}</td>
                    <td className="start" onClick={onClick}>{moment(startDateTime).local().format('HH:mm:ss.SSS')}</td>
                </tr>

                {isExpanded && <CliBackgroundDetails cliBackground={cliBackground}/>}
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
            [TestCliBackground.stateName]: selectedIndexes.toggle(idx)
        })
    }
}
