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

import java.util.List;

import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.ExpressionEvaluationException;
import org.thymeleaf.templateresolver.TemplateResolution;




/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public abstract class ComplexExpression extends Expression {

    
    private static final long serialVersionUID = -3807499386899890260L;



    protected ComplexExpression() {
        super();
    }


    
    static List<ExpressionParsingNode> composeComplexExpressions(
            final List<ExpressionParsingNode> decomposition, int inputIndex) {

        if (decomposition == null) {
            return null;
        }
        
        if (decomposition.get(inputIndex).isExpression()) {
            return decomposition;
        }

        List<ExpressionParsingNode> result = decomposition;

        result = 
            tryComposeUnitaryExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result =
            ConditionalExpression.composeConditionalExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result =
            DefaultExpression.composeDefaultExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            OrExpression.composeOrExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            AndExpression.composeAndExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result =
            EqualsNotEqualsExpression.composeEqualsNotEqualsExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result =
            GreaterLesserExpression.composeGreaterLesserExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            AdditionSubtractionExpression.composeAdditionSubtractionExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            MultiplicationDivisionRemainderExpression.composeMultiplicationDivisionRemainderExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            MinusExpression.composeMinusExpression(result, inputIndex);

        if (result == null) {
            return null;
        }
        if (result.get(inputIndex).isExpression()) {
            return result;
        }

        result = 
            NegationExpression.composeNegationExpression(result, inputIndex);

        return result;
        
    }
    

    
    private static List<ExpressionParsingNode> tryComposeUnitaryExpression(
            final List<ExpressionParsingNode> decomposition, int inputIndex) {

        final ExpressionParsingNode inputParsingNode = decomposition.get(inputIndex);
        
        List<ExpressionParsingNode> result = decomposition;
        
        final String input = inputParsingNode.getInput();
            
        if (input == null || input.trim().equals("")) {
            return null;
        }
    
        final int valueIndex = Expression.placeHolderToIndex(input);
        
        if (valueIndex != -1) {
            
            result =
                ComplexExpression.composeComplexExpressions(result, valueIndex);
            if (result == null) {
                return null;
            }
                
            final ExpressionParsingNode epn = result.get(valueIndex);
            final Expression expr = epn.getExpression();
            if (expr == null) {
                return null;
            }
            
            result.set(inputIndex, new ExpressionParsingNode(expr));
            
        }
        
        return result; 
        
    }


    
    static Object executeComplex(final Arguments arguments, final TemplateResolution templateResolution, 
            final ComplexExpression expression, final IStandardExpressionEvaluator expressionEvaluator) {
        
        if (expression instanceof AdditionExpression) {
            return AdditionExpression.executeAddition(arguments, templateResolution, (AdditionExpression)expression, expressionEvaluator);
        } else if (expression instanceof SubtractionExpression) {
            return SubtractionExpression.executeSubtraction(arguments, templateResolution, (SubtractionExpression)expression, expressionEvaluator);
        } else if (expression instanceof MultiplicationExpression) {
            return MultiplicationExpression.executeMultiplication(arguments, templateResolution, (MultiplicationExpression)expression, expressionEvaluator);
        } else if (expression instanceof DivisionExpression) {
            return DivisionExpression.executeDivision(arguments, templateResolution, (DivisionExpression)expression, expressionEvaluator);
        } else if (expression instanceof RemainderExpression) {
            return RemainderExpression.executeRemainder(arguments, templateResolution, (RemainderExpression)expression, expressionEvaluator);
        } else if (expression instanceof ConditionalExpression) {
            return ConditionalExpression.executeConditional(arguments, templateResolution, (ConditionalExpression)expression, expressionEvaluator);
        } else  if (expression instanceof DefaultExpression) {
            return DefaultExpression.executeDefault(arguments, templateResolution, (DefaultExpression)expression, expressionEvaluator);
        } else  if (expression instanceof MinusExpression) {
            return MinusExpression.executeMinus(arguments, templateResolution, (MinusExpression)expression, expressionEvaluator);
        } else  if (expression instanceof NegationExpression) {
            return NegationExpression.executeNegation(arguments, templateResolution, (NegationExpression)expression, expressionEvaluator);
        } else  if (expression instanceof AndExpression) {
            return AndExpression.executeAnd(arguments, templateResolution, (AndExpression)expression, expressionEvaluator);
        } else  if (expression instanceof OrExpression) {
            return OrExpression.executeOr(arguments, templateResolution, (OrExpression)expression, expressionEvaluator);
        } else  if (expression instanceof EqualsExpression) {
            return EqualsExpression.executeEquals(arguments, templateResolution, (EqualsExpression)expression, expressionEvaluator);
        } else  if (expression instanceof NotEqualsExpression) {
            return NotEqualsExpression.executeNotEquals(arguments, templateResolution, (NotEqualsExpression)expression, expressionEvaluator);
        } else  if (expression instanceof GreaterThanExpression) {
            return GreaterThanExpression.executeGreaterThan(arguments, templateResolution, (GreaterThanExpression)expression, expressionEvaluator);
        } else  if (expression instanceof GreaterOrEqualToExpression) {
            return GreaterOrEqualToExpression.executeGreaterOrEqualTo(arguments, templateResolution, (GreaterOrEqualToExpression)expression, expressionEvaluator);
        } else  if (expression instanceof LessThanExpression) {
            return LessThanExpression.executeLessThan(arguments, templateResolution, (LessThanExpression)expression, expressionEvaluator);
        } else  if (expression instanceof LessOrEqualToExpression) {
            return LessOrEqualToExpression.executeLessOrEqualTo(arguments, templateResolution, (LessOrEqualToExpression)expression, expressionEvaluator);
        }
        
        throw new ExpressionEvaluationException("Unrecognized complex expression: " + expression.getClass().getName());
        
    }
    
    
}
