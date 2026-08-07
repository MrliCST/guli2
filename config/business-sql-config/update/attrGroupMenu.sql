-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564737, '属性分组', '2083926009118793729', '1', 'attrGroup', 'guli/attribute/attrGroup/index', 1, 0, 'C', '0', '0', 'guli:attrGroup:list', '#', 103, 1, sysdate(), null, null, '属性分组菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564738, '属性分组查询', 2083927679432564737, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrGroup:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564739, '属性分组新增', 2083927679432564737, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrGroup:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564740, '属性分组修改', 2083927679432564737, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrGroup:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564741, '属性分组删除', 2083927679432564737, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrGroup:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083927679432564742, '属性分组导出', 2083927679432564737, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:attrGroup:export',       '#', 103, 1, sysdate(), null, null, '');
