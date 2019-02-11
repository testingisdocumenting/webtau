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

import './TestErrorMessage.css'

const linesIncrement = 20

class TestErrorMessage extends React.Component {
    state = {
        numberOfVisibleLines: linesIncrement
    }

    render() {
        const {message} = this.props
        const {numberOfVisibleLines} = this.state

        const allLines = message.split('\n')
        const visibleLines = allLines.slice(0, numberOfVisibleLines)

        const hasMoreLines = numberOfVisibleLines < allLines.length
        return (
            <div className="error-message">
                <pre>{visibleLines.join('\n')}</pre>

                {hasMoreLines && <div className="show-more" onClick={this.showMore}>more ...</div>}
            </div>
        )
    }

    showMore = () => {
        this.setState(prev => ({numberOfVisibleLines: prev.numberOfVisibleLines + linesIncrement}))
    }
}

export default TestErrorMessage
