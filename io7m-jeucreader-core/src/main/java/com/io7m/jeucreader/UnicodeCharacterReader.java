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

import com.io7m.jnull.NullCheck;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The default implementation of {@link UnicodeCharacterReaderType}.
 */

public final class UnicodeCharacterReader implements
  UnicodeCharacterReaderPushBackType
{
  private final Deque<Integer> pushed;
  private final Reader reader;

  /**
   * Construct a new codepoint reader.
   *
   * @param r An existing reader
   */

  public UnicodeCharacterReader(
    final Reader r)
  {
    this.reader = NullCheck.notNull(r);
    this.pushed = new ArrayDeque<>(3);
  }

  /**
   * Construct a new character reader from the given reader.
   *
   * @param r The original reader
   *
   * @return A new reader
   */

  public static UnicodeCharacterReaderPushBackType newReader(
    final Reader r)
  {
    return new UnicodeCharacterReader(r);
  }

  @Override
  public void pushCodePoint(
    final int c)
  {
    this.pushed.push(Integer.valueOf(c));
  }

  @Override
  public int readCodePoint()
    throws IOException
  {
    if (this.pushed.size() > 0) {
      return this.pushed.pop().intValue();
    }

    final int c0 = this.reader.read();

    /**
     * EOF?
     */

    if (c0 == -1) {
      return -1;
    }

    /**
     * Low surrogate without corresponding high surrogate? Error!
     */

    if (Character.isLowSurrogate((char) c0)) {
      throw new OrphanLowSurrogate(
        "Low surrogate received without high surrogate");
    }

    /**
     * High surrogate?
     */

    if (Character.isHighSurrogate((char) c0)) {
      final int c1 = this.reader.read();

      /**
       * EOF before high surrogate? Error!
       */

      if (c1 == -1) {
        throw new MissingLowSurrogate("EOF reached before low surrogate");
      }

      /**
       * Low surrogate? Convert pair and return.
       */

      if (Character.isLowSurrogate((char) c1)) {
        return Character.toCodePoint((char) c0, (char) c1);
      }

      throw new InvalidSurrogatePair(
        "Invalid character received after high surrogate");
    }

    return c0;
  }
}
