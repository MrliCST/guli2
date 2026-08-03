USE `ry-cloud`;

-- 商品管理父菜单
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2079714826489909250, '商品管理', 0, '5', 'product', '', 1, 0, 'M', '0', '0', '', '#', 103, 1, sysdate(), null, null, '商品管理目录');

-- 商品管理-平台属性子菜单
-- /platformAttr 和 platformAttr 一个是绝对路径，一个是相对路径
-- 如果要点击子菜单，自动拼接父的路径在前，是需要使用相对路径
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query_param, is_frame, is_cache, menu_type, visible, status, perms, icon, create_dept, create_by, create_time, update_by, update_time, remark)
values(2083926009118793729, '平台属性', 2079714826489909250, 3, 'platformAttr', null, null, 1, 0, 'M', '0', '0', null, 'swagger', 103, 1, sysdate(), 1, sysdate(), null);
