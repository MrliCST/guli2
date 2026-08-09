use `ry-cloud`;

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575681, '商品发布', '2086016318889484290', '1', 'release', 'guli/maintain/release/index', 1, 0, 'C', '0', '0', 'guli:release:list', '#', 103, 1, sysdate(), null, null, '商品发布菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575683, '商品发布新增', 2086017954357575681, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:release:add', '#', 103, 1, sysdate(), null, null, '');
