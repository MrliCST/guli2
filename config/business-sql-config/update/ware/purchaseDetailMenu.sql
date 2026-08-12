-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835841, '采购单详情', '2087569641141997570', '1', 'purchaseDetail', 'guli/ware/purchaseDetail/index', 1, 0, 'C', '0', '0', 'guli:purchaseDetail:list', '#', 103, 1, sysdate(), null, null, '采购单详情菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835842, '采购单详情查询', 2087573005340835841, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchaseDetail:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835843, '采购单详情新增', 2087573005340835841, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchaseDetail:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835844, '采购单详情修改', 2087573005340835841, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchaseDetail:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835845, '采购单详情删除', 2087573005340835841, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchaseDetail:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087573005340835846, '采购单详情导出', 2087573005340835841, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:purchaseDetail:export',       '#', 103, 1, sysdate(), null, null, '');
