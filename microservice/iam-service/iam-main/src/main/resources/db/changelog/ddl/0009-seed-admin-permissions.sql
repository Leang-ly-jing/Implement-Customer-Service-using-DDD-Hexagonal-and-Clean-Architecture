INSERT INTO permissions (uuid, name, resource, action, description, is_system, is_active, is_deleted, created_by, updated_by)
VALUES
    (gen_random_uuid(), 'user:create', 'user', 'create', 'Create users', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:read', 'user', 'read', 'Read users', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:update', 'user', 'update', 'Update users', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:delete', 'user', 'delete', 'Delete users', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'user:assign', 'user', 'assign', 'Assign roles and permissions to users', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:create', 'role', 'create', 'Create roles', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:read', 'role', 'read', 'Read roles', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:update', 'role', 'update', 'Update roles', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:delete', 'role', 'delete', 'Delete roles', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'role:assign', 'role', 'assign', 'Assign permissions to roles', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:create', 'permission', 'create', 'Create permissions', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:read', 'permission', 'read', 'Read permissions', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:update', 'permission', 'update', 'Update permissions', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM'),
    (gen_random_uuid(), 'permission:delete', 'permission', 'delete', 'Delete permissions', TRUE, TRUE, FALSE, 'SYSTEM', 'SYSTEM')
ON CONFLICT (name) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id, assigned_at, assigned_by, reason)
SELECT r.id, p.id, CURRENT_TIMESTAMP, 'SYSTEM', 'Seed admin permissions'
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.role_id = r.id AND rp.permission_id = p.id AND rp.revoked_at IS NULL
);

