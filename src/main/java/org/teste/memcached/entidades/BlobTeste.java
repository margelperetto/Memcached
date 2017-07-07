package org.teste.memcached.entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobTeste implements Blob, Serializable{

	private static final long serialVersionUID = -8599712986533311679L;
	private byte[] bytes;
	
	@Override
	public long length() throws SQLException {
		return getBytes().length;
	}

	@Override
	public byte[] getBytes(long pos, int length) throws SQLException {
		return bytes;
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		return new ByteArrayInputStream(getBytes());
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
		return pattern[(int)start];
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
		return pattern.position(pattern.getBytes(0, (int)pattern.length()), start);
	}

	@Override
	public int setBytes(long pos, byte[] bytes) throws SQLException {
		this.bytes = bytes;
		return 0;
	}

	@Override
	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
		this.bytes = bytes;
		return 0;
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
		return new ByteArrayOutputStream();
	}

	@Override
	public void truncate(long len) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public void free() throws SQLException {
		setBytes(null);
	}

	@Override
	public InputStream getBinaryStream(long pos, long length) throws SQLException {
		return new ByteArrayInputStream(getBytes());
	}

	@Override
	public String toString() {
		return "BLOB STR: "+new String(getBytes());
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
