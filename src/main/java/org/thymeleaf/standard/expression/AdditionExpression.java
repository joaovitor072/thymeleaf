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
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.ObjectUtils;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class AdditionExpression extends AdditionSubtractionExpression {
    
    private static final long serialVersionUID = -971366486450425605L;


    private static final Logger logger = LoggerFactory.getLogger(AdditionExpression.class);


    
    AdditionExpression(final Expression left, final Expression right) {
        super(left, right);
    }
    

    
    @Override
    public String getStringRepresentation() {
        return getStringRepresentation(ADDITION_OPERATOR);
    }
    
    
    
    
    
    static Object executeAddition(final Arguments arguments, final TemplateResolution templateResolution, 
            final AdditionExpression expression, final IStandardExpressionEvaluator expressionEvaluator) {

        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] Evaluating addition expression: \"{}\"", TemplateEngine.threadIndex(), expression.getStringRepresentation());
        }
        
        Object leftValue = 
            Expression.execute(arguments, templateResolution, expression.getLeft(), expressionEvaluator);

        Object rightValue = 
            Expression.execute(arguments, templateResolution, expression.getRight(), expressionEvaluator);
        
        if (leftValue == null) {
            leftValue = "null";
        }
        if (rightValue == null) {
            rightValue = "null";
        }
        
        final BigDecimal leftNumberValue = ObjectUtils.evaluateAsNumber(leftValue);
        final BigDecimal rightNumberValue = ObjectUtils.evaluateAsNumber(rightValue);
        if (leftNumberValue != null && rightNumberValue != null) {
            // Addition will act as a mathematical 'plus'
            return leftNumberValue.add(rightNumberValue);
        }
        
        return new LiteralValue(
                LiteralValue.unwrap(leftValue).toString().concat(LiteralValue.unwrap(rightValue).toString()));
        
    }

    
}
