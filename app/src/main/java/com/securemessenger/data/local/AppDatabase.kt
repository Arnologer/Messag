package com.securemessenger.data.local

import androidx.room.*
import com.securemessenger.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Room Database for local storage of messages and chats
 */
@Database(
    entities = [
        UserEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        GroupMemberEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun groupMemberDao(): GroupMemberDao

    companion object {
        const val DATABASE_NAME = "secure_messenger_db"
    }
}

// Entity classes

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val publicKey: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val lastSeen: Long?,
    val isOnline: Boolean
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val name: String,
    val participantsJson: String, // JSON array of user IDs
    val isGroup: Boolean,
    val groupAdminId: String?,
    val lastMessageId: String?,
    val unreadCount: Int,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "messages",
    indices = [Index(value = ["chatId"]), Index(value = ["senderId"])]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val messageType: String,
    val mediaUrl: String?,
    val mediaThumbnail: String?,
    val timestamp: Long,
    val isEncrypted: Boolean,
    val deliveryStatus: String,
    val isRead: Boolean
)

@Entity(
    tableName = "group_members",
    primaryKeys = ["chatId", "userId"],
    indices = [Index(value = ["chatId"]), Index(value = ["userId"])]
)
data class GroupMemberEntity(
    val chatId: String,
    val userId: String,
    val role: String,
    val joinedAt: Long
)

// DAOs

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: String): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("UPDATE chats SET unreadCount = :count WHERE id = :chatId")
    suspend fun updateUnreadCount(chatId: String, count: Int)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChatId(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageByChatId(chatId: String): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: String)
}

@Dao
interface GroupMemberDao {
    @Query("SELECT * FROM group_members WHERE chatId = :chatId")
    fun getMembersByChatId(chatId: String): Flow<List<GroupMemberEntity>>

    @Query("SELECT * FROM group_members WHERE chatId = :chatId AND userId = :userId")
    suspend fun getMember(chatId: String, userId: String): GroupMemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: GroupMemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<GroupMemberEntity>)

    @Delete
    suspend fun deleteMember(member: GroupMemberEntity)

    @Query("DELETE FROM group_members WHERE chatId = :chatId AND userId = :userId")
    suspend fun removeMember(chatId: String, userId: String)
}

// Type converters

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }

    @TypeConverter
    fun fromMessageTypeToString(type: MessageType): String {
        return type.name
    }

    @TypeConverter
    fun fromStringToMessageType(data: String): MessageType {
        return MessageType.valueOf(data)
    }

    @TypeConverter
    fun fromDeliveryStatusToString(status: DeliveryStatus): String {
        return status.name
    }

    @TypeConverter
    fun fromStringToDeliveryStatus(data: String): DeliveryStatus {
        return DeliveryStatus.valueOf(data)
    }

    @TypeConverter
    fun fromMemberRoleToString(role: MemberRole): String {
        return role.name
    }

    @TypeConverter
    fun fromStringToMemberRole(data: String): MemberRole {
        return MemberRole.valueOf(data)
    }
}
