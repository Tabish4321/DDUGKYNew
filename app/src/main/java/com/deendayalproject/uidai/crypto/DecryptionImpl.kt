package com.deendayalproject.uidai.crypto


import com.deendayalproject.util.AppConstant.CRYPT_ID
import com.deendayalproject.util.AppConstant.CRYPT_IV
import java.lang.Exception
import javax.inject.Inject

class DecryptionImpl @Inject constructor(private val cryptLib: CryptLib) : CryptoStrategy {


    override fun encrypt(body: String): String? {
        return null
    }

    @Throws(Exception::class)
    override fun decrypt(data: String): String {
        return cryptLib.decrypt(data, CRYPT_ID, CRYPT_IV)
    }
}