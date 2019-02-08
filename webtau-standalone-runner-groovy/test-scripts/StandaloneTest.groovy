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

scenario ("""# first header
optionally split into multiple lines and has a header
""") {
    throw new RuntimeException("error on purpose")
}

scenario("""# second header
optionally split into multiple lines and has a header
""") {
    def a = 10
    a.should == 11
}

scenario("""# third header
optionally split into multiple lines and has a header
""") {
    println "hello"
}
