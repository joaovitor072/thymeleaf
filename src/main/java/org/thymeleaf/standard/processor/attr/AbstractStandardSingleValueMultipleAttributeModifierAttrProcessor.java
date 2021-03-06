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
package org.thymeleaf.standard.processor.attr;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.Arguments;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractStandardSingleValueMultipleAttributeModifierAttrProcessor 
        extends AbstractAttributeModifierAttrProcessor {
    
    
    
    public AbstractStandardSingleValueMultipleAttributeModifierAttrProcessor() {
        super();
    }




    @Override
    protected final Map<String, String> getNewAttributeValues(final Arguments arguments,
            final TemplateResolution templateResolution, final Document document,
            final Element element, final Attr attribute, 
            final String attributeName, final String attributeValue) {
        
        final Expression expression =
            StandardExpressionProcessor.parseExpression(arguments, templateResolution, attributeValue);
        
        final Set<String> newAttributeNames = 
            getNewAttributeNames(arguments, templateResolution, document, 
                    element, attribute, attributeName, attributeValue, expression);

        final Object valueForAttributes = 
            StandardExpressionProcessor.executeExpression(arguments, templateResolution, expression);
        
        final Map<String,String> result = new LinkedHashMap<String,String>();
        for (final String newAttributeName : newAttributeNames) {
            result.put(newAttributeName, (valueForAttributes == null? "" : valueForAttributes.toString()));
        }
        
        return result;
        
    }


    protected abstract Set<String> getNewAttributeNames(final Arguments arguments,
            final TemplateResolution templateResolution, final Document document,
            final Element element, final Attr attribute, 
            final String attributeName, final String attributeValue, final Expression expression);




    
}
