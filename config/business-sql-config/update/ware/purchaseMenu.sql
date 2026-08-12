-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384897, '采购信息', '2087569641141997570', '1', 'purchase', 'guli/ware/purchase/index', 1, 0, 'C', '0', '0', 'guli:purchase:list', '#', 103, 1, sysdate(), null, null, '采购信息菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384898, '采购信息查询', 2087570967282384897, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchase:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384899, '采购信息新增', 2087570967282384897, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchase:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384900, '采购信息修改', 2087570967282384897, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchase:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384901, '采购信息删除', 2087570967282384897, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchase:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087570967282384902, '采购信息导出', 2087570967282384897, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchase:export',       '#', 103, 1, sysdate(), null, null, '');
