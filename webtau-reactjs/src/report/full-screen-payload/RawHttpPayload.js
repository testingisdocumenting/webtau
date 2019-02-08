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

import ClipboardJS from 'clipboard'

import Prism from 'prismjs'

import 'prismjs/components/prism-json'
import 'prismjs/plugins/line-highlight/prism-line-highlight'

import 'prismjs/themes/prism.css'
import 'prismjs/plugins/line-highlight/prism-line-highlight.css'

import './RawHttpPayload.css'

export default class RawHttpPayload extends React.Component {
    state = { displayCopied: false }

    render() {
        const {payload} = this.props
        const {displayCopied} = this.state

        const copyToClipboardText = displayCopied ? 'Copied' : 'Copy to clipboard'
        const copyToClipboardClassName = 'raw-http-payload-copy-to-clipboard' +
            (displayCopied ? ' copied' : '')

        return (
            <div className="raw-http-payload">
                <div className={copyToClipboardClassName} ref={this.saveCopyToClipboardNode}>
                    {copyToClipboardText}
                </div>

                <pre className="language-json">
                    <code>
                        {payload}
                    </code>
                </pre>
            </div>
        )
    }

    componentDidMount() {
        Prism.highlightAll()
        this.setupClipboard()
    }

    componentDidUpdate() {
        Prism.highlightAll()
    }

    componentWillUnmount() {
        this.clearTimer()
        this.destroyClipboard()
    }

    saveCopyToClipboardNode = (node) => {
        this.copyToClipboardNode = node
    }

    setupClipboard() {
        const {payload} = this.props

        if (! this.copyToClipboardNode) {
            return
        }

        this.clipboard = new ClipboardJS(this.copyToClipboardNode, {
            text: trigger => {
                this.setState({displayCopied: true})
                this.startRemoveFeedbackTimer()

                return payload
            }
        })
    }

    destroyClipboard() {
        if (this.clipboard) {
            this.clipboard.destroy()
        }
    }

    startRemoveFeedbackTimer() {
        this.removeFeedbackTimer = setTimeout(() => {
            this.setState({displayCopied: false})
        }, 1000)
    }

    clearTimer() {
        if (this.removeFeedbackTimer) {
            clearTimeout(this.removeFeedbackTimer)
        }
    }
}