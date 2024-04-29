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

package com.io7m.jeucreader.tests;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jeucreader.InvalidSurrogatePair;
import com.io7m.jeucreader.MissingLowSurrogate;
import com.io7m.jeucreader.OrphanLowSurrogate;
import com.io7m.jeucreader.UnicodeCharacterReader;
import com.io7m.jeucreader.UnicodeCharacterReaderPushBackType;
import com.io7m.jeucreader.UnicodeCharacterReaderType;

@SuppressWarnings("static-method") public final class UnicodeCharacterReaderTest
{
  @Test(expected = InvalidSurrogatePair.class) public void testBadSequence()
    throws IOException
  {
    final CharArrayReader rb =
      new CharArrayReader(new char[] { 0xD800, 0x0A });
    final UnicodeCharacterReaderType rr =
      UnicodeCharacterReader.newReader(rb);
    rr.readCodePoint();
  }

  @Test public void testCorrect0()
    throws IOException
  {
    final CharArrayReader rb =
      new CharArrayReader(new char[] { 'A', 'B', 'C', 'D' });
    final UnicodeCharacterReaderType rr =
      UnicodeCharacterReader.newReader(rb);
    Assert.assertEquals('A', rr.readCodePoint());
    Assert.assertEquals('B', rr.readCodePoint());
    Assert.assertEquals('C', rr.readCodePoint());
    Assert.assertEquals('D', rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());
  }

  @Test public void testEOF()
    throws IOException
  {
    final StringReader r = new StringReader("");
    Assert.assertEquals(-1, r.read());
    final UnicodeCharacterReaderType rr = UnicodeCharacterReader.newReader(r);
    Assert.assertEquals(-1, rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());
  }

  @Test(expected = MissingLowSurrogate.class) public
    void
    testMissingLowSurrogate()
      throws IOException
  {
    final CharArrayReader rb = new CharArrayReader(new char[] { 0xD800 });
    final UnicodeCharacterReaderType rr =
      UnicodeCharacterReader.newReader(rb);
    rr.readCodePoint();
  }

  @Test(expected = OrphanLowSurrogate.class) public
    void
    testOrphanLowSurrogate()
      throws IOException
  {
    final CharArrayReader rb = new CharArrayReader(new char[] { 0xDC00 });
    final UnicodeCharacterReaderType rr =
      UnicodeCharacterReader.newReader(rb);
    rr.readCodePoint();
  }

  @Test public void testPushRead0()
    throws IOException
  {
    final CharArrayReader rb =
      new CharArrayReader(new char[] { 'A', 'B', 'C', 'D' });
    final UnicodeCharacterReaderPushBackType rr =
      UnicodeCharacterReader.newReader(rb);
    Assert.assertEquals('A', rr.readCodePoint());
    Assert.assertEquals('B', rr.readCodePoint());
    Assert.assertEquals('C', rr.readCodePoint());
    Assert.assertEquals('D', rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());

    rr.pushCodePoint('3');
    rr.pushCodePoint('2');
    rr.pushCodePoint('1');
    rr.pushCodePoint('0');

    Assert.assertEquals('0', rr.readCodePoint());
    Assert.assertEquals('1', rr.readCodePoint());
    Assert.assertEquals('2', rr.readCodePoint());
    Assert.assertEquals('3', rr.readCodePoint());
    Assert.assertEquals(-1, rr.readCodePoint());
  }

  @Test public void testSurrogateCorrect0()
    throws IOException
  {
    final char[] chars = new char[2];
    final int r = Character.toChars(0x11000, chars, 0);
    final String s = new String(chars);
    Assert.assertEquals(2, s.length());
    Assert.assertEquals(2, r);
    System.out.printf("%d: %s\n", s.length(), s);

    final StringReader sr = new StringReader(s);
    final UnicodeCharacterReaderType rr =
      UnicodeCharacterReader.newReader(sr);
    final int c = rr.readCodePoint();
    Assert.assertEquals(0x11000, c);
  }
}
