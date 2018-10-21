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

import * as React from 'react'

import './Table.css'
import './SortableTable.css'

export default class SortableTable extends React.Component {
    state = {
        sortColumnName: '',
        sortAscending: false
    }

    render() {
        const {header, data, className} = this.props
        const {sortColumnName, sortAscending} = this.state

        const sortedData = sortColumnName ?
            sortData(data, findSortColumnIdx(), sortAscending):
            data

        const fullClassName = 'sortable-table table' + (className ? ' ' + className : '')

        return (
            <table className={fullClassName}>
                <thead>
                <tr>
                    {header.map(columnName => this.renderHeaderEntry(columnName))}
                </tr>
                </thead>
                <tbody>
                    {sortedData.map((row, idx) => <RowEntry key={idx} row={row}/>)}
                </tbody>
            </table>
        )

        function findSortColumnIdx() {
            return header.indexOf(sortColumnName)
        }
    }

    renderHeaderEntry = (columnName) => {
        const {sortColumnName, sortAscending} = this.state
        const sortIndicator = sortColumnName === columnName ?
            <SortIndicator ascending={sortAscending}/>:
            null

        return (
            <th key={columnName}
                onClick={() => this.selectSortColumn(columnName)}>
                <div className="column-with-indicator">
                    {sortIndicator}
                    <div>{columnName}</div>
                </div>
            </th>
        )
    }

    selectSortColumn = (sortColumnName) => {
        this.setState(prev => {
            const sortAscending = prev.sortColumnName === sortColumnName ?
                !prev.sortAscending:
                true;

            return {
                sortColumnName,
                sortAscending
            }
        })
    }
}

function sortData(data, idx, isAscending) {
    const sorted = [...data]
    sorted.sort((a, b) => {
        return compare(a, b) * (isAscending ? -1 : 1)
    })

    return sorted

    function compare(a, b) {
        if (a[idx] < b[idx]) {
            return -1
        } else if (a[idx] > b[idx]) {
            return 1
        } else {
            return 0
        }
    }
}

function RowEntry({row}) {
    return (
        <tr>
            {row.map((value, idx) => <td key={idx}>{value}</td>)}
        </tr>
    )
}

function SortIndicator({ascending}) {
    const className = 'sort-indicator ' + (ascending ? 'ascending' : 'descending')
    return (
        <div className={className}>&#8675;</div>
    )
}