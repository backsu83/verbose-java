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
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A {@code WritableByteChannel} writes encoded hex characters to underlying
 * channel.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> channel type parameter
 * @param <U> encoder type parameter
 */
public class WritableHexChannel<T extends WritableByteChannel, U extends HexEncoder>
        extends WritableFilterChannel<T> {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance on top of given channel.
     *
     * @param channelSupplier the channel into which encoded hex characters are
     * written
     * @param encoderSupplier the encoder to encode bytes
     */
    public WritableHexChannel(final Supplier<T> channelSupplier,
                              final Supplier<U> encoderSupplier) {
        super(channelSupplier);
        this.encoderSupplier = requireNonNull(
                encoderSupplier, "encoderSupplier is null");
    }

    // -------------------------------------------------------------------------
    /**
     * Writes a sequence of bytes to this channel from the given buffer.
     *
     * @param src {@inheritDoc}
     * @return The number of bytes written, possibly zero
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int write(final ByteBuffer src) throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        final ByteBuffer aux = allocate(src.remaining() << 1); // <1>
        final int count = encoder().encode(src, aux); // <2>
        for (aux.flip(); aux.hasRemaining();) { // <3>
            super.write(aux);
        }
        return count;
    }

    // ----------------------------------------------------------------- encoder
    /**
     * Returns the encoder.
     *
     * @return the encoder.
     * @throws IOException if an I/O error occurs.
     */
    protected U encoder() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (encoder == null && ((encoder = encoderSupplier.get()) == null)) {
            throw new RuntimeException("null encoder supplied");
        }
        return encoder;
    }

    // -------------------------------------------------------------------------
    private Supplier<U> encoderSupplier;

    private U encoder;
}
