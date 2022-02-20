/*
 * Copyright 2021 webtau maintainers
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

import * as React from 'react';
import { useState } from 'react';

import './Table.css';
import './SortableTable.css';

export type TableCellType = string | number | object;
export type TableRow = TableCellType[];

export interface TableValuesRenderer {
  canBeExpanded?: (row: TableRow) => boolean;
  cellRenderer?: (columnName: string, value: TableCellType) => JSX.Element;
  expandedRowRenderer?: (row: TableRow) => JSX.Element;
}

interface Props {
  header: string[];
  data: TableCellType[][];
  className?: string;
  renderer?: TableValuesRenderer;
  rowKeyProvider?: (row: TableRow) => string;
}

interface SortColumn {
  name: string;
  ascending: boolean;
}

export function SortableTable({ className, header, data, renderer, rowKeyProvider }: Props) {
  const [sortColumn, setSortColumn] = useState<SortColumn>({ name: '', ascending: false });
  const [expandedRowKeys, setExpandedRowKeys] = useState<{ [id: string]: boolean }>({});

  const sortedData = sortColumn.name ? sortData(data, findSortColumnIdx(), sortColumn.ascending) : data;

  const fullClassName = 'webtau-sortable-table table' + (className ? ' ' + className : '');
  const hasExpand = !!(renderer?.expandedRowRenderer && rowKeyProvider);

  return (
    <table className={fullClassName}>
      <thead>
        <tr>
          {hasExpand && <th />}
          {header.map((columnName) => (
            <HeaderColumn
              key={columnName}
              columnName={columnName}
              sortColumn={sortColumn}
              onClick={handleColumnClick}
            />
          ))}
        </tr>
      </thead>
      <tbody>
        {sortedData.map((row, idx) => {
          const isExpanded = rowKeyProvider ? expandedRowKeys[rowKeyProvider(row)] : false;
          return (
            <RowEntry
              key={idx}
              header={header}
              row={row}
              renderer={renderer}
              hasExpand={hasExpand}
              isExpanded={isExpanded}
              toggleExpand={toggleExpand}
            />
          );
        })}
      </tbody>
    </table>
  );

  function toggleExpand(row: TableRow) {
    if (!rowKeyProvider) {
      return;
    }

    const key = rowKeyProvider(row);
    setExpandedRowKeys({ ...expandedRowKeys, [key]: !expandedRowKeys[key] });
  }

  function handleColumnClick(name: string) {
    setSortColumn((prev) => {
      const sortAscending = prev.name === name ? !prev.ascending : true;

      return {
        name: name,
        ascending: sortAscending,
      };
    });
  }

  function findSortColumnIdx() {
    return header.indexOf(sortColumn.name);
  }
}

interface HeaderColumnProps {
  columnName: string;
  sortColumn: SortColumn;

  onClick(columnName: string): void;
}

function HeaderColumn({ columnName, sortColumn, onClick }: HeaderColumnProps) {
  const sortIndicator = sortColumn.name === columnName ? <SortIndicator ascending={sortColumn.ascending} /> : null;

  return (
    <th key={columnName} onClick={() => onClick(columnName)}>
      <div className="column-with-indicator">
        {sortIndicator}
        <div>{columnName}</div>
      </div>
    </th>
  );
}

function sortData(data: TableCellType[][], idx: number, isAscending: boolean) {
  const sorted = [...data];
  sorted.sort((a, b) => {
    return compare(a, b) * (isAscending ? -1 : 1);
  });

  return sorted;

  function compare(a: any, b: any) {
    if (a[idx] < b[idx]) {
      return -1;
    } else if (a[idx] > b[idx]) {
      return 1;
    } else {
      return 0;
    }
  }
}

interface RowEntryProps {
  header: string[];
  row: TableRow;
  renderer?: TableValuesRenderer;
  hasExpand: boolean;
  isExpanded: boolean;
  toggleExpand(row: TableRow): void;
}

function RowEntry({ header, row, renderer, hasExpand, isExpanded, toggleExpand }: RowEntryProps) {
  return (
    <>
      <tr>
        {hasExpand && (
          <td className="webtau-sorted-table-row-toggle" onClick={() => toggleExpand(row)}>
            {isExpanded ? '-' : '+'}
          </td>
        )}
        {header.map((columnName, idx) => (
          <td key={idx}>{convertValue(idx, row[idx])}</td>
        ))}
      </tr>
      {isExpanded && (
        <tr>
          <td />
          <td colSpan={header.length}>{renderer?.expandedRowRenderer!(row)}</td>
        </tr>
      )}
    </>
  );

  function convertValue(idx: number, value: TableCellType) {
    if (!renderer?.cellRenderer) {
      return value;
    }

    return renderer.cellRenderer(header[idx], value);
  }
}

function SortIndicator({ ascending }: { ascending: boolean }) {
  const className = 'sort-indicator ' + (ascending ? 'ascending' : 'descending');
  return <div className={className}>&#8675;</div>;
}
