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

import deepNestedJson from './deepNestedJson';

const envVars = [
  {
    value:
      '/Users/mykolagolubyev/.sdkman/candidates/webtau/current:/Users/mykolagolubyev/.sdkman/candidates/java/current/bin:/Users/mykolagolubyev/work/apache-maven-3.6.3/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/mykolagolubyev/work/webtau-binary/dist:/usr/local/MacGPG2/bin:/Library/Apple/usr/bin',
    key: 'PATH',
  },
  {
    value: '5.11.0+644',
    key: 'SDKMAN_VERSION',
  },
  {
    value: '/Users/mykolagolubyev/.sdkman/candidates/webtau/current',
    key: 'WEBTAU_HOME',
  },
  {
    value: 'unix2003',
    key: 'COMMAND_MODE',
  },
  {
    value: '2.7',
    key: 'VERSIONER_PYTHON_VERSION',
  },
  {
    value: '/Users/mykolagolubyev/work/testingisdocumenting/webtau/webtau-feature-testing',
    key: 'PWD',
  },
  {
    value: '/Users/mykolagolubyev/.sdkman/candidates',
    key: 'SDKMAN_CANDIDATES_DIR',
  },
  {
    value: '/bin/zsh',
    key: 'SHELL',
  },
  {
    value: 'less',
    key: 'PAGER',
  },
  {
    value: 'Gxfxcxdxbxegedabagacad',
    key: 'LSCOLORS',
  },
  {
    value: 'https://api.sdkman.io/2',
    key: 'SDKMAN_CANDIDATES_API',
  },
  {
    value: '/',
    key: 'OLDPWD',
  },
  {
    value: 'DarwinX64',
    key: 'SDKMAN_PLATFORM',
  },
  {
    value: 'mykolagolubyev',
    key: 'USER',
  },
  {
    value: '/Users/mykolagolubyev/.oh-my-zsh',
    key: 'ZSH',
  },
  {
    value: '/var/folders/h1/w1phy75n6rn1w4hbjfwbw6vc0000gn/T/',
    key: 'TMPDIR',
  },
  {
    value: '/private/tmp/com.apple.launchd.ZYQWzSUbhE/Listeners',
    key: 'SSH_AUTH_SOCK',
  },
  {
    value: '0x0',
    key: 'XPC_FLAGS',
  },
  {
    value: '0x1F5:0x0:0x0',
    key: '__CF_USER_TEXT_ENCODING',
  },
  {
    value: '-R',
    key: 'LESS',
  },
  {
    value: 'en_US.UTF-8',
    key: 'LC_CTYPE',
  },
  {
    value: '/Users/mykolagolubyev',
    key: 'HOME',
  },
];

export const basicReport = {
  version: '1.40',
  name: 'my report name',
  nameUrl: '',
  summary: {
    total: 1,
    passed: 1,
    failed: 0,
    skipped: 0,
    errored: 0,
    startTime: 1547139662469,
    stopTime: 1547139662569,
    duration: 100,
  },
  config: [
    { key: 'env', value: 'dev', source: 'command line' },
    { key: 'url', value: 'https://base', source: 'config file' },
  ],
  envVars: envVars,
  tests: [
    {
      id: 'another.groovy-1',
      scenario: 'customer super read',
      status: 'Passed',
      fileName: 'scenarios/another.groovy',
      shortContainerId: 'another.groovy',
      startTime: 1547139662469,
      elapsedTime: 82,
      steps: [],
    },
    {
      id: 'another.groovy-2',
      scenario: 'another test',
      status: 'Passed',
      fileName: 'scenarios/base/another.groovy',
      shortContainerId: 'base/another.groovy',
      startTime: 1547139662469,
      elapsedTime: 82,
      steps: [],
    },
    {
      id: 'another.groovy-3',
      scenario: 'yet another test',
      status: 'Passed',
      fileName: 'scenarios/base/nested/another.groovy',
      shortContainerId: 'scenarios/base/nested/another.groovy',
      startTime: 1547139662469,
      elapsedTime: 82,
      steps: [],
    },
    {
      id: 'foo.groovy-1',
      scenario: 'some scenario',
      shortContainerId: 'foo.groovy',
      status: 'Passed',
      fileName: 'scenarios/foo.groovy',
      startTime: 1547139662469,
      elapsedTime: 82,
      steps: [],
    },
  ],
};

