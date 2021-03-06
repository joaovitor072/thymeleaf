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

import java.util.List;

import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.AttrProcessorException;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractChildrenModifierAttrProcessor 
        extends AbstractAttrProcessor {
    
    
    
    public AbstractChildrenModifierAttrProcessor() {
        super();
    }
    
    
    
    public final AttrProcessResult process(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute) {

        
        final String attributeName = attribute.getName();
        final String normalizedAttributeName =
            (attributeName == null? null : attributeName.toLowerCase());
        
        String attributeValue = attribute.getValue();

        if (attributeValue == null || attributeValue.trim().equals("")) {
            throw new AttrProcessorException("Empty value for \"" +
                    attribute.getName() + "\" attribute not allowed");
        }
        
        attributeValue = attributeValue.trim();

        final List<Node> newChildren = 
            getNewChildren(arguments, templateResolution, document, element, attribute, normalizedAttributeName, attributeValue);
        
        final NodeList currentElementChildren = element.getChildNodes();
        final int childrenLen = currentElementChildren.getLength();
        for (int i = childrenLen - 1; i >= 0; i--) {
            element.removeChild(currentElementChildren.item(i));
        }
        
        for (final Node newChild : newChildren) {
            element.appendChild(newChild);
        }
        
        element.removeAttributeNode(attribute);
        
        return AttrProcessResult.REMOVE_ATTRIBUTE;
        
    }

    
    
    protected abstract List<Node> getNewChildren(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute, final String attributeName, final String attributeValue);

    
}
