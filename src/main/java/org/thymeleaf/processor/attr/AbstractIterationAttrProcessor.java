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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.AttrProcessorException;
import org.thymeleaf.processor.SubstitutionTag;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.ObjectUtils;
import org.thymeleaf.util.Validate;
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
public abstract class AbstractIterationAttrProcessor 
        extends AbstractAttrProcessor {

    
    public static String DEFAULT_STATUS_VAR_SUFFIX = "Stat";
    

    
    public AbstractIterationAttrProcessor() {
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
        
        final IterationSpec iterationSpec = 
            getIterationSpec(arguments, templateResolution, document, element, attribute, normalizedAttributeName, attributeValue);
        
        
        final String iterVar = iterationSpec.getIterVarName();
        final String statusVar = iterationSpec.getStatusVarName();
        final Object iteratedObject = iterationSpec.getIteratedObject();
        
        
        final List<?> list = ObjectUtils.convertToList(iteratedObject);

        int size = list.size(); 
        int index = 0;
        final List<SubstitutionTag> substitutionTags = new ArrayList<SubstitutionTag>();
        for (final Object obj : list) {
            
            final Map<String,Object> localVariables = new HashMap<String, Object>();
            localVariables.put(iterVar, obj);
            final StatusVar status = 
                new StatusVar(index, index + 1, size, obj);
            if (statusVar != null) {
                localVariables.put(statusVar, status);
            } else {
                localVariables.put(iterVar + DEFAULT_STATUS_VAR_SUFFIX, status);
            }
            
            final Element tag = (Element) element.cloneNode(true);
            tag.removeAttribute(attribute.getName());
            
            final SubstitutionTag substitutionTag = 
                SubstitutionTag.forNodeAndLocalVariables(tag, localVariables);
            
            substitutionTags.add(substitutionTag);
            
            index++;
            
        }
        
        
        return AttrProcessResult.forSubstituteTag(substitutionTags);
        
    }
    


    
    protected abstract IterationSpec getIterationSpec(
            final Arguments arguments, final TemplateResolution templateResolution, 
            final Document document, final Element element, 
            final Attr attribute, final String attributeName, final String attributeValue);
    

    
    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    public static class StatusVar {
        
        private final int index;
        private final int count;
        private final int size;
        private final Object current;

        public StatusVar(final int index, final int count, final int size, final Object current) {
            super();
            this.index = index;
            this.count = count;
            this.size = size;
            this.current = current;
        }

        public int getIndex() {
            return this.index;
        }

        public int getCount() {
            return this.count;
        }
        
        public int getSize() {
            return this.size;
        }
        
        public Object getCurrent() {
            return this.current;
        }
        
        public boolean isEven() {
            return (this.index % 2 == 0);
        }
        
        public boolean isOdd() {
            return !isEven();
        }
        
        public boolean isFirst() {
            return (this.index == 0);
        }
        
        public boolean isLast() {
            return (this.index == this.size - 1);
        }
        
        @Override
        public String toString() {
            return "{index = " + this.index + ", count = " + this.count + 
                    ", size = " + this.size + ", current = " + (this.current == null? "null" : this.current.toString()) + "}";
        }
        
    }
    
    
    
    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    protected static class IterationSpec {
        
        private final String iterVarName;
        private final String statusVarName;
        private final Object iteratedObject;
        
        public IterationSpec(final String iterVarName, 
                final String statusVarName, final Object iteratedObject) {
            super();
            Validate.notEmpty(iterVarName, "Iteration var name cannot be null or empty");
            this.iterVarName = iterVarName;
            this.statusVarName = statusVarName;
            this.iteratedObject = iteratedObject;
        }

        public String getIterVarName() {
            return this.iterVarName;
        }

        public String getStatusVarName() {
            return this.statusVarName;
        }

        public Object getIteratedObject() {
            return this.iteratedObject;
        }

        
    }
    
}
