/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
import JSONTree from 'react-json-tree'

import './JsonPayload.css'

const expandNode = () => true
const jsonTheme = {tree: ({ style }) => ({style: { ...style, backgroundColor: undefined }})}

function JsonPayload({json, checks}) {
    return (
        <div className="data json">
            <JSONTree data={json}
                      theme={jsonTheme}
                      valueRenderer={jsonValueRenderer(checks)}
                      shouldExpandNode={expandNode}/>
        </div>
    )
}

function jsonValueRenderer(checks) {
    checks = checks || {}
    checks.failedPaths = checks.failedPaths || []
    checks.passedPaths = checks.passedPaths || []

    return (pretty, raw, ...pathParts) => {
        const fullPath = buildPath(pathParts)
        const isFailed = checks.failedPaths.indexOf(fullPath) !== -1
        const isPassed = checks.passedPaths.indexOf(fullPath) !== -1

        const isHighlighted = isFailed || isPassed
        const className = isHighlighted ?
            'json-value-highlight' + (isFailed ? ' failed' : ' passed') : null

        return isHighlighted ? <span className={className}>{pretty}</span> : pretty;
    }
}

function buildPath(parts) {
    return parts.reverse().slice(1).reduce((prev, curr) => {
        return prev + (typeof curr === 'number' ?
            '[' + curr + ']':
            '.' + curr)
    }, 'root')
}

export default JsonPayload
