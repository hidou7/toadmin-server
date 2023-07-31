-- 用户表
create table sys_user (
	id              int8,
	name            varchar(20),
	email           varchar(50),
	phone           varchar(20),
	sex             int2,
	avatar          varchar(100),
	dept_id         int8,
	username        varchar(20),
	password        varchar(100),
	status          int2,
	login_ip        varchar(20),
	login_time      timestamp(0),
	creator         int8,
	create_time     timestamp(0),
	updater         int8,
	update_time     timestamp(0),
	deleted         int8 default 0,
	primary key(id)
);

create unique index uk_sysuser_username on sys_user (username, deleted);

comment on table sys_user is '用户表';
comment on column sys_user.name is '用户名称';
comment on column sys_user.email is '邮箱';
comment on column sys_user.phone is '联系电话';
comment on column sys_user.sex is '性别(0=女,1=男)';
comment on column sys_user.avatar is '头像';
comment on column sys_user.dept_id is '部门id';
comment on column sys_user.username is '登录账号';
comment on column sys_user.password is '登录密码';
comment on column sys_user.status is '状态(1=正常,2=停用)';
comment on column sys_user.login_ip is '登录ip';
comment on column sys_user.login_time is '登录时间';
comment on column sys_user.creator is '创建者';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.updater is '更新者';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.deleted is '删除时间戳(0=正常)';

insert into sys_user values (1, '超级管理员', null, null, 1, '', 1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, null, null, 1, now(), 1, now(), 0);

-- 部门表
create table sys_dept (
    id              int8,
    name            varchar(20),
    pid             int8 default 0,
    status          int2,
    sort            int2,
	creator         int8,
	create_time     timestamp(0),
	updater         int8,
	update_time     timestamp(0),
	deleted         int8 default 0,
	primary key(id)  
);

comment on table sys_dept is '部门表';
comment on column sys_dept.name is '部门名称';
comment on column sys_dept.pid is '上级部门id';
comment on column sys_dept.status is '状态(1=正常,2=停用)';
comment on column sys_dept.sort is '排序';
comment on column sys_dept.creator is '创建者';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.updater is '更新者';
comment on column sys_dept.update_time is '更新时间';
comment on column sys_dept.deleted is '删除时间戳(0=正常)';

insert into sys_dept values (1, 'to科技', 0, 1, 0, 1, now(), 1, now(), 0);

