package com.s17201120.player.util;

import com.flyfox.util.serializable.Serializer;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class FSTSerializer implements Serializer {

	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = null;
		FSTObjectOutput fout = null;
		try {
			out = new ByteArrayOutputStream();
			fout = new FSTObjectOutput(out);
			fout.writeObject(obj);
			return out.toByteArray();
		} finally {
			if (fout != null)
				try {
					fout.close();
				} catch (IOException e) {
				}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0)
			return null;
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return (T) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	}

}