export const withCliDataReport = {
  version: '1.43',
  name: 'my cli tool name',
  nameUrl: 'http://github.com/testingisdocumenting/webtau',
  summary: {
    total: 1,
    passed: 0,
    failed: 1,
    skipped: 0,
    errored: 0,
    startTime: 1547139662469,
    stopTime: 1547139827670,
    duration: 300,
  },
  config: [{ key: 'env', value: 'dev', source: 'command line' }],
  envVars: envVars,
  tests: [
    {
      id: 'simpleRun.groovy-2',
      scenario: 'simple cli run',
      status: 'Passed',
      startTime: 1593810461246,
      elapsedTime: 126,
      fileName: 'scenarios/cli/simpleRun.groovy',
      shortContainerId: 'simpleRun.groovy',
      disabled: false,
      cliCalls: [
        {
          personaId: 'Alice',
          command: 'scripts/simple',
          out: 'welcome to my script\nversion: 12.43.2\n\n\n',
          err: '',
          exitCode: 0,
          outMatches: ['version: 12.43.2'],
          errMatches: [],
          startTime: 1593810461266,
          elapsedTime: 25,
          mismatches: [],
          errorMessage: null,
          config: {
            $MY_VAR: 'my-value',
          },
        },
      ],
      cliBackground: [
        {
          command: 'server',
          out: 'server running',
          err: 'not something',
          startTime: 1593810461266,
          config: {},
        },
      ],
      httpCalls: [],
      steps: [
        {
          message: [
            { type: 'action', value: 'ran cli command' },
            {
              type: 'stringValue',
              value: 'scripts/simple',
            },
          ],
          startTime: 1593810461266,
          elapsedTime: 106,
          children: [
            {
              message: [
                { type: 'id', value: 'process output' },
                {
                  type: 'matcher',
                  value: 'contains "version:"',
                },
              ],
              startTime: 1593810461311,
              elapsedTime: 55,
            },
            {
              message: [
                { type: 'id', value: 'exitCode' },
                {
                  type: 'matcher',
                  value:
                    'equals 0\nmatches:\n\nexitCode:   actual: 0 <java.lang.Integer>\n          expected: 0 <java.lang.Integer>',
                },
              ],
              startTime: 1593810461370,
              elapsedTime: 2,
            },
          ],
        },
      ],
      metadata: {},
    },
    {
      id: 'errorRuns.groovy-2',
      scenario: 'match error',
      status: 'Failed',
      startTime: 1560705863915,
      elapsedTime: 169,
      fileName: 'scenarios/cli/errorRuns.groovy',
      disabled: false,
      assertion:
        '\nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E',
      exceptionMessage:
        'java.lang.AssertionError: \nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E',
      failedCodeSnippets: [
        {
          filePath: 'scenarios/cli/errorRuns.groovy',
          lineNumbers: [6],
          snippet:
            "package scenarios.cli\n\nimport static org.testingisdocumenting.webtau.WebTauGroovyDsl.*\n\nscenario(\"match error\") {\n    cli.run('scripts/simple') {\n        output.should contain('versian:')\n    }\n}\n\nscenario(\"run error\") {\n    cli.run('scripts/simplo') {\n        output.should contain('version:')\n    }\n}",
        },
        {
          filePath: 'scenarios/cli/errorRuns.groovy',
          lineNumbers: [7],
          snippet:
            "package scenarios.cli\n\nimport static org.testingisdocumenting.webtau.WebTauGroovyDsl.*\n\nscenario(\"match error\") {\n    cli.run('scripts/simple') {\n        output.should contain('versian:')\n    }\n}\n\nscenario(\"run error\") {\n    cli.run('scripts/simplo') {\n        output.should contain('version:')\n    }\n}",
        },
      ],
      fullStackTrace:
        'java.lang.AssertionError: \nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E\n\tat org.testingisdocumenting.webtau.expectation.ActualValue.handleMismatch(ActualValue.java:84)\n\tat org.testingisdocumenting.webtau.expectation.ActualValue.should(ActualValue.java:39)\n\tat org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps.lambda$shouldStep$0(ValueMatcherExpectationSteps.java:33)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.lambda$toSupplier$2(TestStep.java:243)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.execute(TestStep.java:178)\n\tat org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps.executeStep(ValueMatcherExpectationSteps.java:72)\n\tat org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps.shouldStep(ValueMatcherExpectationSteps.java:31)\n\tat org.testingisdocumenting.webtau.cli.expectation.CliResultExpectations.should(CliResultExpectations.java:32)\n\tat org.testingisdocumenting.webtau.cli.expectation.CliResultExpectations$should.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:127)\n\tat scenarios.cli.errorRuns$_run_closure1$_closure3.doCall(errorRuns.groovy:7)\n\tat scenarios.cli.errorRuns$_run_closure1$_closure3.doCall(errorRuns.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.cli.CliExtension$1.handle(CliExtension.groovy:41)\n\tat org.testingisdocumenting.webtau.cli.Cli.lambda$run$1(Cli.java:70)\n\tat org.testingisdocumenting.webtau.cli.Cli.lambda$runAndValidate$5(Cli.java:126)\n\tat org.testingisdocumenting.webtau.expectation.ExpectationHandlers.withAdditionalHandler(ExpectationHandlers.java:42)\n\tat org.testingisdocumenting.webtau.cli.Cli.runAndValidate(Cli.java:125)\n\tat org.testingisdocumenting.webtau.cli.Cli.lambda$cliStep$3(Cli.java:87)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.lambda$toSupplier$2(TestStep.java:243)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.execute(TestStep.java:178)\n\tat org.testingisdocumenting.webtau.cli.Cli.cliStep(Cli.java:90)\n\tat org.testingisdocumenting.webtau.cli.Cli.run(Cli.java:69)\n\tat org.testingisdocumenting.webtau.cli.Cli.run(Cli.java:65)\n\tat org.testingisdocumenting.webtau.cli.Cli$run.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat org.testingisdocumenting.webtau.cli.CliExtension.run(CliExtension.groovy:27)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.metaclass.ReflectionMetaMethod.invoke(ReflectionMetaMethod.java:54)\n\tat org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod.invoke(NewInstanceMetaMethod.java:56)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoMetaMethodSiteNoUnwrapNoCoerce.invoke(PojoMetaMethodSite.java:246)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat scenarios.cli.errorRuns$_run_closure1.doCall(errorRuns.groovy:6)\n\tat scenarios.cli.errorRuns$_run_closure1.doCall(errorRuns.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat groovy.lang.Closure.call(Closure.java:405)\n\tat groovy.lang.Closure.run(Closure.java:492)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:56)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$_run_closure1.doCall(StandaloneTest.groovy:100)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$_run_closure1.doCall(StandaloneTest.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy12.get(Unknown Source)\n\tat org.testingisdocumenting.webtau.reporter.StepReporters.withAdditionalReporter(StepReporters.java:48)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrap.invoke(StaticMetaMethodSite.java:133)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.call(StaticMetaMethodSite.java:91)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest.run(StandaloneTest.groovy:96)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$run.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestIfNotTerminated(StandaloneTestRunner.groovy:192)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrapNoCoerce.invoke(PogoMetaMethodSite.java:190)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestAndNotifyListeners(StandaloneTestRunner.groovy:154)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:352)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.callCurrent(PogoMetaClassSite.java:68)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10$_closure15$_closure16.doCall(StandaloneTestRunner.groovy:184)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy14.accept(Unknown Source)\n\tat java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap.invoke(PojoMetaMethodSite.java:202)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:127)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10$_closure15.doCall(StandaloneTestRunner.groovy:183)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy14.accept(Unknown Source)\n\tat java.base/java.util.Iterator.forEachRemaining(Iterator.java:133)\n\tat java.base/java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)\n\tat java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:658)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap.invoke(PojoMetaMethodSite.java:202)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:127)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10.doCall(StandaloneTestRunner.groovy:181)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10.doCall(StandaloneTestRunner.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.resetAndWithListeners(StandaloneTestRunner.groovy:162)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$resetAndWithListeners$2.callCurrent(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestsFromStream(StandaloneTestRunner.groovy:169)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTests(StandaloneTestRunner.groovy:140)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$runTests$1.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.runTests(WebTauCliApp.groovy:173)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:352)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.callCurrent(PogoMetaClassSite.java:68)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:160)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp$_start_closure2.doCall(WebTauCliApp.groovy:89)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp$_start_closure2.doCall(WebTauCliApp.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.prepareTestsAndRun(WebTauCliApp.groovy:124)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrapNoCoerce.invoke(PogoMetaMethodSite.java:190)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:176)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.start(WebTauCliApp.groovy:88)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.call(PogoMetaMethodSite.java:70)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauEndToEndTestRunner.runCli(WebTauEndToEndTestRunner.groovy:85)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.call(PogoMetaMethodSite.java:70)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:143)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauCliFeaturesTest.runCli(WebTauCliFeaturesTest.groovy:41)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.invoke(StaticMetaMethodSite.java:46)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:102)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:55)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:196)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:216)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauCliFeaturesTest.error script run(WebTauCliFeaturesTest.groovy:37)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)\n\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\n\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)\n\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\n\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)\n\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)\n\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)\n\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)\n\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)\n\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)\n\tat org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)\n\tat org.junit.runners.ParentRunner.run(ParentRunner.java:363)\n\tat org.junit.runner.JUnitCore.run(JUnitCore.java:137)\n\tat com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)\n\tat com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)\n\tat com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)\n\tat com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)\n',
      shortStackTrace:
        'java.lang.AssertionError: \nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E\n\tat scenarios.cli.errorRuns$_run_closure1$_closure3.doCall(errorRuns.groovy:7)\n\tat scenarios.cli.errorRuns$_run_closure1$_closure3.doCall(errorRuns.groovy)\n\tat scenarios.cli.errorRuns$_run_closure1.doCall(errorRuns.groovy:6)\n\tat scenarios.cli.errorRuns$_run_closure1.doCall(errorRuns.groovy)',
      cliCalls: [
        {
          personaId: 'Alice',
          command: 'scripts/simple',
          out: 'welcome to my script\nversion: 12.43.2',
          err: '',
          exitCode: 0,
          outMatches: [],
          errMatches: [],
          startTime: 1560705863966,
          elapsedTime: 20,
          mismatches: [
            'process output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E',
          ],
          errorMessage: null,
          config: {},
        },
      ],
      httpCalls: [],
      steps: [
        {
          message: [
            { type: 'error', value: 'failed' },
            {
              type: 'action',
              value: 'running cli command ',
            },
            { type: 'stringValue', value: 'scripts/simple' },
            {
              type: 'delimiter',
              value: ':',
            },
            {
              type: 'error',
              value:
                '\nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E',
            },
          ],
          startTime: 1560705863966,
          elapsedTime: 115,
          children: [
            {
              message: [
                { type: 'error', value: 'failed' },
                {
                  type: 'action',
                  value: 'expecting',
                },
                { type: 'id', value: 'process output' },
                {
                  type: 'matcher',
                  value: 'to contain "versian:"',
                },
                { type: 'delimiter', value: ':' },
                {
                  type: 'error',
                  value:
                    '\nprocess output expect to contain "versian:"\nprocess output: mismatches:\n                \n                process output[0]:    actual string: welcome to my script\n                                   expected pattern: \\Qversian:\\E\n                process output[1]:    actual string: version: 12.43.2\n                                   expected pattern: \\Qversian:\\E',
                },
              ],
              startTime: 1560705864019,
              elapsedTime: 56,
            },
          ],
        },
      ],
    },
    {
      id: 'errorRuns.groovy-3',
      scenario: 'run error',
      status: 'Errored',
      startTime: 1560705864114,
      elapsedTime: 13757,
      fileName: 'scenarios/cli/errorRuns.groovy',
      disabled: false,
      assertion: null,
      exceptionMessage:
        'org.testingisdocumenting.webtau.cli.CliException: error during running \'scripts/simplo\'\nCaused by: java.io.IOException: Cannot run program "scripts/simplo": error=2, No such file or directory\nCaused by: java.io.IOException: error=2, No such file or directory',
      failedCodeSnippets: [
        {
          filePath: 'scenarios/cli/errorRuns.groovy',
          lineNumbers: [12],
          snippet:
            "package scenarios.cli\n\nimport static org.testingisdocumenting.webtau.WebTauGroovyDsl.*\n\nscenario(\"match error\") {\n    cli.run('scripts/simple') {\n        output.should contain('versian:')\n    }\n}\n\nscenario(\"run error\") {\n    cli.run('scripts/simplo') {\n        output.should contain('version:')\n    }\n}",
        },
      ],
      fullStackTrace:
        'org.testingisdocumenting.webtau.cli.CliException: error during running \'scripts/simplo\'\n\tat org.testingisdocumenting.webtau.cli.Cli.runAndValidate(Cli.java:133)\n\tat org.testingisdocumenting.webtau.cli.Cli.lambda$cliStep$3(Cli.java:87)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.lambda$toSupplier$2(TestStep.java:243)\n\tat org.testingisdocumenting.webtau.reporter.TestStep.execute(TestStep.java:178)\n\tat org.testingisdocumenting.webtau.cli.Cli.cliStep(Cli.java:90)\n\tat org.testingisdocumenting.webtau.cli.Cli.run(Cli.java:69)\n\tat org.testingisdocumenting.webtau.cli.Cli.run(Cli.java:65)\n\tat org.testingisdocumenting.webtau.cli.Cli$run.call(Unknown Source)\n\tat org.testingisdocumenting.webtau.cli.CliExtension.run(CliExtension.groovy:27)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.metaclass.ReflectionMetaMethod.invoke(ReflectionMetaMethod.java:54)\n\tat org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod.invoke(NewInstanceMetaMethod.java:56)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoMetaMethodSiteNoUnwrapNoCoerce.invoke(PojoMetaMethodSite.java:246)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat scenarios.cli.errorRuns$_run_closure2.doCall(errorRuns.groovy:12)\n\tat scenarios.cli.errorRuns$_run_closure2.doCall(errorRuns.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat groovy.lang.Closure.call(Closure.java:405)\n\tat groovy.lang.Closure.run(Closure.java:492)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:56)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$_run_closure1.doCall(StandaloneTest.groovy:100)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$_run_closure1.doCall(StandaloneTest.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy12.get(Unknown Source)\n\tat org.testingisdocumenting.webtau.reporter.StepReporters.withAdditionalReporter(StepReporters.java:48)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrap.invoke(StaticMetaMethodSite.java:133)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.call(StaticMetaMethodSite.java:91)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest.run(StandaloneTest.groovy:96)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTest$run.call(Unknown Source)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestIfNotTerminated(StandaloneTestRunner.groovy:192)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrapNoCoerce.invoke(PogoMetaMethodSite.java:190)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestAndNotifyListeners(StandaloneTestRunner.groovy:154)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:352)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.callCurrent(PogoMetaClassSite.java:68)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10$_closure15$_closure16.doCall(StandaloneTestRunner.groovy:184)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy14.accept(Unknown Source)\n\tat java.base/java.util.ArrayList.forEach(ArrayList.java:1540)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap.invoke(PojoMetaMethodSite.java:202)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:127)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10$_closure15.doCall(StandaloneTestRunner.groovy:183)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat groovy.lang.Closure.call(Closure.java:411)\n\tat org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:50)\n\tat org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:122)\n\tat com.sun.proxy.$Proxy14.accept(Unknown Source)\n\tat java.base/java.util.Iterator.forEachRemaining(Iterator.java:133)\n\tat java.base/java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1801)\n\tat java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:658)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap.invoke(PojoMetaMethodSite.java:202)\n\tat org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:55)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:127)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10.doCall(StandaloneTestRunner.groovy:181)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$_runTestsFromStream_closure10.doCall(StandaloneTestRunner.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.resetAndWithListeners(StandaloneTestRunner.groovy:162)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$resetAndWithListeners$2.callCurrent(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTestsFromStream(StandaloneTestRunner.groovy:169)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:168)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner.runTests(StandaloneTestRunner.groovy:140)\n\tat org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner$runTests$1.call(Unknown Source)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.runTests(WebTauCliApp.groovy:173)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:352)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.callCurrent(PogoMetaClassSite.java:68)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:160)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp$_start_closure2.doCall(WebTauCliApp.groovy:89)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp$_start_closure2.doCall(WebTauCliApp.groovy)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:264)\n\tat groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1041)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:41)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:119)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.prepareTestsAndRun(WebTauCliApp.groovy:124)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrapNoCoerce.invoke(PogoMetaMethodSite.java:190)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.callCurrent(PogoMetaMethodSite.java:58)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallCurrent(CallSiteArray.java:51)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:156)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callCurrent(AbstractCallSite.java:176)\n\tat org.testingisdocumenting.webtau.cli.WebTauCliApp.start(WebTauCliApp.groovy:88)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.call(PogoMetaMethodSite.java:70)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauEndToEndTestRunner.runCli(WebTauEndToEndTestRunner.groovy:85)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.runtime.callsite.PlainObjectMetaMethodSite.doInvoke(PlainObjectMetaMethodSite.java:43)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite$PogoCachedMethodSiteNoUnwrap.invoke(PogoMetaMethodSite.java:179)\n\tat org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite.call(PogoMetaMethodSite.java:70)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:143)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauCliFeaturesTest.runCli(WebTauCliFeaturesTest.groovy:41)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:104)\n\tat groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:326)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.invoke(StaticMetaMethodSite.java:46)\n\tat org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:102)\n\tat org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:55)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:196)\n\tat org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:216)\n\tat org.testingisdocumenting.webtau.featuretesting.WebTauCliFeaturesTest.error script run(WebTauCliFeaturesTest.groovy:37)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.base/java.lang.reflect.Method.invoke(Method.java:567)\n\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)\n\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\n\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)\n\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\n\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)\n\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)\n\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)\n\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)\n\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)\n\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)\n\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)\n\tat org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)\n\tat org.junit.runners.ParentRunner.run(ParentRunner.java:363)\n\tat org.junit.runner.JUnitCore.run(JUnitCore.java:137)\n\tat com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)\n\tat com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)\n\tat com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)\n\tat com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)\nCaused by: java.io.IOException: Cannot run program "scripts/simplo": error=2, No such file or directory\n\tat java.base/java.lang.ProcessBuilder.start(ProcessBuilder.java:1128)\n\tat java.base/java.lang.ProcessBuilder.start(ProcessBuilder.java:1071)\n\tat org.testingisdocumenting.webtau.cli.ProcessUtils.run(ProcessUtils.java:33)\n\tat org.testingisdocumenting.webtau.cli.Cli.runAndValidate(Cli.java:103)\n\t... 269 more\nCaused by: java.io.IOException: error=2, No such file or directory\n\tat java.base/java.lang.ProcessImpl.forkAndExec(Native Method)\n\tat java.base/java.lang.ProcessImpl.<init>(ProcessImpl.java:340)\n\tat java.base/java.lang.ProcessImpl.start(ProcessImpl.java:271)\n\tat java.base/java.lang.ProcessBuilder.start(ProcessBuilder.java:1107)\n\t... 272 more\n',
      shortStackTrace:
        'org.testingisdocumenting.webtau.cli.CliException: error during running \'scripts/simplo\'\n\tat scenarios.cli.errorRuns$_run_closure2.doCall(errorRuns.groovy:12)\n\tat scenarios.cli.errorRuns$_run_closure2.doCall(errorRuns.groovy)\nCaused by: java.io.IOException: Cannot run program "scripts/simplo": error=2, No such file or directory\nCaused by: java.io.IOException: error=2, No such file or directory',
      cliCalls: [
        {
          personaId: 'Alice',
          command: 'scripts/simplo',
          out: '',
          err: '',
          exitCode: null,
          outMatches: [],
          errMatches: [],
          startTime: 0,
          elapsedTime: 0,
          mismatches: [],
          errorMessage: 'Cannot run program "scripts/simplo": error=2, No such file or directory',
          config: {},
        },
      ],
      httpCalls: [],
      steps: [
        {
          message: [
            { type: 'error', value: 'failed' },
            {
              type: 'action',
              value: 'running cli command ',
            },
            { type: 'stringValue', value: 'scripts/simplo' },
            {
              type: 'delimiter',
              value: ':',
            },
            { type: 'error', value: "error during running 'scripts/simplo'" },
          ],
          startTime: 1560705864115,
          elapsedTime: 13734,
        },
      ],
    },
  ],
  openApiSkippedOperations: [],
  openApiCoveredOperations: [
    {
      method: 'POST',
      url: '/customers/',
    },
    {
      method: 'DELETE',
      url: '/customers/{customerId}',
    },
    {
      method: 'GET',
      url: '/customers/{customerId}',
    },
    {
      method: 'PUT',
      url: '/customers/{customerId}',
    },
  ],
};

