-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673409, '仓库信息', '2087551906857127937', '1', 'wareInfo', 'guli/ware/wareInfo/index', 1, 0, 'C', '0', '0', 'guli:wareInfo:list', '#', 103, 1, sysdate(), null, null, '仓库信息菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673410, '仓库信息查询', 2087554581302673409, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareInfo:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673411, '仓库信息新增', 2087554581302673409, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareInfo:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673412, '仓库信息修改', 2087554581302673409, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareInfo:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673413, '仓库信息删除', 2087554581302673409, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareInfo:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087554581302673414, '仓库信息导出', 2087554581302673409, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareInfo:export',       '#', 103, 1, sysdate(), null, null, '');
