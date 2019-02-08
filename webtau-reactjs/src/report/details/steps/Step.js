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

import Card from '../../widgets/Card'

import {StepMessage} from './StepMessage'
import {StepTime} from './StepTime'

import './Step.css'

export class Step extends React.Component {
    state = {
        collapsed: true
    }

    render() {
        const {step, isTopLevel} = this.props
        const {collapsed} = this.state

        const children = step.children ? step.children.map((c, idx) => <Step key={idx} step={c}/>) : null

        const ParentContainer = isTopLevel ? Card : 'div'
        return (
            <ParentContainer className="step">
                <div className="message-parts">
                    <StepMessage message={step.message}/>
                    {this.renderMoreToggle()}
                    <StepTime millis={step.elapsedTime}/>
                </div>

                {children && !collapsed && <div className="steps-children">{children}</div>}
            </ParentContainer>
        )
    }

    renderMoreToggle = () => {
        const {step, isTopLevel} = this.props
        const {collapsed} = this.state

        if (!step.children || !isTopLevel || !collapsed) {
            return <div/>;
        }

        return (
            <div className="show-children" onClick={this.showChildren}>...</div>
        )
    }

    showChildren = () => {
        this.setState({collapsed: false})
    }
}
