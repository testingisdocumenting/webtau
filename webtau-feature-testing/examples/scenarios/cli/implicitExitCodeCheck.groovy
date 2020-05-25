/*
 * Copyright 2020 webtau maintainers
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

package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('implicitly validates that exit code is zero') {
    cli.run('scripts/exit-code 126') {
        output.should contain('script will exit')
    }
}

scenario('implicitly validates that exit code is zero when no validation block is provided') {
    cli.run('scripts/exit-code 126')
}

scenario('does not implicitly validates exit code if explicit validation is present') {
    cli.run('scripts/exit-code 126') {
        output.should contain('script will exit')
        exitCode.should == 126
    }
}