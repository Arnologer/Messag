package com.securemessenger.data.model

/**
 * Represents a user in the secure messenger
 */
data class User(
    val id: String,
    val username: String,
    val publicKey: String,
    val avatarUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastSeen: Long? = null,
    val isOnline: Boolean = false
)

/**
 * Represents a chat message with encryption support
 */
data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String, // Encrypted content
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val mediaThumbnail: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isEncrypted: Boolean = true,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val isRead: Boolean = false
)

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO,
    FILE
}

enum class DeliveryStatus {
    PENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

/**
 * Represents a chat conversation (private or group)
 */
data class Chat(
    val id: String,
    val name: String,
    val participants: List<String>, // User IDs
    val isGroup: Boolean = false,
    val groupAdminId: String? = null,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Represents a group member with role
 */
data class GroupMember(
    val userId: String,
    val role: MemberRole = MemberRole.MEMBER,
    val joinedAt: Long = System.currentTimeMillis()
)

enum class MemberRole {
    ADMIN,
    MEMBER
}
