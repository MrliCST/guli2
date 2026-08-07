-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474753, '属性值储', '2083926009118793729', '1', 'keyValStore', 'guli/attribute/keyValStore/index', 1, 0, 'C', '0', '0', 'guli:keyValStore:list', '#', 103, 1, sysdate(), null, null, '属性值储菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474754, '属性值储查询', 2084237765654474753, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:keyValStore:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474755, '属性值储新增', 2084237765654474753, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:keyValStore:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474756, '属性值储修改', 2084237765654474753, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:keyValStore:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474757, '属性值储删除', 2084237765654474753, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:keyValStore:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474758, '属性值储导出', 2084237765654474753, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:keyValStore:export',       '#', 103, 1, sysdate(), null, null, '');
