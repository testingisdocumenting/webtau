import React from 'react'

const StackTrace = ({message}) => {
    return (
        <pre className="stack-trace">
            {message}
        </pre>
    )
}

export default StackTrace