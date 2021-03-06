package com.github.jinahya.verbose.hex;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for hex decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HexDecoder {

    /**
     * Decodes two hex characters from given buffer and returns the decoded
     * octet. A {@link BufferUnderflowException} will be thrown if
     * {@code encoded.remaining()} is less than {@code 2}.
     *
     * @param encoded the byte buffer contains encoded characters.
     * @return a decoded octet whose value is between {@code 0} and {@code 255}
     */
    int decodeOctet(ByteBuffer encoded);

    /**
     * Decodes bytes from given input buffer and put results to specified output
     * buffer. This method continuously invokes
     * {@link #decodeOctet(java.nio.ByteBuffer)} with {@code encoded} as its
     * arguments and put the result to {@code decoced} while
     * {@code encoded.remaining()} is greater than or equals to {@code 2} and
     * {@code decoded.hasRemaining()} returns {@code true}.
     *
     * @param encoded the input buffer
     * @param decoded the output buffer
     * @return number of decoded bytes produced to {@code decoded}
     */
    default int decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        // @todo: validate arguments!
        final int position = decoded.position();
        while (encoded.remaining() >= 2 && decoded.hasRemaining()) { // <1>
            decoded.put((byte) decodeOctet(encoded));
        }
        return decoded.position() - position;
    }

    /**
     * Decodes all remaining bytes from given input buffer and returns a byte
     * buffer containing result.
     *
     * @param encoded the input buffer
     * @return a byte buffer containing result
     */
    default ByteBuffer decode(final ByteBuffer encoded) {
        // @todo: validate argument(s)!
        final ByteBuffer decoded = allocate(encoded.remaining() >> 1); // <1>
        decode(encoded, decoded); // <2>
        return (ByteBuffer) decoded.flip(); // <3>
    }

    /**
     * Decodes given string and returns the result as a string encoded with
     * specified character set.
     *
     * @param encoded the string to decode
     * @param charset the character set to encode output string.
     * @return a decoded string
     */
    default String decode(final String encoded, final Charset charset) {
        // @todo: validate arguments!
        final byte[] encodedBytes = encoded.getBytes(US_ASCII); // <1>
        final byte[] decodedBytes = new byte[encodedBytes.length / 2]; // <2>
        decode(wrap(encodedBytes), wrap(decodedBytes)); // <3>
        return new String(decodedBytes, charset); // <4>
    }

//    /**
//     * Decodes given string. This method invokes
//     * {@link #decode(java.lang.String, java.nio.charset.Charset)} method with
//     * given string and {@link StandardCharsets#UTF_8} as its arguments and
//     * returns the result.
//     *
//     * @param encoded the string to decode
//     * @return a decoded string
//     * @see #decode(java.lang.String, java.nio.charset.Charset)
//     */
//    default String decode(final String encoded) {
//        return decode(encoded, UTF_8);
//    }
}
