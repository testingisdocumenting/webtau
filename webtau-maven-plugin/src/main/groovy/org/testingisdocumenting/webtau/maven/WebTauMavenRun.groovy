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

package org.testingisdocumenting.webtau.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.shared.model.fileset.FileSet

@Mojo(name = "run")
class WebTauMavenRun extends AbstractMojo {
    @Parameter
    private FileSet tests

    @Parameter
    private String env

    @Parameter
    private String url

    @Parameter
    private String workingDir

    @Parameter(property = "skipTests", defaultValue = "false")
    protected boolean skipTests

    @Parameter(property = "maven.test.skip", defaultValue = "false")
    protected boolean skip

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        if (skipTests()) {
            getLog().info("Skipping webtau tests")
        } else {
            WebTauMaven.runTests(getLog(), tests, [env: env, url: url, workingDir: workingDir])
        }
    }

    private boolean skipTests() {
        return skipTests || skip
    }
}
