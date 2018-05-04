import React from 'react'
import JSONTree from 'react-json-tree'

import './JsonPayload.css'

const jsonValueRenderer = (checks) => {
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

const expandNode = () => true

const jsonTheme = {tree: ({ style }) => ({style: { ...style, backgroundColor: undefined }})}

const JsonPayload = ({json, checks}) => {
    return (
        <div className="data json">
            <JSONTree data={json}
                      theme={jsonTheme}
                      valueRenderer={jsonValueRenderer(checks)}
                      shouldExpandNode={expandNode}/>
        </div>
    )
}

function buildPath(parts) {
    return parts.reverse().slice(1).reduce((prev, curr) => {
        return prev + (typeof curr === 'number' ?
            '[' + curr + ']':
            '.' + curr)
    }, 'root')
}

export default JsonPayload
