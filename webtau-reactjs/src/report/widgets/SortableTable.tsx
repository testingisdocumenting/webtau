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

export type CellType = string | number;

interface Props {
  header: string[];
  data: CellType[][];
  className?: string;
  renderConverter?: (columnName: string, value: CellType) => JSX.Element;
}

interface SortColumn {
  name: string;
  ascending: boolean;
}

export function SortableTable({ className, header, data, renderConverter }: Props) {
  const [sortColumn, setSortColumn] = useState<SortColumn>({ name: '', ascending: false });

  const sortedData = sortColumn.name ? sortData(data, findSortColumnIdx(), sortColumn.ascending) : data;

  const fullClassName = 'webtau-sortable-table table' + (className ? ' ' + className : '');

  return (
    <table className={fullClassName}>
      <thead>
        <tr>
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
        {sortedData.map((row, idx) => (
          <RowEntry key={idx} header={header} row={row} renderConverter={renderConverter} />
        ))}
      </tbody>
    </table>
  );

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

function sortData(data: CellType[][], idx: number, isAscending: boolean) {
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
  row: CellType[];
  renderConverter?: (columnName: string, value: CellType) => JSX.Element;
}

function RowEntry({ header, row, renderConverter }: RowEntryProps) {
  return (
    <tr>
      {row.map((value, idx) => (
        <td key={idx}>{convertValue(idx, value)}</td>
      ))}
    </tr>
  );

  function convertValue(idx: number, value: CellType) {
    if (!renderConverter) {
      return value;
    }

    return renderConverter(header[idx], value);
  }
}

function SortIndicator({ ascending }: { ascending: boolean }) {
  const className = 'sort-indicator ' + (ascending ? 'ascending' : 'descending');
  return <div className={className}>&#8675;</div>;
}
