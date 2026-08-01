USE `ry-cloud`;

-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153025691650, '商品三级分类', '2079714826489909250', '1', 'category', 'guli/category/index', 1, 0, 'C', '0', '0', 'guli:category:list', '#', 103, 1, sysdate(), null, null, '商品三级分类菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153029885953, '商品三级分类查询', 2080864153025691650, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:category:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153029885954, '商品三级分类新增', 2080864153025691650, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:category:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153029885955, '商品三级分类修改', 2080864153025691650, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:category:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153029885956, '商品三级分类删除', 2080864153025691650, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:category:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2080864153029885957, '商品三级分类导出', 2080864153025691650, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:category:export',       '#', 103, 1, sysdate(), null, null, '');
