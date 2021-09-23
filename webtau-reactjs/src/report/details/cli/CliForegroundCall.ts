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

export interface CliForegroundCall {
  command: string;
  elapsedTime: number;
  err: string;
  errMatches: string[];
  errorMessage: string;
  exitCode: number;
  mismatches: string[];
  out: string;
  outMatches: string[];
  startTime: number;
  config: { [key: string]: object };
}

export interface CliBackgroundCall {
  command: string;
  err: string;
  out: string;
  startTime: number;
  config: { [key: string]: object };
}
