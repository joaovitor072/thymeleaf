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

import java.util.ArrayList;
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
public abstract class SimpleExpression extends Expression {
    
    private static final long serialVersionUID = 9145380484247069725L;
    
    
    static final char EXPRESSION_START_CHAR = '{';
    static final char EXPRESSION_END_CHAR = '}';
    
    
    
    protected SimpleExpression() {
        super();
    }
    
    
    
    static List<ExpressionParsingNode> decomposeSimpleExpressionsExceptNumberLiterals(final String input) {
        return decomposeSimpleExpressions(input, false); 
    }
    
    
    static List<ExpressionParsingNode> decomposeSimpleExpressions(final String input) {
        return decomposeSimpleExpressions(input, true); 
    }

    
    
    private static List<ExpressionParsingNode> decomposeSimpleExpressions(
            final String input, final boolean decomposeNumberLiterals) {
        
        if (input == null || input.trim().equals("")) {
            return null;
        }

        
        final StringBuilder inputWithPlaceholders = new StringBuilder();
        StringBuilder fragment = new StringBuilder();
        final List<ExpressionParsingNode> fragments = new ArrayList<ExpressionParsingNode>();
        int currentIndex = 1;
        
        int expLevel = 0;
        boolean inLiteral = false;
        boolean inNumber = false;
        
        char expSelectorChar = (char)0;
        
        final int inputLen = input.length();
        for (int i = 0; i < inputLen; i++) {
            
            final char c = input.charAt(i);

            /*
             * First of all, we must check if we were dealing with a number until now
             */
            if (!inLiteral && expLevel == 0 && inNumber && 
                    !(Character.isDigit(c) || c == NumberLiteralExpression.DECIMAL_POINT)) {
                if (decomposeNumberLiterals) {
                    // end number without adding current char to it
                    inNumber = false;
                    inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                    inputWithPlaceholders.append(String.valueOf(currentIndex++));
                    inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                    final NumberLiteralExpression literalExpr = 
                        NumberLiteralExpression.parseNumberLiteral(fragment.toString());
                    if (literalExpr == null) {
                        return null;
                    }
                    fragments.add(new ExpressionParsingNode(literalExpr));
                    fragment = new StringBuilder();
                } else {
                    inNumber = false;
                }
            }
                
            /*
             * Once numbers are processed, check the current character
             */
            if (expLevel == 0 && c == TextLiteralExpression.DELIMITER) {
                
                if (i == 0 || !isEscaping(input, i)) {
                    
                    inLiteral = !inLiteral;
                    if (inLiteral) {
                        // starting literal
                        inputWithPlaceholders.append(fragment);
                        fragment = new StringBuilder();
                        fragment.append(c);
                    } else {
                        // ending literal
                        inputWithPlaceholders.append(PARSING_PLACEHOLDER_CHAR);
                        inputWithPlaceholders.append(String.valueOf(currentIndex++));
                        inputWithPlaceholders.append(PARSING_PLACEHOLDER_CHAR);
                        fragment.append(c);
                        final TextLiteralExpression literalExpr = 
                            TextLiteralExpression.parseTextLiteral(fragment.toString());
                        if (literalExpr == null) {
                            return null;
                        }
                        fragments.add(new ExpressionParsingNode(literalExpr));
                        fragment = new StringBuilder();
                    }
                    
                } else {
                    
                    fragment.append(c);
                    
                }
                
            } else if (!inLiteral && c == EXPRESSION_START_CHAR) {

                if (i > 0) {
                    
                    if (expLevel == 0) {
                        final char cPrev = input.charAt(i - 1);
                        if (cPrev == VariableExpression.SELECTOR || cPrev == SelectionVariableExpression.SELECTOR || 
                            cPrev == MessageExpression.SELECTOR || cPrev == LinkExpression.SELECTOR) {
                            // starting expression
                            fragment.deleteCharAt(fragment.length() - 1);
                            inputWithPlaceholders.append(fragment);
                            fragment = new StringBuilder();
                            fragment.append(cPrev);
                            expLevel++;
                            expSelectorChar = cPrev;
                        }
                    } else {
                        expLevel++;
                    }
                    
                }

                fragment.append(c);
                
            } else if (!inLiteral && c == EXPRESSION_END_CHAR) {

                if (expLevel > 0) {
                    
                    expLevel--;
                    
                    if (expLevel == 0) {
                        // ending expression
                        inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                        inputWithPlaceholders.append(String.valueOf(currentIndex++));
                        inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                        fragment.append(c);
                        Expression expr = null;
                        switch (expSelectorChar) {
                            case VariableExpression.SELECTOR: 
                                expr = VariableExpression.parseVariable(fragment.toString()); break;
                            case SelectionVariableExpression.SELECTOR: 
                                expr = SelectionVariableExpression.parseSelectionVariable(fragment.toString()); break;
                            case MessageExpression.SELECTOR: 
                                expr = MessageExpression.parseMessage(fragment.toString()); break;
                            case LinkExpression.SELECTOR: 
                                expr = LinkExpression.parseLink(fragment.toString()); break;
                            default:
                                return null;
                                        
                        }
                        if (expr == null) {
                            return null;
                        }
                        fragments.add(new ExpressionParsingNode(expr));
                        expSelectorChar = (char)0;
                        fragment = new StringBuilder();
                    } else {
                        fragment.append(c);
                    }
                    
                } else {
                    fragment.append(c);
                }
                
            } else if (decomposeNumberLiterals && !inLiteral && expLevel == 0 && Character.isDigit(c)) {
                
                if (!inNumber) {
                    // starting number
                    inNumber = true;
                    inputWithPlaceholders.append(fragment);
                    fragment = new StringBuilder();
                }
                
                fragment.append(c);
                
            } else {
                
                fragment.append(c);
               
            }
            
            
        }
        
        if (inLiteral || expLevel > 0) {
            return null;
        }
        
        if (decomposeNumberLiterals && inNumber) {
            // last part was a number, add it
            inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
            inputWithPlaceholders.append(String.valueOf(currentIndex++));
            inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
            final NumberLiteralExpression literalExpr = 
                NumberLiteralExpression.parseNumberLiteral(fragment.toString());
            if (literalExpr == null) {
                return null;
            }
            fragments.add(new ExpressionParsingNode(literalExpr));
            fragment = new StringBuilder();
        }
        
        inputWithPlaceholders.append(fragment);
        
        final List<ExpressionParsingNode> result = new ArrayList<ExpressionParsingNode>();
        result.add(new ExpressionParsingNode(inputWithPlaceholders.toString()));
        result.addAll(fragments);

        return result; 
        
    }


    

    
    

    
    
