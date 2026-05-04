# SecureMessenger - Прототип

## 📁 Структура проекта

```
SecureMessenger/
├── app/
│   ├── src/main/
│   │   ├── java/com/securemessenger/
│   │   │   ├── data/
│   │   │   │   ├── model/          # Модели данных (User, Message, Chat)
│   │   │   │   ├── local/          # Room Database и DAO
│   │   │   │   └── repository/     # Репозитории (TODO)
│   │   │   ├── domain/             # Бизнес-логика (TODO)
│   │   │   ├── ui/
│   │   │   │   ├── activity/       # Activity классы
│   │   │   │   ├── fragment/       # Fragment классы
│   │   │   │   ├── adapter/        # RecyclerView адаптеры
│   │   │   │   └── viewmodel/      # ViewModel классы
│   │   │   └── util/security/      # Шифрование и безопасность
│   │   ├── res/
│   │   │   ├── layout/             # XML макеты экранов
│   │   │   ├── values/             # Строки, цвета, темы
│   │   │   ├── drawable/           # Графические ресурсы
│   │   │   ├── navigation/         # Навигационный граф
│   │   │   └── xml/                # Дополнительные XML файлы
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## ✅ Созданные компоненты прототипа

### 1. Конфигурация проекта
- [x] `build.gradle.kts` - настройки проекта и зависимости
- [x] `settings.gradle.kts` - модули проекта
- [x] `app/build.gradle.kts` - зависимости приложения

### 2. Модели данных (`data/model/Models.kt`)
- [x] `User` - пользователь с публичным ключом
- [x] `Message` - сообщение с поддержкой шифрования
- [x] `Chat` - чат (личный или группа)
- [x] `GroupMember` - участник группы с ролью
- [x] Enum: `MessageType`, `DeliveryStatus`, `MemberRole`

### 3. Локальная база данных (`data/local/AppDatabase.kt`)
- [x] Room Database с 4 таблицами
- [x] DAO интерфейсы для CRUD операций
- [x] Type converters для enum

### 4. Безопасность (`util/security/SecurityManager.kt`)
- [x] AES-256-GCM шифрование через Android Keystore
- [x] Методы encrypt/decrypt для ByteArray и String

### 5. UI Компоненты
- [x] `MainActivity` - главный контейнер
- [x] `ChatListFragment` - список чатов
- [x] `ChatAdapter` - адаптер для RecyclerView
- [x] `ChatListViewModel` - ViewModel с mock данными

### 6. Ресурсы
- [x] Layouts: activity_main, fragment_chat_list, item_chat
- [x] Navigation graph (nav_graph.xml)
- [x] Colors, Strings, Themes
- [x] Drawable для badge unread count
- [x] FileProvider paths

---

## 🔧 ЧТО ТРЕБУЕТСЯ ОТ ВАС В ДАЛЬНЕЙШЕМ

### Этап 1: Настройка окружения разработки

1. **Установите Android Studio**
   - Скачайте с https://developer.android.com/studio
   - Версия: Arctic Fox или новее

2. **Откройте проект в Android Studio**
   - File → Open → выберите папку `/workspace`
   - Дождитесь синхронизации Gradle

3. **Настройте эмулятор или подключите устройство**
   - Создайте виртуальное устройство (API 24+)
   - Или подключите физическое Android устройство

### Этап 2: Реализация недостающих компонентов

#### 🔴 Критически важно реализовать:

1. **Репозитории данных** (`data/repository/`)
   ```kotlin
   // Нужно создать:
   - UserRepository.kt
   - ChatRepository.kt  
   - MessageRepository.kt
   ```

2. **Сетевой слой** (`data/remote/`)
   ```kotlin
   // Нужно создать:
   - ApiService.kt (Retrofit интерфейс)
   - NetworkModule.kt (DI настройка)
   - DTO классы для API ответов
   ```

3. **Экраны приложения**
   ```
   Нужно добавить layouts и фрагменты:
   - fragment_chat.xml (экран переписки)
   - fragment_create_group.xml
   - activity_auth.xml (вход/регистрация)
   - ChatActivity.kt
   - ChatFragment.kt
   ```

4. **Отправка сообщений**
   ```kotlin
   // Нужно реализовать:
   - Поле ввода сообщения
   - Кнопка отправки
   - Обработка фото/видео/аудио
   - Отображение статуса доставки
   ```

### Этап 3: Интеграция Signal Protocol для E2EE

```kotlin
// Требуется реализовать:
1. Генерация identity key pair
2. Создание prekey bundles
3. Session establishment
4. Шифрование сообщений перед отправкой
5. Дешифрование полученных сообщений
```

**Документация Signal Protocol:**
- https://github.com/signalapp/libsignal

### Этап 4: Работа с медиа

1. **CameraX интеграция**
   - Захват фото
   - Запись видео
   
2. **Audio Recorder**
   - Запись голосовых сообщений
   
3. **Media Picker**
   - Выбор из галереи
   
4. **Compress & Upload**
   - Сжатие медиа перед отправкой
   - Загрузка на сервер

### Этап 5: Бэкенд инфраструктура

Вам потребуется создать серверную часть:

**Варианты:**
1. **Firebase** (быстрый старт)
   - Firestore для сообщений
   - Firebase Auth для пользователей
   - Cloud Storage для медиа
   - FCM для пуш-уведомлений

2. **Свой сервер** (полный контроль)
   - Node.js/Go/Python backend
   - WebSocket для real-time
   - PostgreSQL/MongoDB
   - S3-compatible storage для файлов

### Этап 6: Тестирование

```kotlin
// Нужно добавить тесты:
- Unit тесты для ViewModels
- Integration тесты для Repository
- UI тесты с Espresso
```

---

## 📋 План действий (Checklist)

### Немедленно (MVP):
- [ ] Открыть проект в Android Studio
- [ ] Исправить ошибки компиляции (если есть)
- [ ] Добавить фрагмент ChatFragment для переписки
- [ ] Реализовать отправку текстовых сообщений
- [ ] Подключить Firebase (или другой backend)

### Краткосрочно (2-4 недели):
- [ ] Интегрировать Signal Protocol
- [ ] Добавить отправку фото
- [ ] Реализовать создание групп
- [ ] Добавить отображение статусов сообщений

### Среднесрочно (1-2 месяца):
- [ ] Голосовые сообщения
- [ ] Видеосообщения
- [ ] Пуш-уведомления
- [ ] Поиск по чатам

### Долгосрочно:
- [ ] Групповые видеозвонки
- [ ] Исчезающие сообщения
- [ ] Резервное копирование
- [ ] Мультиязычность

---

## 🚀 Быстрый старт

```bash
# 1. Клонируйте проект (если еще не локально)
git clone <your-repo-url>
cd SecureMessenger

# 2. Откройте в Android Studio
# File → Open → выберите папку проекта

# 3. Синхронизируйте Gradle
# Нажмите "Sync Now" когда появится уведомление

# 4. Запустите приложение
# Нажмите Run (зеленый треугольник) или Shift+F10
```

---

## 📞 Контакты и помощь

Если возникнут вопросы по реализации:

1. **Официальная документация Android:**
   https://developer.android.com

2. **Signal Protocol документация:**
   https://github.com/signalapp/libsignal

3. **Firebase для Android:**
   https://firebase.google.com/docs/android/setup

4. **Kotlin Coroutines:**
   https://kotlinlang.org/docs/coroutines-overview.html

---

## ⚠️ Важные замечания по безопасности

1. **Никогда не храните приватные ключи в plaintext**
2. **Используйте Android Keystore для всех криптографических операций**
3. **Включите ProGuard/R8 для обфускации кода в release сборке**
4. **Реализуйте certificate pinning для сетевого слоя**
5. **Запретите скриншоты в настройках безопасности (опционально)**

---

**Прототип готов к дальнейшей разработке!** 

Следующий шаг: откройте проект в Android Studio и начните реализацию недостающих компонентов согласно плану выше.
