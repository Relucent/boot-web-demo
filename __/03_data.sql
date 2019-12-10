-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES ('1000', '0', '演示机构', '1', 1000, '/1000/', 0, 0, '0', '0', NULL, NULL);

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('10000', '0', '系统管理', 2, '', '', '', '', 100, '/10000/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10100', '10000', '机构管理', 3, '10100', '/view/organization/organization-index.html', 'el-icon-office-building', '', 100, '/10000/10100/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10200', '10000', '用户管理', 3, '10200', '/view/user/user-index.html', 'el-icon-user', '', 100, '/10000/10200/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10300', '10000', '角色管理', 3, '10300', '/view/role/role-index.html', 'el-icon-trophy', '', 100, '/10000/10300/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10400', '10000', '功能管理', 3, '10400', '/view/permission/permission-index.html', 'el-icon-tickets', '', 100, '/10000/10400/', 0, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1000', '1000', '示例角色', '演示用的角色', 0, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('_admin', '0', 'admin', '{bcrypt}$2a$10$IqPmgREnTDlVbq.v00jfL.Sb5deR/RFLXLp.YGoDtaqGacqXkp54q', '管理员', '', '', '管理员', 1, 0, 0, NULL, '0', NULL, NULL);
INSERT INTO `user` VALUES ('_test', '1000', 'test1', '{bcrypt}$2a$10$E2HbfuRQDfaMVATOv.L42uIxIBZe/25Mgy0NrJoOvbzBY3ttwGy/.', '测试用户1', '男', '', '', 1, 0, 0, '_admin', '_admin', '2021-11-01 18:15:23', '2021-11-01 19:58:52');

