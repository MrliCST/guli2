USE `ry-cloud`;

-- 商品管理父菜单
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2079714826489909250, '商品管理', 0, '5', 'product', '', 1, 0, 'M', '0', '0', '', '#', 103, 1, sysdate(), null, null, '商品管理目录');

-- 商品管理-属性管理子菜单
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2086016318889484290, '商品维护', 2079714826489909250, 4, 'maintain', null, null, 1, 0, 'M', '0', '0', null, 'cascader', 103, 1, sysdate(), null, null, null);

-- /attribute 和 attribute 一个是绝对路径，一个是相对路径
-- 如果要点击子菜单，自动拼接父的路径在前，是需要使用相对路径
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083926009118793729, '属性管理', 2079714826489909250, 3, 'attribute', null, null, 1, 0, 'M', '0', '0', null, 'swagger', 103, 1, sysdate(), 1, sysdate(), null);

-- 仓储管理父菜单
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087551906857127937, '仓储管理', 0, '9', 'ware', '', 1, 0, 'M', '0', '0', '', 'list', 103, 1, sysdate(), null, null, '仓储管理目录');

-- 仓储管理-采购维护子目录
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087569641141997570, '采购维护', 2087551906857127937, 1, 'purchase', null, null, 1, 0, 'M', '0', '0', null, 'money', 103, 1, sysdate(), null, null, null);
