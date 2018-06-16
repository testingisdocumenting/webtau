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

package com.twosigma.webtau.groovy.ast

import com.twosigma.webtau.Ddjt
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

        return createEqualCall(leftExpression, binaryExpression.rightExpression)
    }

    private static boolean isSupportedBinaryOperation(BinaryExpression binaryExpression) {
        return binaryExpression.operation.text == '=='
    }

    private static boolean isShouldOrWait(PropertyExpression leftExpression) {
        return leftExpression.propertyAsString in ['should', 'shouldNot', 'waitTo', 'waitToNot']
    }

    private static MethodCallExpression createEqualCall(PropertyExpression leftExpression, Expression rightExpression) {
        new MethodCallExpression(leftExpression.objectExpression, leftExpression.property,
            new ArgumentListExpression(new StaticMethodCallExpression(
                new ClassNode(Ddjt), 'equal', rightExpression)))
    }
}
