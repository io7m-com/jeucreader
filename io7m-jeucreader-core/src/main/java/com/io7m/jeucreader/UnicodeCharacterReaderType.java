/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jeucreader;

import java.io.IOException;

/**
 * An interface that provides unicode characters one at a time.
 */

public interface UnicodeCharacterReaderType
{
  /**
   * @return A single unicode code point, or <code>-1</code> at end-of-stream.
   * @throws IOException
   *           On I/O errors
   * @throws InvalidSurrogatePair
   *           If the character received after a high surrogate was not a low
   *           surrogate.
   * @throws MissingLowSurrogate
   *           If an end-of-stream was encountered before the low surrogate of
   *           a surrogate pair was received.
   * @throws OrphanLowSurrogate
   *           If a low surrogate was received outside of a high/low surrogate
   *           pair.
   */

  int readCodePoint()
    throws IOException;
}
