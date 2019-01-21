/*
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

import {decompressAndDecodeReportData} from './reportDataCompression'

describe('reportDataCompression.js', () => {
    it('should uncompress base64 lz4 compressed content', () => {
        const result =
            decompressAndDecodeReportData('9AZ7dGVzdDogW3tpZDogImlkMSJ9LCANABgyDQBrMyJ9XSwgLwAYNCIAGDUNAFA2In1dfQ==', 94)

        expect(result).toEqual('{test: [{id: "id1"}, {id: "id2"}, {id: "id3"}], test: [{id: "id4"}, {id: "id5"}, {id: "id6"}]}')
    })
})