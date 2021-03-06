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

import java.util.Set;

import org.thymeleaf.Arguments;
import org.thymeleaf.processor.applicability.AttrApplicability;
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
public final class StandardXmlSpaceAttrProcessor 
        extends AbstractStandardSingleAttributeModifierAttrProcessor {

    public static final Integer ATTR_PRECEDENCE = Integer.valueOf(1000);
    public static final String ATTR_NAME = "xmlspace";
    public static final String TARGET_ATTR_NAME = "xml:space";
    
    
    
    public StandardXmlSpaceAttrProcessor() {
        super();
    }

    
    
    public Set<AttrApplicability> getAttributeApplicabilities() {
        return AttrApplicability.createSetForAttrName(ATTR_NAME);
    }

    public Integer getPrecedence() {
        return ATTR_PRECEDENCE;
    }



    @Override
    protected String getTargetAttributeName(
            final Arguments arguments, final TemplateResolution templateResolution, final Document document, 
            final Element element, final Attr attribute, final String attributeName, final String attributeValue) {
        return TARGET_ATTR_NAME;
    }

    
    @Override
    protected ModificationType getModificationType(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute, 
            final String attributeName, final String attributeValue,
            final String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }



    @Override
    protected boolean removeAttributeIfEmpty(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute, 
            final String attributeName, final String attributeValue,
            final String newAttributeName) {
        return true;
    }



    
}
