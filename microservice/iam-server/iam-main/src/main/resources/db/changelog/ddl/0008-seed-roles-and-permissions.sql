-- 1. Insert Core Permissions
INSERT INTO permissions (uuid, name, resource, action, description, is_system, is_active, created_by, updated_by)
VALUES
    (gen_random_uuid(), 'user:create', 'user', 'create', 'Create users', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:read', 'user', 'read', 'Read users', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:update', 'user', 'update', 'Update users', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:delete', 'user', 'delete', 'Delete users', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:assign', 'user', 'assign', 'Assign roles and permissions to users', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),

    (gen_random_uuid(), 'role:create', 'role', 'create', 'Create roles', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:read', 'role', 'read', 'Read roles', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:update', 'role', 'update', 'Update roles', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:delete', 'role', 'delete', 'Delete roles', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:assign', 'role', 'assign', 'Assign permissions to roles', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),

    (gen_random_uuid(), 'permission:create', 'permission', 'create', 'Create permissions', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:read', 'permission', 'read', 'Read permissions', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:update', 'permission', 'update', 'Update permissions', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:delete', 'permission', 'delete', 'Delete permissions', TRUE, TRUE, 'SYSTEM', 'SYSTEM')
    ON CONFLICT (name) DO NOTHING;

-- 2. Insert Core System Roles (ADMIN and USER)
INSERT INTO roles (uuid, name, description, is_system, is_active, created_by, updated_by)
VALUES
    ('0b9e6c1a-6a57-4a8e-9e0b-3a0f4d6f2c11', 'ADMIN', 'System Administrator with full access', TRUE, TRUE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'USER', 'Standard end user', TRUE, TRUE, 'SYSTEM', 'SYSTEM')
    ON CONFLICT (name) DO NOTHING;

-- 3. Assign All Permissions to ADMIN Role
INSERT INTO role_permissions (role_id, permission_id, assigned_at, assigned_by, reason)
SELECT r.id, p.id, CURRENT_TIMESTAMP, 'SYSTEM', 'Seed admin permissions'
FROM roles r
         CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;

-- 4. Assign Basic Read Permission to USER Role
INSERT INTO role_permissions (role_id, permission_id, assigned_at, assigned_by, reason)
SELECT r.id, p.id, CURRENT_TIMESTAMP, 'SYSTEM', 'Seed user permissions'
FROM roles r
         JOIN permissions p ON p.name = 'user:read'
WHERE r.name = 'USER'
    ON CONFLICT DO NOTHING;