export const withRestDataReport = {
  version: '1.4',
  name: 'my report name',
  summary: {
    total: 5,
    passed: 3,
    failed: 1,
    skipped: 1,
    errored: 0,
    startTime: 1547139662469,
    stopTime: 1547139827670,
    duration: 165201,
  },
  config: [
    { key: 'env', value: 'dev', source: 'command line' },
    { key: 'url', value: 'https://blahlong-url-maybe-need-shortening.com/v1', source: 'config file' },
  ],
  envVars: envVars,
  tests: [
    {
      id: 'another.groovy-1',
      className: 'com.example.tests.rest.AnotherIT',
      scenario: 'customer super read',
      status: 'Passed',
      fileName: 'rest\\springboot\\anotherTest.groovy',
      startTime: 1534456916484,
      elapsedTime: 32,
      httpCalls: [
        {
          id: '7',
          method: 'GET',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916484,
          elapsedTime: 22,
          errorMessage: null,
          mismatches: [],
          warnings: ['path http://localhost:8080/customers is not found in OpenAPI spec'],
          responseType: 'application/json;charset=UTF-8',
          responseStatusCode: 200,
          requestHeader: [],
          responseHeader: [
            {
              key: 'Transfer-Encoding-LongerKeyLongishSufixLongerKeyLongishSufixLongerKeyLongishSufix',
              value: 'chunked',
            },
            {
              key: null,
              value: 'HTTP/1.1 201',
            },
            {
              key: 'Date',
              value: 'Tue, 23 Oct 2018 11:01:26 GMT',
            },
            {
              key: 'Content-Type',
              value: 'application/json;charset=UTF-8',
            },
            {
              key: 'Location',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          responseBody:
            '{\n  "id" : 1,\n  "firstName" : "FN",\n  "lastName" : "LN",\n  "_links" : {\n    "self" : {\n      "href" : "http://localhost:8080/customers/1"\n    },\n    "customer" : {\n      "href" : "http://localhost:8080/customers/1"\n    }\n  }\n}',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: ['root.firstName', 'root.lastName'],
          },
        },
      ],
      steps: [
        {
          elapsedTime: 200,
          message: [
            {
              type: 'action',
              value: 'executed HTTP GET',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'body',
                },
                {
                  type: 'matcher',
                  value:
                    'equals {firstName=FN, lastName=LN}\nmatches:\n\nbody.firstName:   actual: "FN" <java.lang.String>\n                expected: "FN" <java.lang.String>\nbody.lastName:   actual: "LN" <java.lang.String>\n               expected: "LN" <java.lang.String>',
                },
              ],
            },
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      id: 'customerCrudSeparated.groovy-1',
      scenario: 'customer create',
      status: 'Passed',
      startTime: 1534456916484,
      elapsedTime: 283,
      className: 'com.example.tests.rest.CustomerCrudSeparatedIT',
      fileName: 'rest/springboot/customerCrudSeparated.groovy',
      httpCalls: [
        {
          id: '6',
          method: 'POST',
          url:
            'http://localhost:8080/customers/long-url/url-sub-part/and-again/long-url/url-sub-part/and-again/long-url/url-sub-part/and-again/long-url/url-sub-part/and-again/long-url/url-sub-part/and-again/long-url/url-sub-part/and-again',
          startTime: 1534456916484,
          elapsedTime: 283,
          errorMessage: null,
          mismatches: [],
          responseType: 'application/json;charset=UTF-8',
          responseStatusCode: 201,
          responseBody:
            '{\n  "id" : 1,\n  "FirstNameVeryLongNameFirstNameVeryLongNameFirstNameVeryLongNameFirstNameVeryLongNameFirstNameVeryLongName" : "FN",\n  "lastName" : "LN",\n  "_links" : {\n    "self" : {\n      "href" : "http://localhost:8080/customers/1"\n    },\n    "customer" : {\n      "href" : "http://localhost:8080/customers/1"\n    }\n  }\n}',
          requestType: 'application/json',
          requestBody: JSON.stringify(deepNestedJson),
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: [],
          },
        },
      ],
      steps: [
        {
          elapsedTime: 300,
          message: [
            {
              type: 'action',
              value: 'executed HTTP POST',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 201\nmatches:\n\nheader.statusCode:   actual: 201 <java.lang.Integer>\n                   expected: 201 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      id: 'another.groovy-2',
      scenario: 'customer read',
      status: 'Passed',
      fileName: 'rest\\springboot\\anotherTest.groovy',
      className: 'com.example.tests.rest.AnotherIT',
      startTime: 1534456916484,
      elapsedTime: 22,
      httpCalls: [
        {
          id: '5',
          method: 'GET',
          url: 'http://localhost:8080/customers/3',
          startTime: 1534456916484,
          elapsedTime: 22,
          errorMessage: null,
          mismatches: [],
          responseType: 'application/json;charset=UTF-8',
          responseStatusCode: 200,
          responseBody:
            '{\n  "id" : 1,\n  "firstName" : "FN",\n  "lastName" : "LN",\n  "_links" : {\n    "self" : {\n      "href" : "http://localhost:8080/customers/1"\n    },\n    "customer" : {\n      "href" : "http://localhost:8080/customers/1"\n    }\n  }\n}',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: ['root.firstName', 'root.lastName'],
          },
        },
      ],
      steps: [
        {
          elapsedTime: 350,
          message: [
            {
              type: 'action',
              value: 'executed HTTP GET',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'body',
                },
                {
                  type: 'matcher',
                  value:
                    'equals {firstName=FN, lastName=LN}\nmatches:\n\nbody.firstName:   actual: "FN" <java.lang.String>\n                expected: "FN" <java.lang.String>\nbody.lastName:   actual: "LN" <java.lang.String>\n               expected: "LN" <java.lang.String>',
                },
              ],
            },
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      id: 'customerCrudSeparated.groovy-3',
      className: 'com.example.tests.rest.CustomerCrudSeparatedIT',
      scenario: 'customer update multi persona',
      status: 'Passed',
      startTime: 1534456916384,
      elapsedTime: 32,
      fileName: 'rest/springboot/customerCrudSeparated.groovy',
      httpCalls: [
        {
          id: '1',
          method: 'PUT',
          personaId: 'Admin',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456927154,
          elapsedTime: 29,
          errorMessage: null,
          mismatches: [],
          responseType: 'application/json;charset=UTF-8',
          responseStatusCode: 200,
          responseBody:
            '{\n  "id" : 1,\n  "firstName" : "FN",\n  "lastName" : "NLN",\n  "_links" : {\n    "self" : {\n      "href" : "http://localhost:8080/customers/1"\n    },\n    "customer" : {\n      "href" : "http://localhost:8080/customers/1"\n    }\n  }\n}',
          requestType: 'application/json',
          requestBody: '{"firstName":"FN","lastName":"NLN"}',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: ['root.lastName'],
          },
        },
        {
          id: '2',
          method: 'GET',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916394,
          elapsedTime: 6,
          errorMessage: null,
          mismatches: [],
          responseType: 'application/json;charset=UTF-8',
          responseStatusCode: 200,
          responseBody:
            '{\n  "id" : 1,\n  "firstName" : "FN",\n  "lastName" : "NLN",\n  "_links" : {\n    "self" : {\n      "href" : "http://localhost:8080/customers/1"\n    },\n    "customer" : {\n      "href" : "http://localhost:8080/customers/1"\n    }\n  }\n}',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: ['root.lastName'],
          },
        },
      ],
      steps: [
        {
          elapsedTime: 450,
          personaId: 'Admin',
          message: [
            {
              type: 'action',
              value: 'executed HTTP PUT',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
            {
              type: 'string',
              value:
                'long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap long text value to cause wrap ',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'body.lastName',
                },
                {
                  type: 'matcher',
                  value:
                    'equals "NLN"\nmatches:\n\nbody.lastName:   actual: "NLN" <java.lang.String>\n               expected: "NLN" <java.lang.String>',
                },
              ],
            },
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
        {
          elapsedTime: 150,
          message: [
            {
              type: 'action',
              value: 'executed HTTP GET',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'body.lastName',
                },
                {
                  type: 'matcher',
                  value:
                    'equals "NLN"\nmatches:\n\nbody.lastName:   actual: "NLN" <java.lang.String>\n               expected: "NLN" <java.lang.String>',
                },
              ],
            },
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      id: 'customerCrudSeparated.groovy-4',
      className: 'com.example.tests.rest.CustomerCrudSeparatedIT',
      scenario: 'customer delete',
      status: 'Failed',
      fileName: 'rest/springboot/customerCrudSeparated.groovy',
      startTime: 1534456916784,
      elapsedTime: 32,
      exceptionMessage: 'error calling something',
      httpCalls: [
        {
          id: '3',
          method: 'DELETE',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916784,
          elapsedTime: 12,
          errorMessage: null,
          mismatches: [],
          responseType: '',
          responseStatusCode: 204,
          responseBody: '',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: [],
          },
        },
        {
          id: '4',
          method: 'GET',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916684,
          elapsedTime: 9,
          mismatches: [],
          responseType: '',
          responseStatusCode: 404,
          responseBody: '',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: [],
          },
          errorMessage:
            'long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line',
        },
      ],
      steps: [
        {
          elapsedTime: 200,
          message: [
            {
              type: 'action',
              value: 'executed HTTP DELETE',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 204\nmatches:\n\nheader.statusCode:   actual: 204 <java.lang.Integer>\n                   expected: 204 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
        {
          elapsedTime: 150,
          message: [
            {
              type: 'action',
              value: 'executed HTTP GET',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 404\nmatches:\n\nheader.statusCode:   actual: 404 <java.lang.Integer>\n                   expected: 404 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      id: 'customerCrudSeparated.groovy-5',
      className: 'com.example.tests.rest.CustomerCrudSeparatedIT',
      scenario: 'customer extra delete',
      status: 'Skipped',
      fileName: 'rest/springboot/customerCrudSeparated.groovy',
      startTime: 1534456916784,
      elapsedTime: 32,
      exceptionMessage: 'error calling something',
      httpCalls: [
        {
          id: '3',
          method: 'DELETE',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916784,
          elapsedTime: 12,
          errorMessage: null,
          mismatches: [],
          responseType: '',
          responseStatusCode: 204,
          responseBody: '',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: [],
          },
        },
        {
          id: '4',
          method: 'GET',
          url: 'http://localhost:8080/customers/1',
          startTime: 1534456916684,
          elapsedTime: 9,
          mismatches: [],
          responseType: '',
          responseStatusCode: 404,
          responseBody: '',
          responseBodyChecks: {
            failedPaths: [],
            passedPaths: [],
          },
          errorMessage:
            'long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line long long long line line line',
        },
      ],
      steps: [
        {
          elapsedTime: 200,
          message: [
            {
              type: 'action',
              value: 'executed HTTP DELETE',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 204\nmatches:\n\nheader.statusCode:   actual: 204 <java.lang.Integer>\n                   expected: 204 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
        {
          elapsedTime: 150,
          message: [
            {
              type: 'action',
              value: 'executed HTTP GET',
            },
            {
              type: 'url',
              value: 'http://localhost:8080/customers/1',
            },
          ],
          children: [
            {
              elapsedTime: 50,
              message: [
                {
                  type: 'id',
                  value: 'header.statusCode',
                },
                {
                  type: 'matcher',
                  value:
                    'equals 404\nmatches:\n\nheader.statusCode:   actual: 404 <java.lang.Integer>\n                   expected: 404 <java.lang.Integer>',
                },
              ],
            },
          ],
        },
      ],
    },
  ],
  httpPerformance: {
    aggregated: [
      {
        p50ms: 7,
        p99ms: 8,
        p20ms: 6,
        groupId: 'GET /{customerId}',
        maxMs: 8,
        p95ms: 8,
        count: 2,
        minMs: 6,
        p80ms: 8,
        averageMs: 7,
      },
      {
        p50ms: 10,
        p99ms: 10,
        p20ms: 10,
        groupId: 'PUT /{customerId}',
        maxMs: 10,
        p95ms: 10,
        count: 1,
        minMs: 10,
        p80ms: 10,
        averageMs: 10,
      },
      {
        p50ms: 17,
        p99ms: 17,
        p20ms: 17,
        groupId: 'POST ',
        maxMs: 17,
        p95ms: 17,
        count: 1,
        minMs: 17,
        p80ms: 17,
        averageMs: 17,
      },
    ],
  },
  openApiSkippedOperations: [],
  openApiCoveredOperations: [
    {
      method: 'POST',
      url: '/customers/',
    },
    {
      method: 'DELETE',
      url: '/customers/{customerId}',
    },
    {
      method: 'GET',
      url: '/customers/{customerId}',
    },
    {
      method: 'PUT',
      url: '/customers/{customerId}',
    },
  ],
};