    static List<ExpressionParsingNode> addNumberLiteralDecomposition(
            final List<ExpressionParsingNode> inputExprs, final int inputIndex) {

        
        if (inputExprs == null || inputExprs.size() == 0 || inputIndex >= inputExprs.size()) {
            return null;
        }

        final String input = inputExprs.get(inputIndex).getInput();

        
        final StringBuilder inputWithPlaceholders = new StringBuilder();
        StringBuilder fragment = new StringBuilder();
        final List<ExpressionParsingNode> fragments = new ArrayList<ExpressionParsingNode>();
        int currentIndex = inputExprs.size();
        
        boolean inNumber = false;
        boolean inExpression = false;
        
        final int inputLen = input.length();
        for (int i = 0; i < inputLen; i++) {
            
            final char c = input.charAt(i);

            /*
             * First of all, we must check if we were dealing with a number until now
             */
            if (inNumber && !(Character.isDigit(c) || c == NumberLiteralExpression.DECIMAL_POINT)) {
                // end number without adding current char to it
                inNumber = false;
                inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                inputWithPlaceholders.append(String.valueOf(currentIndex++));
                inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
                final NumberLiteralExpression literalExpr = 
                    NumberLiteralExpression.parseNumberLiteral(fragment.toString());
                if (literalExpr == null) {
                    return null;
                }
                fragments.add(new ExpressionParsingNode(literalExpr));
                fragment = new StringBuilder();
            }
                
                
            if (!inExpression && Character.isDigit(c)) {
                
                if (!inNumber) {
                    // starting number
                    inNumber = true;
                    inputWithPlaceholders.append(fragment);
                    fragment = new StringBuilder();
                }
                
                fragment.append(c);
                
            } else if (c == Expression.PARSING_PLACEHOLDER_CHAR){
                
                inExpression = !inExpression;
                fragment.append(c);
                
            } else {
                
                fragment.append(c);
               
            }
            
            
        }
        
        if (inNumber) {
            // last part was a number, add it
            inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
            inputWithPlaceholders.append(String.valueOf(currentIndex++));
            inputWithPlaceholders.append(Expression.PARSING_PLACEHOLDER_CHAR);
            final NumberLiteralExpression literalExpr = 
                NumberLiteralExpression.parseNumberLiteral(fragment.toString());
            if (literalExpr == null) {
                return null;
            }
            fragments.add(new ExpressionParsingNode(literalExpr));
            fragment = new StringBuilder();
        }
        
        inputWithPlaceholders.append(fragment);
        
        final List<ExpressionParsingNode> result = inputExprs;
        result.set(inputIndex, new ExpressionParsingNode(inputWithPlaceholders.toString()));
        result.addAll(fragments);

        return result; 
        
    }


    
    
    
    
    private static boolean isEscaping(final String input, final int pos) {
        // Only an odd number of \'s will indicate escaping
        if (pos == 0 || input.charAt(pos - 1) != '\\') {
            return false;
        }
        int i = pos - 1;
        boolean odd = false;
        while (i >= 0) {
            if (input.charAt(i) == '\\') {
                odd = !odd;
            } else {
                return odd;
            }
            i--;
        }
        return odd;
    }
    

    
    
    
    
    static Object executeSimple(final Arguments arguments, final TemplateResolution templateResolution, 
            final SimpleExpression expression, final IStandardExpressionEvaluator expressionEvaluator) {
        
        if (expression instanceof VariableExpression) {
            return VariableExpression.executeVariable(arguments, templateResolution, (VariableExpression)expression, expressionEvaluator);
        } else  if (expression instanceof MessageExpression) {
            return MessageExpression.executeMessage(arguments, templateResolution, (MessageExpression)expression, expressionEvaluator);
        } else  if (expression instanceof TextLiteralExpression) {
            return TextLiteralExpression.executeTextLiteral(arguments, templateResolution, (TextLiteralExpression)expression);
        } else  if (expression instanceof NumberLiteralExpression) {
            return NumberLiteralExpression.executeNumberLiteral(arguments, templateResolution, (NumberLiteralExpression)expression);
        } else  if (expression instanceof LinkExpression) {
            return LinkExpression.executeLink(arguments, templateResolution, (LinkExpression)expression, expressionEvaluator);
        } else  if (expression instanceof SelectionVariableExpression) {
            return SelectionVariableExpression.executeSelectionVariable(arguments, templateResolution, (SelectionVariableExpression)expression, expressionEvaluator);
        }
        
        throw new ExpressionEvaluationException("Unrecognized simple expression: " + expression.getClass().getName());
        
    }
    
}
