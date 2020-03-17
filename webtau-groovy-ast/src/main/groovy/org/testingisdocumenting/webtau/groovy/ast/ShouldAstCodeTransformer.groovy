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

package org.testingisdocumenting.webtau.groovy.ast

import org.testingisdocumenting.webtau.WebTauCore
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.control.SourceUnit

@PackageScope
@CompileStatic
class ShouldAstCodeTransformer extends ClassCodeExpressionTransformer {
    @Override
    protected SourceUnit getSourceUnit() {
        return sourceUnit
    }

    @Override
    Expression transform(Expression exp) {
        if (exp instanceof ClosureExpression) {
            visitClosureExpression(exp)
        }

        if (!(exp instanceof BinaryExpression)) {
            return super.transform(exp)
        }

        BinaryExpression binaryExpression = exp as BinaryExpression
        if (!(binaryExpression.leftExpression instanceof PropertyExpression)) {
            return super.transform(exp)
        }

        if (!isSupportedBinaryOperation(binaryExpression)) {
            return super.transform(exp)
        }

        PropertyExpression leftExpression = binaryExpression.leftExpression as PropertyExpression
        if (!isShouldOrWait(leftExpression)) {
            return super.transform(exp)
        }

        return createMatcherValidationCall(leftExpression,
                binaryExpression.operation.text,
                binaryExpression.rightExpression)
    }

    private static boolean isSupportedBinaryOperation(BinaryExpression binaryExpression) {
        return matcherMethodForOperation(binaryExpression.operation.text) != null
    }

    private static boolean isShouldOrWait(PropertyExpression leftExpression) {
        return leftExpression.propertyAsString in [
                'should',
                'shouldBe',
                'shouldNot',
                'shouldNotBe',
                'waitTo',
                'waitToBe',
                'waitToNot',
                'waitToNotBe',
        ]
    }

    private static MethodCallExpression createMatcherValidationCall(PropertyExpression leftExpression,
                                                                    String operationText,
                                                                    Expression rightExpression) {
        new MethodCallExpression(leftExpression.objectExpression, leftExpression.property,
            new ArgumentListExpression(new StaticMethodCallExpression(
                new ClassNode(WebTauCore), matcherMethodForOperation(operationText), rightExpression)))
    }

    static String matcherMethodForOperation(String operationText) {
        switch (operationText) {
            case '==':
                return 'equal'
            case '!=':
                return 'notEqual'
            case '>':
                return 'greaterThan'
            case '>=':
                return 'greaterThanOrEqual'
            case '<':
                return 'lessThan'
            case '<=':
                return 'lessThanOrEqual'
            default:
                return null
        }
    }
}
