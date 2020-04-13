package com.ainirobot.bitmapfiltertest;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferBuilder implements DataOutput, DataInput {
    private ByteBuffer byteBuffer;

    public ByteBufferBuilder(int initialCapacity) {
        this(ByteBuffer.allocate(initialCapacity).order(ByteOrder.nativeOrder()));
    }

    public ByteBufferBuilder(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void readFully(byte[] b) {
        byteBuffer.get(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        byteBuffer.get(b, off, len);
    }

    @Override
    public int skipBytes(int n) {
        byteBuffer.position(byteBuffer.position() + n);
        return n;
    }

    @Override
    public boolean readBoolean() {
        return readByte() != 0;
    }

    @Override
    public byte readByte() {
        return byteBuffer.get();
    }

    @Override
    public int readUnsignedByte() {
        return readByte() & 0xFF;
    }

    @Override
    public short readShort() {
        return byteBuffer.getShort();
    }

    @Override
    public int readUnsignedShort() {
        return ((int) byteBuffer.getShort()) & 0xffff;
    }

    @Override
    public char readChar() {
        return byteBuffer.getChar();
    }

    @Override
    public int readInt() {
        return byteBuffer.getInt();
    }

    @Override
    public long readLong() {
        return byteBuffer.getLong();
    }

    @Override
    public float readFloat() {
        return byteBuffer.getFloat();
    }

    @Override
    public double readDouble() {
        return byteBuffer.getDouble();
    }

    @Override
    public String readLine() {
        return null;
    }

    @Override
    public String readUTF() {
        return null;
    }

    ByteBuffer ensureCapacity(int capacity) {
        if (capacity <= byteBuffer.capacity()) {
            return byteBuffer;
        } else {
            int realCapacity = byteBuffer.capacity();
            while ((realCapacity = 2 * realCapacity + 1) < capacity) ;
            return byteBuffer = ByteBuffer.allocate(realCapacity).order(ByteOrder.nativeOrder()).put((ByteBuffer) byteBuffer.flip());
        }
    }

    @Override
    public void write(int b) {
        ensureCapacity(byteBuffer.position() + 1).put((byte) b);
    }

    @Override
    public void write(byte[] b) {
        ensureCapacity(byteBuffer.position() + b.length).put(b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        ensureCapacity(byteBuffer.position() + len).put(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) {
        write(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) {
        byteBuffer.put((byte) v);
    }

    @Override
    public void writeShort(int v) {
        byteBuffer.putShort((short) v);
    }

    @Override
    public void writeChar(int v) {
        byteBuffer.putChar((char) v);
    }

    @Override
    public void writeInt(int v) {
        ensureCapacity(byteBuffer.position() + 4).putInt(v);
    }

    @Override
    public void writeLong(long v) {
        ensureCapacity(byteBuffer.position() + 8).putLong(v);
    }

    @Override
    public void writeFloat(float v) {
        ensureCapacity(byteBuffer.position() + 4).putFloat(v);
    }

    @Override
    public void writeDouble(double v) {
        ensureCapacity(byteBuffer.position() + 8).putDouble(v);
    }

    @Override
    public void writeBytes(String s) {

    }

    @Override
    public void writeChars(String s) {

    }

    @Override
    public void writeUTF(String s) {

    }

    public ByteBufferBuilder append(int[] arr) {
        if (arr != null) {
            writeInt(arr.length);
            for (int v : arr) {
                writeInt(v);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public ByteBufferBuilder append(byte[] arr) {
        if (arr != null) {
            writeInt(arr.length);
            write(arr);
        } else {
            writeInt(-1);
        }
        return this;
    }

    public ByteBufferBuilder append(CharSequence charSequence) {
        if (charSequence != null) {
            final ByteBuffer buffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(charSequence));
            writeInt(buffer.remaining());
            write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
        } else {
            writeInt(-1);
        }
        return this;
    }

    public CharSequence readCharSequence() {
        final int len = readInt();
        if (len >= 0) {
            final int oldLimit = byteBuffer.limit();
            try {
                return StandardCharsets.UTF_8.decode((ByteBuffer) byteBuffer.limit(byteBuffer.position() + len));
            } finally {
                byteBuffer.limit(oldLimit);
            }
        } else {
            return null;
        }
    }

    public CharSequence[] readCharSequenceArray() {
        final int len = readInt();
        if (len >= 0) {
            final CharSequence[] charSequences = new CharSequence[len];
            for (int i = 0; i < len; i++) {
                charSequences[i] = readCharSequence();
            }
            return charSequences;
        } else {
            return null;
        }
    }

    public ByteBufferBuilder append(CharSequence[] charSequences) {
        if (charSequences != null) {
            writeInt(charSequences.length);
            for (CharSequence charSequence : charSequences) {
                append(charSequence);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public ByteBufferBuilder append(short[] shorts) {
        if (shorts != null) {
            writeInt(shorts.length);
            for (short s : shorts) {
                writeShort(s);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public short[] readShortArray() {
        final int len = readInt();
        if (len >= 0) {
            short[] shorts = new short[len];
            for (int i = 0; i < len; i++) {
                shorts[i] = readShort();
            }
            return shorts;
        } else {
            return null;
        }
    }

    public int[] readIntArray() {
        final int len = readInt();
        if (len >= 0) {
            final int[] ints = new int[len];
            for (int i = 0; i < len; i++) {
                ints[i] = readInt();
            }
            return ints;
        } else {
            return null;
        }
    }

    public byte[] readByteArray() {
        final int len = readInt();
        if (len >= 0) {
            final byte[] bytes = new byte[len];
            readFully(bytes);
            return bytes;
        } else {
            return null;
        }
    }

    public ByteBufferBuilder append(double[] doubles) {
        if (doubles != null) {
            writeInt(doubles.length);
            for (double s : doubles) {
                writeDouble(s);
            }
        } else {
            writeInt(-1);
        }
        return this;
    }

    public double[] readDoubleArray() {
        final int len = readInt();
        if (len >= 0) {
            final double[] doubles = new double[len];
            for (int i = 0; i < len; i++) {
                doubles[i] = readDouble();
            }
            return doubles;
        } else {
            return null;
        }
    }

    public ByteBuffer build() {
        return (ByteBuffer) byteBuffer.flip();
    }
}
