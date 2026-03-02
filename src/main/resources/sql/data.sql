-- 预制角色

INSERT INTO sys_role (id, role_code, role_name)
SELECT 1, 'ROLE_ADMIN', '管理员'
    WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE id = 1);

INSERT INTO sys_role (id, role_code, role_name)
SELECT 2, 'ROLE_USER', '普通用户'
    WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE id = 2);

-- 预制权限

INSERT INTO sys_permission (id, permission_code, permission_name, type)
SELECT 1, 'user:add', '新增用户', 'API'
    WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = 1);

INSERT INTO sys_permission (id, permission_code, permission_name, type)
SELECT 2, 'user:delete', '删除用户', 'API'
    WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = 2);

INSERT INTO sys_permission (id, permission_code, permission_name, type)
SELECT 3, 'user:view', '查看用户', 'API'
    WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE id = 3);

-- 给角色赋予权限

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, 1
    WHERE NOT EXISTS (
  SELECT 1 FROM sys_role_permission WHERE role_id=1 AND permission_id=1
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, 2
    WHERE NOT EXISTS (
  SELECT 1 FROM sys_role_permission WHERE role_id=1 AND permission_id=2
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, 3
    WHERE NOT EXISTS (
  SELECT 1 FROM sys_role_permission WHERE role_id=1 AND permission_id=3
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, 3
    WHERE NOT EXISTS (
  SELECT 1 FROM sys_role_permission WHERE role_id=2 AND permission_id=3
);

-- 插入管理员用户

INSERT INTO sys_user (id, username, password, status)
SELECT 1, 'admin', '$2a$10$E44a2acmMaWyC771wm8btu.4oA96u9nN3M3g6o.oQpOTv6NdM6UOe', 1
    WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE id = 1);

-- 给管理员用户赋予管理员角色

INSERT INTO sys_user_role (user_id, role_id)
SELECT 1, 1
    WHERE NOT EXISTS (
  SELECT 1 FROM sys_user_role WHERE user_id=1 AND role_id=1
);


