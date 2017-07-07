package org.teste.memcached.entidades;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SEBlobImpl implements Blob{

	private ByteArrayInputStream stream;

    private InputStream originalStream;

    private final int length;

    public SEBlobImpl(final byte[] bytes) {
        this.stream = new ByteArrayInputStream(bytes);
        this.length = bytes.length;
    }

    public SEBlobImpl(@JsonProperty(value="stream") InputStream stream, @JsonProperty(value="length") final int length) {
        if (stream instanceof ByteArrayInputStream) {
            this.stream = (ByteArrayInputStream) stream;
        } else {
            this.originalStream = stream;
        }
        this.length = length;
    }

    @Override
    public void free() throws SQLException {
        SEBlobImpl.excep();
    }

    @Override
    public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
        return this.getBinaryStream();
    }

    public long length() throws SQLException {
        return this.length;
    }

    public void truncate(final long pos) throws SQLException {
        SEBlobImpl.excep();
    }

    public byte[] getBytes(final long pos, final int len) throws SQLException {
        try {
            return IOUtils.toByteArray(this.getBinaryStream());
        } catch (final IOException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public int setBytes(final long pos, final byte[] bytes) throws SQLException {
        SEBlobImpl.excep();
        return 0;
    }

    public int setBytes(final long pos, final byte[] bytes, final int i, final int j) throws SQLException {
        SEBlobImpl.excep();
        return 0;
    }

    public long position(final byte[] bytes, final long pos) throws SQLException {
        SEBlobImpl.excep();
        return 0;
    }

    public InputStream getBinaryStream() throws SQLException {
        if (this.stream == null) {
            try {
                this.stream = new ByteArrayInputStream(IOUtils.toByteArray(this.originalStream));
            } catch (final IOException ioe) {
                throw new SQLException("could not reset reader");
            }
        } else {
            this.stream.reset();
        }
        return this.stream;
    }

    public OutputStream setBinaryStream(final long pos) throws SQLException {
        SEBlobImpl.excep();
        return null;
    }

    public long position(final Blob blob, final long pos) throws SQLException {
        SEBlobImpl.excep();
        return 0;
    }

    private static void excep() {
        throw new UnsupportedOperationException("Blob may not be manipulated from creating session");
    }
    
    @Override
    public String toString() {
    	try {
			return "BLOBImlp[ Size:"+length+" - text:"+new String(IOUtils.toByteArray(stream))+" ]";
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
    }
}
