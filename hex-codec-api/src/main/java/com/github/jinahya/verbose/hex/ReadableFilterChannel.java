/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.hex;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> channel type parameter
 */
public class ReadableFilterChannel<T extends ReadableByteChannel>
        extends FilterChannel<T>
        implements ReadableByteChannel {

    public ReadableFilterChannel(final T channel) {
        super(channel);
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        return channel.read(dst);
    }

}