USE `ry-cloud`;

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575681, '商品维护', '2086016318889484290', '1', 'maintain', 'guli/product/maintain/release/index', 1, 0, 'C', '0', '0', 'guli:maintain:list', '#', 103, 1, sysdate(), null, null, '商品维护菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575682, '商品维护查询', 2086017954357575681, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:maintain:query', '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575683, '商品维护新增', 2086017954357575681, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:maintain:add',   '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086017954357575684, '商品维护修改', 2086017954357575681, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:maintain:edit',  '#', 103, 1, sysdate(), null, null, '');
