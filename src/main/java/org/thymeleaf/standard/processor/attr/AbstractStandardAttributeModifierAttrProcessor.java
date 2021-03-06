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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.AttrProcessorException;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.Assignation;
import org.thymeleaf.standard.expression.AssignationSequence;
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
public abstract class AbstractStandardAttributeModifierAttrProcessor 
        extends AbstractAttributeModifierAttrProcessor {
    

    
    
    public AbstractStandardAttributeModifierAttrProcessor() {
        super();
    }


    
    
    
    @Override
    protected final Map<String,String> getNewAttributeValues(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute, final String attributeName, final String attributeValue) {
        
        
        final AssignationSequence assignations = 
            StandardExpressionProcessor.parseAssignationSequence(arguments, templateResolution, attributeValue);
        if (assignations == null) {
            throw new AttrProcessorException(
                    "Could not parse value as attribute assignations: \"" + attributeValue + "\"");
        }
        
        final Map<String,String> newAttributeValues = new LinkedHashMap<String,String>();
        
        for (final Assignation assignation : assignations) {
            
            final String newAttributeName = assignation.getLeft().getValue();
            final Expression expression = assignation.getRight();
            
            final Object result = StandardExpressionProcessor.executeExpression(arguments, templateResolution, expression);
            
            newAttributeValues.put(newAttributeName, (result == null? "" : result.toString()));

        }
        
        return Collections.unmodifiableMap(newAttributeValues);
        
    }

    

    
}
