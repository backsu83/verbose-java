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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * A demonstrative implementation of {@link HexDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HexDecoderMock implements HexDecoder {

    /**
     * {@inheritDoc}
     *
     * @param encoded {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        if (encoded == null) {
            throw new NullPointerException("encoded is null");
        }
        if (encoded.remaining() < 2) {
            throw new BufferUnderflowException();
        }
        encoded.position(encoded.position() + 2);
        return 0;
    }
}
