-- 菜单 SQL
USE `ry-cloud`;

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994113, '品牌管理', '2079714826489909250', '1', 'brand', 'guli/brand/index', 1, 0, 'C', '0', '0', 'guli:brand:list', '#', 103, 1, sysdate(), null, null, '品牌管理菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994114, '品牌查询', 2082848552891994113, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:brand:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994115, '品牌新增', 2082848552891994113, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:brand:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994116, '品牌修改', 2082848552891994113, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:brand:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994117, '品牌删除', 2082848552891994113, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:brand:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2082848552891994118, '品牌导出', 2082848552891994113, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:brand:export',       '#', 103, 1, sysdate(), null, null, '');
