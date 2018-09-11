/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.maven

import com.twosigma.webtau.cli.WebTauCliApp
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.shared.model.fileset.FileSet
import org.apache.maven.shared.model.fileset.util.FileSetManager

@Mojo(name = "run")
class WebTauMavenRunner extends AbstractMojo {
    @Parameter
    private FileSet tests

    @Parameter
    private String env

    @Parameter
    private String url

    @Parameter
    private String workingDir

    @Parameter( property = "skipTests", defaultValue = "false" )
    protected boolean skipTests

    @Parameter( property = "maven.test.skip", defaultValue = "false" )
    protected boolean skip

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        if (skipTests()) {
            getLog().info("Skipping webtau tests")
        } else {
            runTests()
        }
    }

    private void runTests() {
        def fileSetManager = new FileSetManager()
        def files = fileSetManager.getIncludedFiles(tests) as List

        getLog().info("test files:\n    " + files.join("\n    "))

        def args = buildArgs([env: env, url: url, workingDir: workingDir])
        args.addAll(files)

        def cli = new WebTauCliApp(args as String[])
        cli.start(true)

        if (cli.problemCount > 0) {
            throw new MojoFailureException("check failed tests")
        }
    }

    static List<String> buildArgs(Map params) {
        return params.entrySet()
                .findAll { e -> e.value != null}
                .collect { e -> "--${e.key}=${e.value}"}
    }

    private boolean skipTests() {
        return skipTests || skip
    }
}
