/*
 * Copyright 2020 webtau maintainers
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

import React, { useState } from 'react';

import { Card } from '../../widgets/Card';

import { StepMessage } from './StepMessage';
import { StepTime } from './StepTime';

import { WebTauStep, WebTauStepInput, WebTauStepOutput } from '../../WebTauTest';

import { StepInputOutputKeyValue } from './StepInputOutputKeyValue';

import './Step.css';

interface Props {
  step: WebTauStep;
  isTopLevel?: boolean;
}

export function Step({ step, isTopLevel }: Props) {
  const [collapsed, setCollapsed] = useState(true);

  const children = step.children ? step.children.map((c, idx) => <Step key={idx} step={c} />) : null;

  const ParentContainer: any = isTopLevel ? Card : 'div';
  return (
    <ParentContainer className="step">
      <div className="message-parts">
        {step.personaId ? <div className="persona-id">{step.personaId}</div> : <div />}

        <StepMessage message={step.message} />
        {renderMoreToggle()}
        <StepTime millis={step.elapsedTime} />
      </div>

      {(step.input || step.output) && renderStepInputOutput(step.input, step.output)}

      {children && !collapsed && <div className="steps-children">{children}</div>}
    </ParentContainer>
  );

  function renderStepInputOutput(input?: WebTauStepInput, output?: WebTauStepOutput) {
    if (input?.type === 'WebTauStepInputKeyValue' || output?.type === 'WebTauStepOutputKeyValue') {
      return <StepInputOutputKeyValue inputData={input?.data} outputData={output?.data} />;
    }
  }

  function renderMoreToggle() {
    if (!step.children || !isTopLevel || !collapsed) {
      return <div />;
    }

    return (
      <div className="show-children" onClick={showChildren}>
        ...
      </div>
    );
  }

  function showChildren() {
    setCollapsed(false);
  }
}
