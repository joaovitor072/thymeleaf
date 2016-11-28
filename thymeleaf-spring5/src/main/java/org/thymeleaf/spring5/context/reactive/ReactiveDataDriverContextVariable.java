/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
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
package org.thymeleaf.spring5.context.reactive;

import org.reactivestreams.Publisher;
import org.thymeleaf.util.Validate;

/**
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 3.0.3
 *
 */
public class ReactiveDataDriverContextVariable<T>
        extends ReactiveLazyContextVariable<T>
        implements IReactiveDataDriverContextVariable<T> {

    public static final int DEFAULT_DATA_DRIVER_CHUNK_SIZE_ELEMENTS = 100;

    public final int dataChunkSize;


    public ReactiveDataDriverContextVariable(final Publisher<T> dataStream) {
        this(dataStream, DEFAULT_DATA_DRIVER_CHUNK_SIZE_ELEMENTS);
    }

    public ReactiveDataDriverContextVariable(final Publisher<T> dataStream, final int dataChunkSizeElements) {
        super(dataStream);
        Validate.isTrue(dataChunkSizeElements > 0, "Data Chunk Size cannot be <= 0");
        this.dataChunkSize = dataChunkSizeElements;
    }


    public int getDataChunkSizeElements() {
        return this.dataChunkSize;
    }

}
