/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.hello;

import dagger.ObjectGraph;
import static java.lang.invoke.MethodHandles.lookup;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.BeforeClass;

/**
 * Test class injects its own fields using Dagger.
 *
 * @see <a href="http://square.github.io/dagger/">Dagger</a>
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldDaggerTest extends HelloWorldInjectionTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    @BeforeClass
    void inject() {
        final ObjectGraph graph
                = ObjectGraph.create(new HelloWorldDaggerModule());
        graph.inject(this);
        logger.debug("injected");
    }
}
