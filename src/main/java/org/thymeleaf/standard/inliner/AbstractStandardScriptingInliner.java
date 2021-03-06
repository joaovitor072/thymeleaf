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
package org.thymeleaf.standard.inliner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.ExpressionParsingException;
import org.thymeleaf.inliner.ITextInliner;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.w3c.dom.Text;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1.2
 *
 */
public abstract class AbstractStandardScriptingInliner implements ITextInliner {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    public static final String SCRIPT_ADD_INLINE_EVAL = "\\/\\*\\[\\+(.*?)\\+\\]\\*\\/";
    public static final Pattern SCRIPT_ADD_INLINE_EVAL_PATTERN = Pattern.compile(SCRIPT_ADD_INLINE_EVAL, Pattern.DOTALL);

    public static final String SCRIPT_REMOVE_INLINE_EVAL = "\\/\\*\\[\\-(.*?)\\-\\]\\*\\/";
    public static final Pattern SCRIPT_REMOVE_INLINE_EVAL_PATTERN = Pattern.compile(SCRIPT_REMOVE_INLINE_EVAL, Pattern.DOTALL);

    public static final String SCRIPT_VARIABLE_EXPRESSION_INLINE_EVAL = "\\/\\*(\\[\\[(.*?)\\]\\])\\*\\/([^\n]*?)\n";
    public static final Pattern SCRIPT_VARIABLE_EXPRESSION_INLINE_EVAL_PATTERN = Pattern.compile(SCRIPT_VARIABLE_EXPRESSION_INLINE_EVAL, Pattern.DOTALL);

    public static final String SCRIPT_INLINE_EVAL = "\\[\\[(.*?)\\]\\]";
    public static final Pattern SCRIPT_INLINE_EVAL_PATTERN = Pattern.compile(SCRIPT_INLINE_EVAL, Pattern.DOTALL);
   
    
    
    
    protected AbstractStandardScriptingInliner() {
        super();
    }
    

    
    public final void inline(final Arguments arguments, final TemplateResolution templateResolution, final Text text) {
        
        final String content = text.getTextContent();
        final String javascriptContent =
            processScriptingInline(content, arguments, templateResolution);
        text.setTextContent(javascriptContent);
        
    }

    
    
    

    private final String processScriptingInline(
            final String input, final Arguments arguments, final TemplateResolution templateResolution) {
        
        return
            processScriptingVariableInline(
                processScriptingVariableExpressionInline(
                    processScriptingAddInline(
                        processScriptingRemoveInline(
                            input)
                        )
                    ),
                arguments, templateResolution);
            
    }

    
    
    private final String processScriptingAddInline(final String input) {
        
        final Matcher matcher = SCRIPT_ADD_INLINE_EVAL_PATTERN.matcher(input);

        if (matcher.find()) {
            
            final StringBuilder strBuilder = new StringBuilder();
            int curr = 0;
            
            do {
                
                strBuilder.append(input.substring(curr,matcher.start(0)));
                
                final String match = matcher.group(1);
                
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("[THYMELEAF][{}] Adding inlined javascript text \"{}\"", TemplateEngine.threadIndex(), match);
                }
                
                strBuilder.append(match);
                
                curr = matcher.end(0);
                
            } while (matcher.find());
            
            strBuilder.append(input.substring(curr));
            
            return strBuilder.toString();
            
        }
        
        return input;
        
    }


    
    private final String processScriptingRemoveInline(final String input) {
        
        final Matcher matcher = SCRIPT_REMOVE_INLINE_EVAL_PATTERN.matcher(input);

        if (matcher.find()) {
            
            final StringBuilder strBuilder = new StringBuilder();
            int curr = 0;
            
            do {
                
                strBuilder.append(input.substring(curr,matcher.start(0)));
                
                final String match = matcher.group(1);
                
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("[THYMELEAF][{}] Removing inlined javascript text \"{}\"", TemplateEngine.threadIndex(), match);
                }
                
                curr = matcher.end(0);
                
            } while (matcher.find());
            
            strBuilder.append(input.substring(curr));
            
            return strBuilder.toString();
            
        }
        
        return input;
    }
    


    
    
    
    private final String processScriptingVariableExpressionInline(final String input) {
        
        final Matcher matcher = SCRIPT_VARIABLE_EXPRESSION_INLINE_EVAL_PATTERN.matcher(input);
        
        if (matcher.find()) {
            
            final StringBuilder strBuilder = new StringBuilder();
            int curr = 0;
            
            do {
                
                strBuilder.append(input.substring(curr,matcher.start(0)));
                
                strBuilder.append(matcher.group(1));
                strBuilder.append(';');
                
                curr = matcher.end(0);
                
            } while (matcher.find());
            
            strBuilder.append(input.substring(curr));
            
            return strBuilder.toString();
            
        }
        
        return input;
        
    }

    
    
    
    private final String processScriptingVariableInline(
            final String input, final Arguments arguments, final TemplateResolution templateResolution) {
        
        final Matcher matcher = SCRIPT_INLINE_EVAL_PATTERN.matcher(input);
        
        if (matcher.find()) {
            
            final StringBuilder strBuilder = new StringBuilder();
            int curr = 0;
            
            do {
                
                strBuilder.append(input.substring(curr,matcher.start(0)));
                
                final String match = matcher.group(1);
                
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("[THYMELEAF][{}] Applying javascript variable inline evaluation on \"{}\"", TemplateEngine.threadIndex(), match);
                }
                
                
                try {
                    
                    final Object result = 
                        StandardExpressionProcessor.processExpression(arguments, templateResolution, match);
                    
                    strBuilder.append(formatEvaluationResult(result));
                    
                } catch (ExpressionParsingException e) {
                    
                    strBuilder.append(match);
                    
                }
                
                curr = matcher.end(0);
                
            } while (matcher.find());
            
            strBuilder.append(input.substring(curr));
            
            return strBuilder.toString();
            
        }
        
        return input;
        
    }
    

    
    
    protected abstract String formatEvaluationResult(final Object result);
    
    

}
