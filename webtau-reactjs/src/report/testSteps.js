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
