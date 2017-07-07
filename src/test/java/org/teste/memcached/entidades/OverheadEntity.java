package org.teste.memcached.entidades;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

public class OverheadEntity {
	private Blob data;

	public Blob getData() {
		return data;
	}

	public void setData(final Blob data) {
		this.data = data;
	}
	
	public void setData(final InputStream inputStream) throws IOException {}
	
	public void setData(final File file) throws IOException {}
	
	public void setData(final byte[] bytes) {
        final int size = bytes.length;

        if (size < 0) {
            throw new RuntimeException("Cannot insert files greatter than " + Integer.MAX_VALUE + " bytes");
        }
        BlobTeste blobTest = new BlobTeste();
        blobTest.setBytes(bytes);
        this.data = blobTest;
    }
	
	@Override
	public String toString() {
		return "Override="+data;
	}
}
