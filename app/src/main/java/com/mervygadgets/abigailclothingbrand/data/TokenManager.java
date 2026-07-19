package com.mervygadgets.abigailclothingbrand.data;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
public class TokenManager {
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context){
        try{
            MasterKey masterkey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "auth_prefs",
                    masterkey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    public void saveTokens(String accessToken, String refreshToken) {
      sharedPreferences.edit()
              .putString("access_token", accessToken)
              .putString("refresh_token", refreshToken)
              .apply();
    }

    public String getAccessToken(){
        return sharedPreferences.getString("access_token", null);
    }
}
