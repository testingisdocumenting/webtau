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

import React, { useState } from 'react';

import './WebtauThemeSwitcher.css';

export function WebtauThemeSwitcher() {
  const [label, setLabel] = useState(generateLabel);

  return (
    <div className="webtau-theme-selection" onClick={toggleTheme}>
      {label}
    </div>
  );

  function generateLabel() {
    // @ts-ignore
    const themeName = window.webtauTheme.name;

    const newThemeName = themeName === 'webtau-light' ? 'dark' : 'light';
    return `click to switch to ${newThemeName} theme`;
  }

  function toggleTheme() {
    // @ts-ignore
    window.webtauTheme.toggle();
    setLabel(generateLabel);
  }
}