-- 角色表
create table sys_role (
    id              int8,
    name            varchar(20),
    data_scope      int2,
    status          int2,
    sort            int2,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_role is '角色表';
comment on column sys_role.name is '角色名称';
comment on column sys_role.data_scope is '数据权限(1=所有部门,2=指定部门,3=本部门,4=本部门及子部门,5=仅本人数据)';
comment on column sys_role.status is '状态(1=正常,2=停用)';
comment on column sys_role.sort is '排序';
comment on column sys_role.creator is '创建者';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.updater is '更新者';
comment on column sys_role.update_time is '更新时间';
comment on column sys_role.deleted is '删除时间戳(0=正常)';

insert into sys_role values (1, '超级管理员', 1, '1', 1, 1, now(), 1, now(), 0);

-- 角色数据权限关联表
create table sys_role_data_scope (
    id              int8,
    role_id         int8,
    dept_id         int8,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_role_data_scope is '角色数据权限关联表';
comment on column sys_role_data_scope.role_id is '角色ID';
comment on column sys_role_data_scope.dept_id is '部门ID';
comment on column sys_role_data_scope.creator is '创建者';
comment on column sys_role_data_scope.create_time is '创建时间';
comment on column sys_role_data_scope.updater is '更新者';
comment on column sys_role_data_scope.update_time is '更新时间';
comment on column sys_role_data_scope.deleted is '删除时间戳(0=正常)';

-- 用户角色关联表
create table sys_user_role (
    id              int8,    
    user_id         int8,
    role_id         int8,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_user_role is '用户角色关联表';
comment on column sys_user_role.user_id is '用户ID';
comment on column sys_user_role.role_id is '部门ID';
comment on column sys_user_role.creator is '创建者';
comment on column sys_user_role.create_time is '创建时间';
comment on column sys_user_role.updater is '更新者';
comment on column sys_user_role.update_time is '更新时间';
comment on column sys_user_role.deleted is '删除时间戳(0=正常)';

insert into sys_user_role values (1, 1, 1, 1, now(), 1, now(), 0);

-- 菜单表
create table sys_menu (
    id              int8,
    pid             int8 default 0,
    name            varchar(20),
    type            int2,
    permission      varchar(50),
    path            varchar(100),
    icon            varchar(100),
    status          int2,
    sort            int2,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_menu is '菜单表';
comment on column sys_menu.pid is '上级菜单id';
comment on column sys_menu.name is '菜单名称';
comment on column sys_menu.type is '菜单类型(1=菜单,2=按钮)';
comment on column sys_menu.permission is '权限标识';
comment on column sys_menu.path is '组件路径';
comment on column sys_menu.icon is '菜单图标';
comment on column sys_menu.status is '状态(1=正常,2=停用)';
comment on column sys_menu.sort is '排序';
comment on column sys_menu.creator is '创建者';
comment on column sys_menu.create_time is '创建时间';
comment on column sys_menu.updater is '更新者';
comment on column sys_menu.update_time is '更新时间';
comment on column sys_menu.deleted is '删除时间戳(0=正常)';

-- insert into sys_menu values (1, 0, '系统管理', );

-- 角色菜单关联表
create table sys_role_menu (
    id              int8,
    role_id         int8,
    menu_id         int8,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_role_menu is '角色菜单关联表';
comment on column sys_role_menu.role_id is '角色ID';
comment on column sys_role_menu.menu_id is '菜单ID';
comment on column sys_role_menu.creator is '创建者';
comment on column sys_role_menu.create_time is '创建时间';
comment on column sys_role_menu.updater is '更新者';
comment on column sys_role_menu.update_time is '更新时间';
comment on column sys_role_menu.deleted is '删除时间戳(0=正常)';

-- 字典类型表
create table sys_dict_type (
    id              int8,
    name            varchar(20),
    value           varchar(50),
    remark          varchar(50),
    sort            int2,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

create unique index uk_sysdicttype_value on sys_dict_type (value, deleted);

comment on table sys_dict_type is '字典类型表';
comment on column sys_dict_type.name is '字典名称';
comment on column sys_dict_type.value is '字典键';
comment on column sys_dict_type.remark is '备注';
comment on column sys_dict_type.sort is '排序';
comment on column sys_dict_type.creator is '创建者';
comment on column sys_dict_type.create_time is '创建时间';
comment on column sys_dict_type.updater is '更新者';
comment on column sys_dict_type.update_time is '更新时间';
comment on column sys_dict_type.deleted is '删除时间戳(0=正常)';

-- 字典值表
create table sys_dict_data (
    id              int8,
    name            varchar(20),
    type_id         int8,
    value           varchar(10),
    status          int2,
    remark          varchar(50),
    sort            int2,
    creator         int8,
    create_time     timestamp(0),
    updater         int8,
    update_time     timestamp(0),
    deleted         int8 default 0,
    primary key(id)
);

comment on table sys_dict_data is '字典类型表';
comment on column sys_dict_data.name is '字典项';
comment on column sys_dict_data.type_id is '字典类型ID';
comment on column sys_dict_data.value is '字典值';
comment on column sys_dict_data.status is '状态(1=正常,2=停用)';
comment on column sys_dict_data.remark is '备注';
comment on column sys_dict_data.sort is '排序';
comment on column sys_dict_data.creator is '创建者';
comment on column sys_dict_data.create_time is '创建时间';
comment on column sys_dict_data.updater is '更新者';
comment on column sys_dict_data.update_time is '更新时间';
comment on column sys_dict_data.deleted is '删除时间戳(0=正常)';