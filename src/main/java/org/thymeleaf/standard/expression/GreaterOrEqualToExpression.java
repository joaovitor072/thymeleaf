/*
 * =============================================================================
 * 
 *   Copyright (c) 2011, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.standard.expression;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.ExpressionEvaluationException;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.ObjectUtils;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class GreaterOrEqualToExpression extends GreaterLesserExpression {

    private static final long serialVersionUID = 4318966518910979010L;

    private static final Logger logger = LoggerFactory.getLogger(GreaterOrEqualToExpression.class);

    
    GreaterOrEqualToExpression(final Expression left, final Expression right) {
        super(left, right);
    }
    
    
    @Override
    public String getStringRepresentation() {
        return getStringRepresentation(GREATER_OR_EQUAL_TO_OPERATOR);
    }
    
    
    

    
    @SuppressWarnings("unchecked")
    static Object executeGreaterOrEqualTo(final Arguments arguments, final TemplateResolution templateResolution, 
            final GreaterOrEqualToExpression expression, final IStandardExpressionEvaluator expressionEvaluator) {
        
        Object leftValue = 
            Expression.execute(arguments, templateResolution, expression.getLeft(), expressionEvaluator);

        Object rightValue = 
            Expression.execute(arguments, templateResolution, expression.getRight(), expressionEvaluator);

        leftValue = LiteralValue.unwrap(leftValue);
        rightValue = LiteralValue.unwrap(rightValue);

        Boolean result = null; 
        
        final BigDecimal leftNumberValue = ObjectUtils.evaluateAsNumber(leftValue); 
        final BigDecimal rightNumberValue = ObjectUtils.evaluateAsNumber(rightValue);
        
        if (leftNumberValue != null && rightNumberValue != null) {
            result = Boolean.valueOf(leftNumberValue.compareTo(rightNumberValue) != -1);
        } else {
            if (leftValue != null && rightValue != null &&
                    leftValue.getClass().equals(rightValue.getClass()) && 
                    Comparable.class.isAssignableFrom(leftValue.getClass())) {
                result = Boolean.valueOf(((Comparable<Object>)leftValue).compareTo(rightValue) != -1);
            } else {
                throw new ExpressionEvaluationException(
                        "Cannot execute GREATER OR EQUAL TO from Expression \"" + 
                        expression.getStringRepresentation() + "\". Left is \"" + 
                        leftValue + "\", right is \"" + rightValue + "\"");
            }
        }
        
        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] Evaluating GREATER OR EQUAL TO expression: \"{}\". Left is \"{}\", right is \"{}\". Result is \"{}\"", 
                    new Object[] {TemplateEngine.threadIndex(), expression.getStringRepresentation(), leftValue, rightValue, result});
        }
        
        return result; 
        
    }

    
}
