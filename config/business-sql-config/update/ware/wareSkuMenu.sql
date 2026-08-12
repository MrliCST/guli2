-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741761, '商品库存', '2087551906857127937', '1', 'wareSku', 'guli/ware/wareSku/index', 1, 0, 'C', '0', '0', 'guli:wareSku:list', '#', 103, 1, sysdate(), null, null, '商品库存菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741762, '商品库存查询', 2087567255914741761, '1',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareSku:query',        '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741763, '商品库存新增', 2087567255914741761, '2',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareSku:add',          '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741764, '商品库存修改', 2087567255914741761, '3',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareSku:edit',         '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741765, '商品库存删除', 2087567255914741761, '4',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareSku:remove',       '#', 103, 1, sysdate(), null, null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2087567255914741766, '商品库存导出', 2087567255914741761, '5',  '#', '', 1, 0, 'F', '0', '0', 'guli:wareSku:export',       '#', 103, 1, sysdate(), null, null, '');
