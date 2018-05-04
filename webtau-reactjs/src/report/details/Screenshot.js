import React from 'react'

const Screenshot = ({test}) => {
    return (
        <div className="image">
            <img src={"data:image/png;base64," + test.screenshot} width="100%"/>
        </div>
    )
}

export default Screenshot
