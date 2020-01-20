use `_demo`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `parent_id`			varchar(36)		NOT NULL	COMMENT '上级ID',
  `name`				varchar(255)	NOT NULL	COMMENT '名称',
  `remark`				varchar(2000)				COMMENT '备注',
  `ordinal`				varchar(20) 				COMMENT '序位',
  `id_path`				varchar(2000) 	DEFAULT ''	COMMENT 'ID路径',
  `deleted`				int(11)			NOT NULL	COMMENT '删除标记',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `department_id`		varchar(36)		NOT NULL	COMMENT '所属机构ID',
  `department_name` 	varchar(255)	NOT NULL	COMMENT '所属机构名称',
  `username`			varchar(255)	NOT NULL	COMMENT '用户名',
  `password`			varchar(255)	NOT NULL	COMMENT '登录密码(密文)',
  `name`				varchar(255)	NOT NULL	COMMENT '名称',
  `remark`				varchar(2000)				COMMENT '备注',
  `enabled`				int(11) 		DEFAULT 1	COMMENT '是否启用(0禁用,1启用)',
  `deleted`				int(11)			NOT NULL	COMMENT '删除标记(0正常,1删除)',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `name`				varchar(255)	NOT NULL	COMMENT '名称',
  `remark`				varchar(2000)				COMMENT '备注',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `user_id` 			varchar(36)		NOT NULL	COMMENT '用户ID',
  `role_id` 			varchar(36)		NOT NULL	COMMENT '角色ID',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;


DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `parent_id`			varchar(36)		NOT NULL	COMMENT '上级ID',
  `name`				varchar(255)	NOT NULL	COMMENT '名称',
  `remark`				varchar(2000)				COMMENT '备注',
  `type`				int(11)			NOT NULL	COMMENT '类别',
  `value`				varchar(2000)	NOT NULL	COMMENT '内容(访问路径)',
  `icon` 				varchar(255) 	DEFAULT ''	COMMENT '图标',
  `ordinal`				varchar(20) 				COMMENT '序位',
  `id_path`				varchar(2000) 	DEFAULT ''	COMMENT 'ID路径',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;




DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `role_id` 			varchar(36)		NOT NULL	COMMENT '角色ID',
  `permission_id`		varchar(36)		NOT NULL	COMMENT '权限ID',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `signal`;
CREATE TABLE `signal`  (
  `id`					varchar(36)		NOT NULL	COMMENT '主键',
  `value` 				bigint(20) 		NOT NULL	COMMENT '信号值',
  `created_by`			varchar(32)					COMMENT '创建者',
  `created_at`			datetime					COMMENT '创建时间',
  `updated_by`			varchar(32)					COMMENT '最后修改者',
  `updated_at`			datetime					COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;


DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence`  (
  `id` 			varchar(255) 	NOT NULL	COMMENT '主键',
  `current` 	bigint(20) 		NOT NULL DEFAULT 0 COMMENT '当前序列',
  `increment` 	bigint(20) 		NOT NULL DEFAULT 1 COMMENT '序列增量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

DROP FUNCTION IF EXISTS `current_sequence`;
delimiter ;;
CREATE DEFINER=`root`@`%` FUNCTION `current_sequence`(p1 VARCHAR(255)) RETURNS bigint(20)
BEGIN
	DECLARE VALUE BIGINT(20);
	SELECT current INTO VALUE FROM sequence WHERE id = p1;
	RETURN VALUE;
END
;;
delimiter ;

DROP FUNCTION IF EXISTS `next_sequence`;
delimiter ;;
CREATE DEFINER=`root`@`%` FUNCTION `next_sequence`(`p1` varchar(255)) RETURNS bigint(20)
BEGIN
	UPDATE sequence SET current = current + increment WHERE id = p1;
	RETURN current_sequence(p_name);
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;