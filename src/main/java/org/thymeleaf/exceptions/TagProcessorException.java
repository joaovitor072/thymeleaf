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
package org.thymeleaf.exceptions;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class TagProcessorException extends TemplateProcessingException {

    private static final long serialVersionUID = 7749477057002632757L;


    
    public TagProcessorException(final String message) {
        super(message);
    }
    
    public TagProcessorException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public TagProcessorException(final String message, final String templateName, final Integer lineNumber, final Throwable cause) {
        super(message, templateName, lineNumber, cause);
    }

    public TagProcessorException(final String message, final String templateName, final Integer lineNumber) {
        super(message, templateName, lineNumber);
    }

    public TagProcessorException(final String message, final String templateName, final String expression, final Integer lineNumber, final Throwable cause) {
        super(message, templateName, expression, lineNumber, cause);
    }

    public TagProcessorException(final String message, final String templateName, final String expression, final Integer lineNumber) {
        super(message, templateName, expression, lineNumber);
    }

    public TagProcessorException(final String message, final String templateName, final String expression, final Throwable cause) {
        super(message, templateName, expression, cause);
    }

    public TagProcessorException(final String message, final String templateName, final String expression) {
        super(message, templateName, expression);
    }

    public TagProcessorException(final String message, final String templateName, final Throwable cause) {
        super(message, templateName, cause);
    }

    public TagProcessorException(final String message, final String templateName) {
        super(message, templateName);
    }
    
    
}
