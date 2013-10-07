/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2013, The THYMELEAF team (http://www.thymeleaf.org)
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
package org.thymeleaf.engine21.conversion.conversion3;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring3.dialect.SpringStandardDialect;
import org.thymeleaf.testing.templateengine.context.web.SpringWebProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;


public class Conversion3Test {



    public Conversion3Test() {
        super();
    }






    @Test
    public void testConversion1() throws Exception {

        final SpringWebProcessingContextBuilder contextBuilder = new SpringWebProcessingContextBuilder();
        contextBuilder.setApplicationContextConfigLocation("classpath:engine21/conversion/conversion3/applicationContext.xml");

        final TestExecutor executor = new TestExecutor();
        executor.setProcessingContextBuilder(contextBuilder);
        executor.setDialects(Arrays.asList(new IDialect[]{new SpringStandardDialect()}));

        executor.execute("classpath:engine21/conversion/conversion3");

        Assert.assertTrue(executor.isAllOK());

    }


}
