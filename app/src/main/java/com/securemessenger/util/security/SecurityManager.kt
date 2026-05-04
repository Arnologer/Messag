package com.securemessenger.util.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * SecurityManager handles encryption/decryption using Android Keystore
 * Implements AES-256-GCM for secure message encryption
 */
class SecurityManager {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val masterKeyName = "SecureMessengerMasterKey"
    private val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, GCM_IV)

    companion object {
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private val GCM_IV = ByteArray(12) // 96-bit IV for GCM
    }

    init {
        if (!keyStore.containsAlias(masterKeyName)) {
            generateMasterKey()
        }
    }

    /**
     * Generate a master key in Android Keystore
     */
    private fun generateMasterKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            masterKeyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(false)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    /**
     * Get the master secret key
     */
    private fun getMasterKey(): SecretKey {
        val entry = keyStore.getEntry(masterKeyName, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    /**
     * Encrypt data using AES-256-GCM
     * @param plaintext Data to encrypt
     * @return Encrypted data with IV prepended
     */
    fun encrypt(plaintext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getMasterKey(), gcmParameterSpec)
        
        val encryptedData = cipher.doFinal(plaintext)
        
        // Prepend IV to encrypted data for storage/transmission
        return gcmParameterSpec.iv + encryptedData
    }

    /**
     * Decrypt data using AES-256-GCM
     * @param ciphertext Encrypted data with IV prepended
     * @return Decrypted plaintext
     */
    fun decrypt(ciphertext: ByteArray): ByteArray {
        // Extract IV from the beginning of ciphertext
        val iv = ciphertext.copyOfRange(0, 12)
        val actualCiphertext = ciphertext.copyOfRange(12, ciphertext.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, getMasterKey(), spec)

        return cipher.doFinal(actualCiphertext)
    }

    /**
     * Encrypt a string message
     */
    fun encryptString(plaintext: String): String {
        val encryptedBytes = encrypt(plaintext.toByteArray(Charsets.UTF_8))
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    /**
     * Decrypt a string message
     */
    fun decryptString(ciphertext: String): String {
        val encryptedBytes = android.util.Base64.decode(ciphertext, android.util.Base64.DEFAULT)
        val decryptedBytes = decrypt(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}
