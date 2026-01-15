
-- ===============================================
-- PART 1: CREATE TABLES (3 Relations)
-- ===============================================

DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS conversations CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- TABLE 1: USERS
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_password_length CHECK (LENGTH(password) >= 6),
    CONSTRAINT chk_user_role CHECK (role IN ('STUDENT', 'ADMIN'))
);

-- TABLE 2: CONVERSATIONS
CREATE TABLE conversations (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraint
    CONSTRAINT fk_conversation_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_title_length CHECK (LENGTH(title) <= 255),
    CONSTRAINT chk_dates CHECK (updated_at >= created_at)
);

-- TABLE 3: MESSAGES
CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    conversation_id INTEGER NOT NULL,
    sender_type VARCHAR(10) NOT NULL,
    message_text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraint
    CONSTRAINT fk_message_conversation 
        FOREIGN KEY (conversation_id) 
        REFERENCES conversations(id) 
        ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_sender_type CHECK (sender_type IN ('USER', 'AI')),
    CONSTRAINT chk_message_not_empty CHECK (LENGTH(TRIM(message_text)) > 0)
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_conversations_user_id ON conversations(user_id);
CREATE INDEX idx_conversations_updated_at ON conversations(updated_at DESC);
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_timestamp ON messages(timestamp);


-- ===============================================
-- PART 2: CREATE VIEWS (5 Views)
-- ===============================================

-- VIEW 1: conversation_summary
CREATE OR REPLACE VIEW conversation_summary AS
SELECT 
    c.id AS conversation_id,
    c.title AS conversation_title,
    c.created_at,
    c.updated_at,
    u.id AS user_id,
    u.email AS user_email,
    u.role AS user_role,
    COUNT(m.id) AS message_count,
    MAX(m.timestamp) AS last_message_time
FROM conversations c
JOIN users u ON c.user_id = u.id
LEFT JOIN messages m ON c.id = m.conversation_id
GROUP BY c.id, c.title, c.created_at, c.updated_at, u.id, u.email, u.role
ORDER BY c.updated_at DESC;

-- VIEW 2: user_activity
CREATE OR REPLACE VIEW user_activity AS
SELECT 
    u.id AS user_id,
    u.email,
    u.role,
    COUNT(DISTINCT c.id) AS total_conversations,
    COUNT(m.id) AS total_messages,
    COUNT(CASE WHEN m.sender_type = 'USER' THEN 1 END) AS user_messages,
    COUNT(CASE WHEN m.sender_type = 'AI' THEN 1 END) AS ai_responses,
    MAX(c.updated_at) AS last_activity
FROM users u
LEFT JOIN conversations c ON u.id = c.user_id
LEFT JOIN messages m ON c.id = m.conversation_id
GROUP BY u.id, u.email, u.role
ORDER BY last_activity DESC NULLS LAST;

-- VIEW 3: recent_conversations
CREATE OR REPLACE VIEW recent_conversations AS
SELECT 
    c.id AS conversation_id,
    c.title,
    c.updated_at,
    u.email AS user_email,
    u.role AS user_role,
    (SELECT message_text 
     FROM messages 
     WHERE conversation_id = c.id 
     ORDER BY timestamp ASC 
     LIMIT 1) AS first_message,
    (SELECT COUNT(*) 
     FROM messages 
     WHERE conversation_id = c.id) AS message_count
FROM conversations c
JOIN users u ON c.user_id = u.id
ORDER BY c.updated_at DESC
LIMIT 50;

-- VIEW 4: admin_statistics
CREATE OR REPLACE VIEW admin_statistics AS
SELECT 
    (SELECT COUNT(*) FROM users WHERE role = 'STUDENT') AS total_students,
    (SELECT COUNT(*) FROM users WHERE role = 'ADMIN') AS total_admins,
    (SELECT COUNT(*) FROM users) AS total_users,
    (SELECT COUNT(*) FROM conversations) AS total_conversations,
    (SELECT COUNT(*) FROM messages) AS total_messages,
    (SELECT COUNT(*) FROM messages WHERE sender_type = 'USER') AS total_user_messages,
    (SELECT COUNT(*) FROM messages WHERE sender_type = 'AI') AS total_ai_responses,
    (SELECT AVG(msg_count) 
     FROM (SELECT COUNT(*) AS msg_count 
           FROM messages 
           GROUP BY conversation_id) AS avg_calc) AS avg_messages_per_conversation;

-- VIEW 5: conversation_details
CREATE OR REPLACE VIEW conversation_details AS
SELECT 
    c.id AS conversation_id,
    c.title AS conversation_title,
    c.created_at AS conversation_created,
    c.updated_at AS conversation_updated,
    u.email AS user_email,
    u.role AS user_role,
    m.id AS message_id,
    m.sender_type,
    m.message_text,
    m.timestamp AS message_timestamp
FROM conversations c
JOIN users u ON c.user_id = u.id
LEFT JOIN messages m ON c.id = m.conversation_id
ORDER BY c.id, m.timestamp;