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

import org.thymeleaf.util.JavaScriptUtils;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1.2
 *
 */
public class StandardJavaScriptInliner extends AbstractStandardScriptingInliner {
    
    
    public static final StandardJavaScriptInliner INSTANCE = new StandardJavaScriptInliner();
    
    
    private StandardJavaScriptInliner() {
        super();
    }

    
    
    
    @Override
    protected String formatEvaluationResult(final Object result) {
        return JavaScriptUtils.print(result);
    }


}
