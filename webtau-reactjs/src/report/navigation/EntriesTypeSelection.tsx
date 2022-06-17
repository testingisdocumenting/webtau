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

import React from 'react';

import NavigationEntriesType from './NavigationEntriesType';

import './EntriesTypeSelection.css';

interface Props {
  reportName: string;
  reportNameUrl: string;
  selectedType: string;
  onSelect(testId: string): void;
}

export function EntriesTypeSelection({ reportName, reportNameUrl, selectedType, onSelect }: Props) {
  reportNameUrl = reportNameUrl || "https://github.com/testingisdocumenting/webtau"

  return (
    <div className="entries-type-selection">
      <div className="webtau-report-name">
        <a href={reportNameUrl} target="_blank" rel="noopener noreferrer">
          {reportName}
        </a>
      </div>
      <div className="selection-area">
        <EntryType selectedType={selectedType} type={NavigationEntriesType.TESTS} label="tests" onSelect={onSelect} />
        <EntryType
          selectedType={selectedType}
          type={NavigationEntriesType.HTTP_CALLS}
          label="http calls"
          onSelect={onSelect}
        />
      </div>
    </div>
  );
}

interface EntryTypeProps {
  selectedType: string;
  type: string;
  label: string;
  onSelect(type: string): void;
}

function EntryType({ selectedType, type, label, onSelect }: EntryTypeProps) {
  const isSelected = selectedType === type;
  const className = 'entry-type entry-type-' + type + (isSelected ? ' selected' : '');

  return (
    <div className={className} onClick={() => onSelect(type)}>
      <div>{label}</div>
    </div>
  );
}
