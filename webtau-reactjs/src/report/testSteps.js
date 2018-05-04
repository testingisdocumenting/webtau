export default [
    {
        "message": [
            {
                "type": "action",
                "value": "opened"
            },
            {
                "type": "url",
                "value": "http://localhost:8180/search"
            }
        ]
    },
    {
        "message": [
            {
                "type": "error",
                "value": "failed"
            },
            {
                "type": "none",
                "value": "submitting search value \u0027search this\u0027"
            },
            {
                "type": "delimiter",
                "value": ":"
            },
            {
                "type": "error",
                "value": "can\u0027t clear as element is not found: by css #search-box. Try to wait for it to appear first."
            }
        ],
        "children": [
            {
                "message": [
                    {
                        "type": "error",
                        "value": "failed"
                    },
                    {
                        "type": "action",
                        "value": "setting value"
                    },
                    {
                        "type": "stringValue",
                        "value": "search this"
                    },
                    {
                        "type": "preposition",
                        "value": "to"
                    },
                    {
                        "type": "selectorType",
                        "value": "by css"
                    },
                    {
                        "type": "selectorValue",
                        "value": "#search-box"
                    },
                    {
                        "type": "delimiter",
                        "value": ":"
                    },
                    {
                        "type": "error",
                        "value": "can\u0027t clear as element is not found: by css #search-box. Try to wait for it to appear first."
                    }
                ],
                "children": [
                    {
                        "message": [
                            {
                                "type": "error",
                                "value": "failed"
                            },
                            {
                                "type": "action",
                                "value": "clearing"
                            },
                            {
                                "type": "selectorType",
                                "value": "by css"
                            },
                            {
                                "type": "selectorValue",
                                "value": "#search-box"
                            },
                            {
                                "type": "delimiter",
                                "value": ":"
                            },
                            {
                                "type": "error",
                                "value": "can\u0027t clear as element is not found: by css #search-box. Try to wait for it to appear first."
                            }
                        ]
                    }
                ]
            }
        ]
    }
]
