/*
 * Copyright 2021 webtau maintainers
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

import '../widgets/Table.css';
import './EnvVarsTable.css';

interface Props {
  report: any;
}

export function EnvVarsTable({ report }: Props) {
  return (
    <table className="env-vars-table table">
      <thead>
        <tr>
          <th>Key</th>
          <th>Value</th>
        </tr>
      </thead>
      <tbody>
        {report.envVars.map((e: any) => (
          <EnvVarEntry key={e.key} label={e.key} value={e.value} />
        ))}
      </tbody>
    </table>
  );
}

interface EnvVarProp {
  label: string;
  value: string;
}

function EnvVarEntry({ label, value }: EnvVarProp) {
  return (
    <tr>
      <td>{label}</td>
      <td>{value}</td>
    </tr>
  );
}
