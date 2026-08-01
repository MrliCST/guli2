USE `ry-cloud`;

-- 商品管理父菜单
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2079714826489909250, '商品管理', 0, '5', 'guli', '', 1, 0, 'M', '0', '0', '', '#', 103, 1, sysdate(), null, null, '商品管理目录');
