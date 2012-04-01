package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;

public class TokenSerializer {

	public static void serialize(AccessTokenResponse token) {
		TokenWrapper wrapper = new TokenWrapper(token);
		
		try {
			FileOutputStream fout = new FileOutputStream("./temp/code");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(wrapper);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AccessTokenResponse load() throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream("./temp/code");
		ObjectInputStream ois = new ObjectInputStream(fin);
		AccessTokenResponse token = ((TokenWrapper) ois.readObject()).getToken();
		ois.close();

		return token;
	}
}
