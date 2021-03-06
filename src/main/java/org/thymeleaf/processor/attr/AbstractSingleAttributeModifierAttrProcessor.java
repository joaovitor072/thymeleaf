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
package org.thymeleaf.processor.attr;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
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
public abstract class AbstractSingleAttributeModifierAttrProcessor 
        extends AbstractAttributeModifierAttrProcessor {

    
    
    public AbstractSingleAttributeModifierAttrProcessor() {
        super();
    }

    
    
    @Override
    protected final Map<String,String> getNewAttributeValues(
            final Arguments arguments, final TemplateResolution templateResolution,
            final Document document, final Element element, final Attr attribute,
            final String attributeName, final String attributeValue) {
        
        final String name = getTargetAttributeName(arguments, templateResolution, document, element, attribute, attributeName, attributeValue);
        final String value = getTargetAttributeValue(arguments, templateResolution, document, element, attribute, attributeName, attributeValue);
        
        final Map<String,String> valuesMap = new HashMap<String,String>();
        valuesMap.put(name, value);
        return valuesMap;
        
    }
    

    
    protected abstract String getTargetAttributeName(
            final Arguments arguments, final TemplateResolution templateResolution,
            final Document document, final Element element, final Attr attribute,
            final String attributeName, final String attributeValue);
    
    
    
    protected abstract String getTargetAttributeValue(
            final Arguments arguments, final TemplateResolution templateResolution,
            final Document document, final Element element, final Attr attribute,
            final String attributeName, final String attributeValue);
    
    
}
