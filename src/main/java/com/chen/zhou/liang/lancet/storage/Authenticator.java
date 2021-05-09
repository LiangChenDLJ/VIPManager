package com.chen.zhou.liang.lancet.storage;

import com.chen.zhou.liang.lancet.storage.orm.tables.records.LoginmsgRecord;
import com.chen.zhou.liang.lancet.utils.DisplayableException;
import com.google.inject.Inject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jooq.DSLContext;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.chen.zhou.liang.lancet.storage.orm.Tables.LOGINMSG;

public class Authenticator {
    private final DSLContext dslContext;
    private static final int SALT_BYTES_LENGTH = 64;
    @Inject
    public Authenticator(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    private static byte[] concatenateBytes(byte[] bytes1, byte[] bytes2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes1.length + bytes2.length);
        byteBuffer.put(bytes1);
        byteBuffer.put(bytes2);
        return byteBuffer.array();
    }

    private static byte[] getDigest(String password, byte[] saltBytes) throws NoSuchAlgorithmException {
        byte[] passwordSaltBytes = concatenateBytes(password.getBytes(StandardCharsets.UTF_8), saltBytes);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passwordSaltBytes);
        return md.digest();
    }

    private static boolean bytesEqual(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) return false;
        }
        return true;
    }

    private static boolean verifyPassword(LoginmsgRecord admin, String inputPassword)
            throws DecoderException, NoSuchAlgorithmException  {
        byte[] inputPasswordSaltDigest = getDigest(inputPassword, Hex.decodeHex(admin.getSalt()));
        byte[] storedPasswordSaltDigest = Hex.decodeHex(admin.getPasswordhash());
        return bytesEqual(inputPasswordSaltDigest, storedPasswordSaltDigest);
    }

    public void authenticate(String username, String password) throws DisplayableException {
        LoginmsgRecord admin = dslContext.selectFrom(LOGINMSG).where(LOGINMSG.USERNAME.eq(username)).fetchAny();
        if (admin == null) {
            throw new DisplayableException("验证失败，用户 " + username + " 不存在");
        }
        boolean loginSuccess;
        try {
            loginSuccess = verifyPassword(admin, password);
        } catch (DecoderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DisplayableException("[内部错误] 加密失败", e);
        }
        if (!loginSuccess) {
            throw new DisplayableException("验证失败，密码错误");
        }
    }

    private static byte[] getRandomSalt() {
        Random rand = new Random();
        byte[] bytes = new byte[SALT_BYTES_LENGTH];
        rand.nextBytes(bytes);
        return bytes;
    }

    public void updatePassword(String username, String newPassword) throws DisplayableException {
        byte[] newSaltBytes = getRandomSalt();
        String newSaltString = Hex.encodeHexString(newSaltBytes);
        byte[] newDigestBytes;
        try {
            newDigestBytes = getDigest(newPassword, newSaltBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DisplayableException("[内部错误] 加密失败", e);
        }
        String newDigestString = Hex.encodeHexString(newDigestBytes);
        int rowsUpdated = dslContext
                .update(LOGINMSG)
                .set(LOGINMSG.PASSWORDHASH, newDigestString)
                .set(LOGINMSG.SALT, newSaltString)
                .where(LOGINMSG.USERNAME.eq(username))
                .execute();
        assert(rowsUpdated == 1);
    }
}
