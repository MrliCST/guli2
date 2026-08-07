-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474753, '商品属性', '2083926009118793729', '1', 'attrKeyValue', 'guli/platformAttr/attrKeyValue/index', 1, 0, 'C', '0', '0', 'guli:attrKeyValue:list', '#', 103, 1, sysdate(), null, null, '商品属性菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474754, '商品属性查询', 2084237765654474753, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrKeyValue:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474755, '商品属性新增', 2084237765654474753, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrKeyValue:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474756, '商品属性修改', 2084237765654474753, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrKeyValue:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474757, '商品属性删除', 2084237765654474753, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrKeyValue:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2084237765654474758, '商品属性导出', 2084237765654474753, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrKeyValue:export',       '#', 103, 1, sysdate(), null, null, '');
